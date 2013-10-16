package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ChartData {

	protected int width;
	protected int height;
	
	protected String idTag;
	protected String titulo;
	protected String labelEjeX;
	protected String labelEjeY;
	
	protected List<String> ticksEjeX = new ArrayList<String>();
	protected List<String> ticksEjeY = new ArrayList<String>();
	
	public ChartData() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getLabelEjeX() {
		return labelEjeX;
	}

	public void setLabelEjeX(String labelEjeX) {
		this.labelEjeX = labelEjeX;
	}

	public String getLabelEjeY() {
		return labelEjeY;
	}

	public void setLabelEjeY(String labelEjeY) {
		this.labelEjeY = labelEjeY;
	}

	public List<String> getTicksEjeX() {
		return ticksEjeX;
	}

	public void setTicksEjeX(List<String> ticksEjeX) {
		this.ticksEjeX = ticksEjeX;
	}
	
	public List<String> getTicksEjeY() {
		return ticksEjeY;
	}

	public void setTicksEjeY(List<String> ticksEjeY) {
		this.ticksEjeY = ticksEjeY;
	}
	
	public String getIdTag() {
		return idTag;
	}

	public void setIdTag(String idTag) {
		this.idTag = idTag;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public JSONObject toJson() {
		try {
			JSONObject json = new JSONObject();
			json.put("ticksEjeX", new JSONArray(this.ticksEjeX));
			json.put("ticksEjeY", new JSONArray(this.ticksEjeY));
			json.put("minDomainX", this.ticksEjeX.get(0).toString());
			json.put("maxDomainX", this.ticksEjeX.get(this.ticksEjeX.size()-1).toString());
			json.put("minDomainY", "0");
			json.put("maxDomainY", "100");
			json.put("labelEjeY", this.labelEjeY);
			json.put("labelEjeX", this.labelEjeX);
			json.put("titulo", this.titulo);
			json.put("height", this.height);
			json.put("width", this.width);
			json.put("idTag", this.idTag);
			return json;
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
