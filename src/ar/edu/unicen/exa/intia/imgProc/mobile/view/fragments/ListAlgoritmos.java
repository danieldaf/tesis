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
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list.ItemModel;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.list_algoritmos)
public class ListAlgoritmos extends Fragment implements
		OnItemClickListener, OnItemLongClickListener, OnFocusChangeListener, ListAlgoritmosAdd.HandlerResult {

	private List<ItemModel<AlgCombinacion>> itemsSelected = new ArrayList<ItemModel<AlgCombinacion>>();
	
	@Bean
	protected ModeloDeDatos modeloDatos;
	
	@ViewById(R.id.list_view_algoritmos)
	protected ListView listView;
	@ViewById(R.id.list_view_empty_algoritmos)
	protected TextView emptyView;
	private ActionMode mActionMode;	
	
	private AlgCombinacionAdapter listAdapter;

	private static class ViewHolder {
		public TextView txtDetector;
		public TextView txtExtractor;
		public TextView txtMatcher;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listAdapter = new AlgCombinacionAdapter(getActivity(), modeloDatos.getListAlgoritmos());

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		listView.setOnItemLongClickListener(this);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mActionMode != null && view != null) {
			ItemModel<AlgCombinacion> itemModel = (ItemModel<AlgCombinacion>) listAdapter.getItem(position);
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

		ItemModel<AlgCombinacion> itemModel = (ItemModel<AlgCombinacion>) listAdapter.getItem(position);
		itemModel.setSelected(true);
//		if (itemModel.isSelected() && !itemsSelected.contains(itemModel))
			itemsSelected.add(itemModel);
//		view.invalidate();
		listView.invalidateViews();
//		listAdapter.notifyDataSetChanged();

		mActionMode = getActivity().startActionMode(mActionModeCallback);
		return true;
	}

	private class AlgCombinacionAdapter extends
			ArrayAdapter<ItemModel<AlgCombinacion>> {
		protected final Context context;

		public AlgCombinacionAdapter(Context context,
				List<AlgCombinacion> listadoDatos) {
			super(context, R.layout.list_algoritmos_item, ItemModel
					.buildModel(listadoDatos));
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			ViewHolder viewHolder;

			if (itemView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				itemView = inflater.inflate(R.layout.list_algoritmos_item,
						parent, false);

				viewHolder = new ViewHolder();
				viewHolder.txtDetector = (TextView) itemView
						.findViewById(R.id.list_algoritmo_item_detector);
				viewHolder.txtExtractor = (TextView) itemView
						.findViewById(R.id.list_algoritmo_item_extractor);
				viewHolder.txtMatcher = (TextView) itemView
						.findViewById(R.id.list_algoritmo_item_matcher);

				itemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) itemView.getTag();
			}

			ItemModel<AlgCombinacion> itemModel = getItem(position);
			String strDetector = "Detector: ";
			String strExtractor = "Extractor: ";
			String strMatcher = "Matcher: ";
			AlgCombinacion item = itemModel.getItem();
			if (item != null) {
				strDetector += item.getDetector().getNombre();
				strExtractor += item.getExtractor().getNombre();
				strMatcher += item.getMatcher().getNombre();
			} else {
				strDetector += "?";
				strExtractor += "?";
				strMatcher += "?";
			}
			viewHolder.txtDetector.setText(strDetector);
			viewHolder.txtExtractor.setText(strExtractor);
			viewHolder.txtMatcher.setText(strMatcher);

			int backgroundColor = android.R.color.transparent;
			if (itemModel.isSelected()) {
				backgroundColor = android.R.color.holo_blue_light;
			}
			itemView.setBackgroundColor(Resources.getSystem().getColor(
					backgroundColor));

			return itemView;
		}
		
		public void addItem(AlgCombinacion item) {
			ItemModel<AlgCombinacion> itemModel = ItemModel.buildItem(item);
			this.add(itemModel);
		}
		
		public void removeItem(ItemModel<AlgCombinacion> itemModel) {
			this.remove(itemModel);
		}
		
//		public void updateItem(ItemModel<AlgCombinacion> itemModelNew) {
//			int posModel = this.getPosition(itemModelNew);
//			ItemModel<AlgCombinacion> itemModelOld = this.getItem(posModel);
//			itemModelOld.setItem(itemModelNew.getItem());
//		}
		
		public List<AlgCombinacion> getListadoDatos() {
			ArrayList<AlgCombinacion> result = new ArrayList<AlgCombinacion>();
			ItemModel<AlgCombinacion> itemModel = null;
			int cantidad = this.getCount();
			for (int pos=0; pos<cantidad; pos++) {
				itemModel = getItem(pos);
				result.add(itemModel.getItem());
			}
			return result;
		}
		
		public void setListadoDatos(List<AlgCombinacion> listadoDatos) {
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
		modeloDatos.setListAlgoritmos(listAdapter.getListadoDatos());
	}

	@Override
	public void onResume() {
		super.onResume();
		listAdapter.setListadoDatos(modeloDatos.getListAlgoritmos());
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if ((v == listView && !listAdapter.isEmpty()) || (v == emptyView && listAdapter.isEmpty())) {
			if (!hasFocus) {
				if (mActionMode != null) {
					mActionMode.finish();
				}
				modeloDatos.setListAlgoritmos(listAdapter.getListadoDatos());
			}
		}
	}
	
	public void invokeAlgoritmoAddAction() {
//		Toast.makeText(this.getActivity(), "Accion agregar algoritmo", Toast.LENGTH_SHORT).show();
		ListAlgoritmosAdd addDialog = new ListAlgoritmosAdd();
		addDialog.addHandlerResult(this);
		addDialog.show(getFragmentManager(), "listAlgoritmosAdd");
	}
	
	public void flushDatos() {
		modeloDatos.setListAlgoritmos(listAdapter.getListadoDatos());
	}

	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
		
		private boolean deleteItems;
		private MenuItem mnuRemoveItems;

		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.list_algoritmos_onselection, menu);
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
			for (ItemModel<AlgCombinacion> itemSelected : itemsSelected) {
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
	public void addItem(ListAlgoritmosAdd dialog, AlgCombinacion alg) {
		listAdapter.addItem(alg);
	}

	@Override
	public void cancel(ListAlgoritmosAdd dialog) {
	}
}
