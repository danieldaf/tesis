package ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list.ItemModel;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.list_transformaciones)
public class ListTransformaciones extends Fragment implements
		OnItemClickListener, OnItemLongClickListener, OnFocusChangeListener, ListTransformacionesAdd.HandlerResult {

	private List<ItemModel<AlgTransformacion>> itemsSelected = new ArrayList<ItemModel<AlgTransformacion>>();

	@Bean
	protected ModeloDeDatos modeloDatos;

	@ViewById(R.id.list_view_transformaciones)
	protected ListView listView;
	@ViewById(R.id.list_view_empty_transformaciones)
	protected TextView emptyView;
	private ActionMode mActionMode;

	private AlgTransformacionAdapter listAdapter;

	private static class ViewHolder {
		public TextView txtTransformacion;
		public TextView txtTransformacionDetail;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listAdapter = new AlgTransformacionAdapter(getActivity(),
				modeloDatos.getListTransformaciones());

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mActionMode != null && view != null) {
			ItemModel<AlgTransformacion> itemModel = (ItemModel<AlgTransformacion>) listAdapter
					.getItem(position);
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
			listView.invalidateViews();
		}
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		if (mActionMode != null) {
			return false;
		}

		ItemModel<AlgTransformacion> itemModel = (ItemModel<AlgTransformacion>) listAdapter
				.getItem(position);
		itemModel.setSelected(true);
		if (itemModel.isSelected() && !itemsSelected.contains(itemModel))
			itemsSelected.add(itemModel);
		view.invalidate();
		listView.invalidateViews();

		mActionMode = getActivity().startActionMode(mActionModeCallback);
		return true;
	}

	private class AlgTransformacionAdapter extends
			ArrayAdapter<ItemModel<AlgTransformacion>> {
		protected final Context context;

		public AlgTransformacionAdapter(Context context,
				List<AlgTransformacion> listadoDatos) {
			super(context, R.layout.list_transformaciones_item, ItemModel
					.buildModel(listadoDatos));
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			ViewHolder viewHolder;

			if (itemView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				itemView = inflater.inflate(R.layout.list_transformaciones_item,
						parent, false);

				viewHolder = new ViewHolder();
				viewHolder.txtTransformacion = (TextView) itemView
						.findViewById(R.id.list_transformacion_item);
				viewHolder.txtTransformacionDetail = (TextView) itemView.findViewById(R.id.list_transformacion_item_detail); 
				itemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) itemView.getTag();
			}

			ItemModel<AlgTransformacion> itemModel = getItem(position);
			String strTransformacion = "";
			String strDetalle = "";
			AlgTransformacion item = itemModel.getItem();
			if (item != null) {
				strTransformacion += item.getTipo().getNombre();
				strDetalle += item.getJsonCaracteristicas()!=null?item.getJsonCaracteristicas():"<sin definir>";
			} else {
				strTransformacion += "?";
				strDetalle += "?";
			}
			viewHolder.txtTransformacion.setText(strTransformacion);
			viewHolder.txtTransformacionDetail.setText(strDetalle);

			int backgroundColor = android.R.color.transparent;
			if (itemModel.isSelected()) {
				backgroundColor = android.R.color.holo_blue_light;
			}
			itemView.setBackgroundColor(Resources.getSystem().getColor(
					backgroundColor));

			return itemView;
		}

		public void addItem(AlgTransformacion item) {
			ItemModel<AlgTransformacion> itemModel = ItemModel.buildItem(item);
			this.add(itemModel);
		}
		
		public void removeItem(ItemModel<AlgTransformacion> itemModel) {
			this.remove(itemModel);
		}
		
//		public void updateItem(ItemModel<AlgTransformacion> itemModelNew) {
//			int posModel = this.getPosition(itemModelNew);
//			ItemModel<AlgTransformacion> itemModelOld = this.getItem(posModel);
//			itemModelOld.setItem(itemModelNew.getItem());
//		}
		
		public List<AlgTransformacion> getListadoDatos() {
			ArrayList<AlgTransformacion> result = new ArrayList<AlgTransformacion>();
			ItemModel<AlgTransformacion> itemModel = null;
			int cantidad = this.getCount();
			for (int pos=0; pos<cantidad; pos++) {
				itemModel = getItem(pos);
				result.add(itemModel.getItem());
			}
			return result;
		}
		
		public void setListadoDatos(List<AlgTransformacion> listadoDatos) {
			super.clear();
			super.addAll(ItemModel.buildModel(listadoDatos));
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
		modeloDatos.setListTransformaciones(listAdapter.getListadoDatos());
	}

	@Override
	public void onResume() {
		super.onResume();
		listAdapter.setListadoDatos(modeloDatos.getListTransformaciones());
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if ((v == listView && !listAdapter.isEmpty())
				|| (v == emptyView && listAdapter.isEmpty())) {
			if (!hasFocus) {
				if (mActionMode != null) {
					mActionMode.finish();
				}
				modeloDatos.setListTransformaciones(listAdapter.getListadoDatos());
			}
		}
	}

	public void invokeTransformacionAction() {
//		Toast.makeText(this.getActivity(), "Accion agregar transformacion",
//				Toast.LENGTH_SHORT).show();
		ListTransformacionesAdd addDialog = new ListTransformacionesAdd();
		addDialog.addHandlerResult(this);
		addDialog.show(getFragmentManager(), "listTransformacionesAdd");
	}
	
	public void flushDatos() {
		modeloDatos.setListTransformaciones(listAdapter.getListadoDatos());
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		private boolean deleteItems;
		private MenuItem mnuRemoveItems;

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.list_transformaciones_onselection, menu);
			mnuRemoveItems = menu.findItem(R.id.mnu_list_remove_items_selected);
			return true;
		}

		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			boolean result = false;
			deleteItems = false;
			boolean mnuRemoveItemsEnabled = itemsSelected != null
					&& !itemsSelected.isEmpty();
			if (mnuRemoveItemsEnabled != mnuRemoveItems.isEnabled()) {
				mnuRemoveItems.setEnabled(mnuRemoveItemsEnabled);
				mnuRemoveItems.getIcon().setAlpha(
						mnuRemoveItemsEnabled ? 255 : 128);
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
			for (ItemModel<AlgTransformacion> itemSelected : itemsSelected) {
				itemSelected.setSelected(false);
				if (deleteItems)
					listAdapter.removeItem(itemSelected);
			}
			itemsSelected.clear();
			mActionMode = null;
			listView.invalidateViews();
		}
	};

	@Override
	public void addItem(ListTransformacionesAdd dialog, AlgTransformacionEnum alg) {
		if (alg.isEnabled()) {
			AlgTransformacion item = new AlgTransformacion(null, alg, alg.getDefaultJsonCaracteristicas());
			listAdapter.addItem(item);
		} else {
			Toast.makeText(this.getActivity(), "Transformación no implementada.", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void cancel(ListTransformacionesAdd dialog) {
	}
}
