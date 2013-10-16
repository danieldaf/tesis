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
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgDeteccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgExtraccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgMatching;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.list.ItemModel;

public class ListAlgoritmosAdd extends DialogFragment implements
		OnItemSelectedListener {
	
	private Spinner listDetectores;
	private Spinner listExtractores;
	private Spinner listMatchers;
	
	private AlgDeteccion algDeteccionSelected;
	private AlgExtraccion algExtraccionSelected;
	private AlgMatching algMatchingSelected;
	
	private HandlerResult handler;
	
	public interface HandlerResult {
		public void addItem(ListAlgoritmosAdd dialog, AlgCombinacion alg);
		public void cancel(ListAlgoritmosAdd dialog);
	}
	
	public void addHandlerResult (HandlerResult handler) {
		this.handler = handler;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog result = null;
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.list_algoritmos_add, null);
		builder.setTitle(R.string.str_add_algoritmo);
		builder.setView(view);
		builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (handler != null) {
					AlgCombinacion alg = null;
					AlgDeteccion detector = ListAlgoritmosAdd.this.algDeteccionSelected;
					AlgExtraccion extractor = ListAlgoritmosAdd.this.algExtraccionSelected;
					AlgMatching matcher = ListAlgoritmosAdd.this.algMatchingSelected;
					if (detector != null && extractor != null && matcher != null) {
						alg = new AlgCombinacion(null, null, detector, extractor, matcher);
					}
					handler.addItem(ListAlgoritmosAdd.this, alg);
				}
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ListAlgoritmosAdd.this.getDialog().cancel();
				if (handler != null) {
					handler.cancel(ListAlgoritmosAdd.this);
				}
			}
		});
		listDetectores = (Spinner)view.findViewById(R.id.sub_list_detectores);
		listExtractores = (Spinner)view.findViewById(R.id.sub_list_extractores);
		listMatchers = (Spinner)view.findViewById(R.id.sub_list_matchers);

		AlgAdapter<AlgDeteccion> listDetectoresAdapter = new AlgAdapter<AlgDeteccion>(getActivity(), AlgDeteccion.values());
		AlgAdapter<AlgExtraccion> listExtractoresAdapter = new AlgAdapter<AlgExtraccion>(getActivity(), AlgExtraccion.values());
		AlgAdapter<AlgMatching> listMatchersAdapter = new AlgAdapter<AlgMatching>(getActivity(), AlgMatching.values());
		
		algDeteccionSelected = listDetectoresAdapter.getItem(0).getItem();
		algExtraccionSelected = listExtractoresAdapter.getItem(0).getItem();
		algMatchingSelected = listMatchersAdapter.getItem(0).getItem();
		
		listDetectores.setAdapter(listDetectoresAdapter);
		listDetectores.setOnItemSelectedListener(this);
		listExtractores.setAdapter(listExtractoresAdapter);
		listExtractores.setOnItemSelectedListener(this);
		listMatchers.setAdapter(listMatchersAdapter);
		listMatchers.setOnItemSelectedListener(this);
	
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
		if (parent == listDetectores) {
			ItemModel<AlgDeteccion> item = (ItemModel<AlgDeteccion>)listDetectores.getItemAtPosition(position);
			algDeteccionSelected = item.getItem();
		} else if (parent == listExtractores) {
			ItemModel<AlgExtraccion> item = (ItemModel<AlgExtraccion>)listExtractores.getItemAtPosition(position);
			algExtraccionSelected = item.getItem();
		} else if (parent == listMatchers) {
			ItemModel<AlgMatching> item = (ItemModel<AlgMatching>)listMatchers.getItemAtPosition(position);
			algMatchingSelected = item.getItem();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
	}

}
