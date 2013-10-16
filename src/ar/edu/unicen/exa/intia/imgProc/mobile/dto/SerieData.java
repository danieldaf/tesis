package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;

public class SerieData {
	
	private String label;
	private String color;
	private List<Double> valores;
	
	public SerieData(String label, String color, List<Double> valores) {
		this.label = label;
		this.color = color;
		this.valores = valores;
	}
	
	public SerieData(String label, List<Double> valores) {
		Random rnd = new Random();
		int r = rnd.nextInt(255);
		int g = rnd.nextInt(255);
		int b = rnd.nextInt(255);
		int clr = Color.rgb(r, g, b) & 0x00FFFFFF;
			
		String color = "#"+Integer.toHexString(clr);
		this.label = label;
		this.color = color;
		this.valores = valores;
	}

	public String getLabel() {
		return label;
	}

	public String getColor() {
		return color;
	}

	public List<Double> getValores() {
		return valores;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof SerieData) {
			SerieData serie = (SerieData)obj;
			return label.equals(serie.label);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return label.hashCode();
	}
	
	public JSONObject toJson() {
		try {
			JSONArray jsonValores = new JSONArray();
			for (Double valor : valores)
				jsonValores.put(valor);
			
			JSONObject json = new JSONObject();
			json.putOpt("data", jsonValores);
			json.putOpt("color", color);
			json.putOpt("name", label);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}