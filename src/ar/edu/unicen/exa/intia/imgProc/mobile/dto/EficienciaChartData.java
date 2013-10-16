package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EficienciaChartData extends ChartData {

	private AlgTransformacion algTransformacion = null;
	private String unidadMetrica;
	private List<String> itemLabels;
	private List<String> itemColors;
	
	public EficienciaChartData() {
	}
	public EficienciaChartData(AlgTransformacion algT) {
		this.algTransformacion = algT;
	}

	public AlgTransformacion getAlgTransformacion() {
		return algTransformacion;
	}
	public String getUnidadMetrica() {
		return unidadMetrica;
	}
	public void setUnidadMetrica(String unidadMetrica) {
		this.unidadMetrica = unidadMetrica;
	}
	public List<String> getItemLabels() {
		return itemLabels;
	}
	public void setItemLabels(List<String> itemLabels) {
		this.itemLabels = itemLabels;
	}
	public List<String> getItemColors() {
		return itemColors;
	}
	public void setItemColors(List<String> itemColors) {
		this.itemColors = itemColors;
	}

	@Override
	public JSONObject toJson() {
		try {
			JSONObject json = super.toJson();
			json.put("unidadMetrica", this.unidadMetrica);
			json.put("itemLabels", new JSONArray(this.itemLabels));
			json.put("itemColors", new JSONArray(this.itemColors));
			return json;
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
