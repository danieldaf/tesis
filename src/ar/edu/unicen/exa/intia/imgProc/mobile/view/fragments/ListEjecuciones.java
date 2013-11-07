package ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ar.edu.unicen.exa.intia.imgProc.mobile.EstadisticasActivity_;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EjecucionDto;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.EjecucionUtils;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list.ItemModel;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

@EFragment(R.layout.list_ejecuciones)
public class ListEjecuciones extends Fragment implements OnItemClickListener {

	@Bean
	protected ModeloDeDatos modeloDatos;

	@ViewById(R.id.list_view_ejecuciones)
	protected ListView listView;
	@ViewById(R.id.list_view_empty_ejecuciones)
	protected TextView emptyView;

	private AlgEjecucionAdapter listAdapter;

	private static class ViewHolder {
		public ImageView viewImage;
		public TextView txtEjecucionFecha;
		public TextView txtEjecucionAlg;
		public TextView txtEjecucionDetail;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		listAdapter = new AlgEjecucionAdapter(this.getActivity(), EjecucionUtils.obtenerListadoEjecuciones(modeloDatos.getResultadosConfig().getFolderPathOutResourcesBase()));

		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(this);
		listView.setEmptyView(emptyView);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ItemModel<EjecucionDto> itemModel = (ItemModel<EjecucionDto>)listAdapter.getItem(position);
		if (itemModel != null && itemModel.getItem() != null) {
			EjecucionDto item = itemModel.getItem();
			
			Intent estadisticsIntent = new Intent(this.getActivity(), EstadisticasActivity_.class); 
			estadisticsIntent.putExtra(EstadisticasActivity_.EXTRA_PATH, item.getPathDirectory());
			startActivity(estadisticsIntent);
		}
	}

	private class AlgEjecucionAdapter extends
			ArrayAdapter<ItemModel<EjecucionDto>> {
		protected final Context context;

		public AlgEjecucionAdapter(Context context,
				List<EjecucionDto> listadoDatos) {
			super(context, R.layout.list_ejecuciones_item, ItemModel
					.buildModel(listadoDatos));
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;
			ViewHolder viewHolder;

			if (itemView == null) {
				LayoutInflater inflater = LayoutInflater.from(context);
				itemView = inflater.inflate(R.layout.list_ejecuciones_item,
						parent, false);

				viewHolder = new ViewHolder();
				viewHolder.viewImage = (ImageView) itemView.findViewById(R.id.list_ejecucion_item_image);
				viewHolder.txtEjecucionFecha = (TextView) itemView.findViewById(R.id.list_ejecucion_item_fecha);
				viewHolder.txtEjecucionAlg = (TextView) itemView.findViewById(R.id.list_ejecucion_item_alg);
				viewHolder.txtEjecucionDetail = (TextView) itemView.findViewById(R.id.list_ejecucion_item_detail); 
				itemView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) itemView.getTag();
			}

			ItemModel<EjecucionDto> itemModel = getItem(position);
			String strImage = "";
			String strFecha = "";
			String strAlgoritmo = "";
			String strDetalle = "";
			EjecucionDto item = itemModel.getItem();
			if (item != null) {
				strImage = item.getImagePath();
				strFecha = item.getFechaInicio();
				strAlgoritmo = item.getAlgoritmos();
				strDetalle = "Cant Imágenes:"+item.getCantImagenes() +" Cant Transformaciones:"+item.getCantTransformaciones();
			} else {
				strImage = null;
				strFecha = "?";
				strAlgoritmo = "?";
				strDetalle = "?";
			}
			if (strImage == null)
				viewHolder.viewImage.setImageResource(R.drawable.no_image_available);
			else
				viewHolder.viewImage.setImageURI(Uri.parse(strImage));
			viewHolder.txtEjecucionFecha.setText(strFecha);
			viewHolder.txtEjecucionAlg.setText(strAlgoritmo);
			viewHolder.txtEjecucionDetail.setText(strDetalle);

			int backgroundColor = android.R.color.transparent;
			if (itemModel.isSelected()) {
				backgroundColor = android.R.color.holo_blue_light;
			}
			itemView.setBackgroundColor(Resources.getSystem().getColor(
					backgroundColor));

			return itemView;
		}
		
		public void setListadoDatos(List<EjecucionDto> listadoDatos) {
			super.clear();
			super.addAll(ItemModel.buildModel(listadoDatos));
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		listAdapter.setListadoDatos(EjecucionUtils.obtenerListadoEjecuciones(modeloDatos.getResultadosConfig().getFolderPathOutResourcesBase()));
	}
	
	public void refreshListado() {
		listAdapter.setListadoDatos(EjecucionUtils.obtenerListadoEjecuciones(modeloDatos.getResultadosConfig().getFolderPathOutResourcesBase()));
		Toast.makeText(this.getActivity(), R.string.list_ejecuciones_refreshing, Toast.LENGTH_SHORT).show();
	}

}
