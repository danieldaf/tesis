package ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.BitmapUtils;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.list_source_imagenes)
public class ListSourceImagenes extends Fragment implements OnItemClickListener, OnItemLongClickListener {

	private static int IMG_REQUEST_CODE = 2013;
	private static int WIDTH = 128;
	private static int HEIGHT = 128;
	
	@ViewById(R.id.lst_src_images)
	protected GridView lstSrcImages;
	
	@Bean
	protected ModeloDeDatos modeloDatos;
	
	private List<ImagenOrigen> listaImagenes = new ArrayList<ImagenOrigen>();
	private List<ImagenOrigenView> itemsSelected = new ArrayList<ImagenOrigenView>();
	private ImageAdapter imageAdapter = new ImageAdapter();
	
	private ActionMode mActionMode;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		listaImagenes.addAll(modeloDatos.getListImagenes());
		lstSrcImages.setColumnWidth(WIDTH+4);
		lstSrcImages.setOnItemClickListener(this);
		lstSrcImages.setOnItemLongClickListener(this);
		lstSrcImages.setAdapter(imageAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mActionMode != null && view != null) {
			ImagenOrigenView itemModel = (ImagenOrigenView)imageAdapter.getItem(position);
			itemModel.setSelected(!itemModel.isSelected());
			boolean invalidateActionMode = false;
			if (itemModel.isSelected() && !itemsSelected.contains(itemModel)) {
				invalidateActionMode = itemsSelected.isEmpty();
				itemsSelected.add(itemModel);
			}
			if (!itemModel.isSelected() && itemsSelected.contains(itemModel)) {
				itemsSelected.remove(itemModel);
				invalidateActionMode = itemsSelected.isEmpty();
			}
			if (invalidateActionMode)
				mActionMode.invalidate();
			lstSrcImages.invalidateViews();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (mActionMode != null) {
			return false;
		}

		ImagenOrigenView itemModel = (ImagenOrigenView)imageAdapter.getItem(position);
		itemModel.setSelected(true);
		itemsSelected.add(itemModel);
		lstSrcImages.invalidateViews();

		mActionMode = getActivity().startActionMode(mActionModeCallback);
		return true;
	}

	public void invokeSourceImageAction() {
		// Toast.makeText(this.getActivity(), "Accion agregar imagen",
		// Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("image/*");
		startActivityForResult(intent, IMG_REQUEST_CODE);
	}
	
	public void flushDatos() {
		modeloDatos.setListImagenes(listaImagenes);
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		private boolean deleteItems;
		private MenuItem mnuRemoveItems;

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.list_source_images_onselection, menu);
			mnuRemoveItems = menu.findItem(R.id.mnu_list_remove_items_selected);
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			boolean result = false;
			deleteItems = false;
			boolean mnuRemoveItemsEnabled = itemsSelected != null && !itemsSelected.isEmpty();
			if (mnuRemoveItemsEnabled != mnuRemoveItems.isEnabled()) {
				mnuRemoveItems.setEnabled(mnuRemoveItemsEnabled);
				mnuRemoveItems.getIcon().setAlpha(mnuRemoveItemsEnabled?255:128);
				result = true;
			}
			
			return result; 
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
			case R.id.mnu_list_remove_items_selected:
				deleteItems = true;
				mActionMode.finish();
				return true;
			default:
				return false;
			}
		}

		public void onDestroyActionMode(ActionMode mode) {
			for (ImagenOrigenView itemSelected : itemsSelected) {
				itemSelected.setSelected(false);
				if (deleteItems)
					listaImagenes.remove(itemSelected);
			}
			itemsSelected.clear();
			mActionMode = null;
//			imageAdapter.notifyDataSetChanged();
			lstSrcImages.invalidateViews();
		}
	};
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == IMG_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
			Uri uriImgSelected = data.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };

			Cursor cursor = getActivity().getContentResolver().query(
					uriImgSelected, filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();

			BitmapFactory.Options opts = BitmapUtils.buildBmpOptions(filePath, WIDTH, HEIGHT);
			
			String nombre = filePath;
			int pos = filePath.lastIndexOf(File.separator);
			if (pos >= 0)
				nombre = filePath.substring(pos+1);
			Uri uri = BitmapUtils.uriFromPath(filePath); 
			ImagenOrigenView img = new ImagenOrigenView(nombre, uri.toString(), opts); 
			listaImagenes.add(img);
			imageAdapter.notifyDataSetChanged();
		}
	}
	
	public void focusOut() {
		if (mActionMode != null) {
			mActionMode.finish();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		focusOut();
		modeloDatos.setListImagenes(listaImagenes);
	}

	@Override
	public void onResume() {
		super.onResume();
		listaImagenes = new ArrayList<ImagenOrigen>();
		listaImagenes.addAll(modeloDatos.getListImagenes());
		if (imageAdapter != null)
			imageAdapter.notifyDataSetChanged();
	}

//	@Override
//	public void onFocusChange(View v, boolean hasFocus) {
//		if ((v == listView && !listAdapter.isEmpty()) || (v == emptyView && listAdapter.isEmpty())) {
//			if (!hasFocus) {
//				if (mActionMode != null) {
//					mActionMode.finish();
//				}
//				modeloDatos.setListAlgoritmos(listAdapter.getListadoDatos());
//			}
//		}
//	}
	
	protected class ImagenOrigenView extends ImagenOrigen {
		
		protected BitmapFactory.Options options;
		protected boolean selected;
		
		public ImagenOrigenView(String nombre, String originalUri, BitmapFactory.Options options) {
			super(null, nombre, originalUri, null);
			this.options = options;
			this.selected = false;
		}
		
		public BitmapFactory.Options getOptions() {
			return options;
		}
		
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		public boolean isSelected() {
			return selected;
		}
	}
	
	protected class ImageAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return listaImagenes.size();
		}

		@Override
		public Object getItem(int position) {
			return listaImagenes.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imgView = null;
			if (convertView == null || !(convertView instanceof ImageView)) {
				imgView = new ImageView(getActivity());
				imgView.setLayoutParams(new GridView.LayoutParams(WIDTH, HEIGHT));
				imgView.setScaleType(ImageView.ScaleType.CENTER_CROP);
				imgView.setPadding(4, 4, 4, 4);
			} else {
				imgView = (ImageView)convertView;
			}
			
			ImagenOrigenView img = null;
			ImagenOrigen imgTmp = listaImagenes.get(position);
			String filePath = BitmapUtils.pathFromUri(Uri.parse(imgTmp.getOriginalUri()));
			if (!(imgTmp instanceof ImagenOrigenView)) {
				BitmapFactory.Options opts = BitmapUtils.buildBmpOptions(filePath, WIDTH, HEIGHT);
				img = new ImagenOrigenView(imgTmp.getNombre(), imgTmp.getOriginalUri(), opts);
				listaImagenes.set(position, img);
			} else {
				img = (ImagenOrigenView)imgTmp;
			}

			Object tagPath = imgView.getTag();
			if (tagPath == null || !tagPath.toString().equals(filePath)) {
				imgView.setTag(filePath);
				Bitmap bmp = BitmapFactory.decodeFile(filePath, img.getOptions());
				imgView.setImageBitmap(bmp);
			}

			int backgroundColor = android.R.color.transparent;
			if (img.isSelected()) {
				backgroundColor = android.R.color.holo_blue_light;
			}
			imgView.setBackgroundColor(Resources.getSystem().getColor(backgroundColor));
			
			return imgView;
		}
	}
}