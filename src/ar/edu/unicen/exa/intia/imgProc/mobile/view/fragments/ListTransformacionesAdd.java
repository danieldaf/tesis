package ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list.ItemModel;

public class ListTransformacionesAdd extends DialogFragment implements
		OnItemSelectedListener {
	
	private Spinner listTransformaciones;
	
	private AlgTransformacionEnum algTransformacionSelected;
	
	private HandlerResult handler;
	
	public interface HandlerResult {
		public void addItem(ListTransformacionesAdd dialog, AlgTransformacionEnum alg);
		public void cancel(ListTransformacionesAdd dialog);
	}
	
	public void addHandlerResult (HandlerResult handler) {
		this.handler = handler;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog result = null;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.list_transformaciones_add, null);
		builder.setTitle(R.string.str_add_transformacion);
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (handler != null) {
					AlgTransformacionEnum alg = ListTransformacionesAdd.this.algTransformacionSelected;
					handler.addItem(ListTransformacionesAdd.this, alg);
					dialog.dismiss();
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ListTransformacionesAdd.this.getDialog().cancel();
				if (handler != null) {
					handler.cancel(ListTransformacionesAdd.this);
				}
			}
		});
		listTransformaciones = (Spinner)view.findViewById(R.id.sub_list_transformaciones);

		AlgAdapter<AlgTransformacionEnum> listTransformacionesAdapter = new AlgAdapter<AlgTransformacionEnum>(getActivity(), AlgTransformacionEnum.values());
		
		algTransformacionSelected = listTransformacionesAdapter.getItem(0).getItem();
		
		listTransformaciones.setAdapter(listTransformacionesAdapter);
		listTransformaciones.setOnItemSelectedListener(this);
	
		result = builder.create();
		return result;
	}

	private class AlgAdapter<T> extends
			ArrayAdapter<ItemModel<T>> {
		public AlgAdapter(Context context,
				T[] listadoDatos) {
			super(context, android.R.layout.simple_spinner_item, ItemModel
					.buildModel(listadoDatos));
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		if (parent == listTransformaciones) {
			ItemModel<AlgTransformacionEnum> item = (ItemModel<AlgTransformacionEnum>)listTransformaciones.getItemAtPosition(position);
			algTransformacionSelected = item.getItem();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

}
