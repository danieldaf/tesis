package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import org.json.JSONException;
import org.json.JSONObject;

public class EjecucionDto {

	private String pathDirectory;
	private String pathDBFile;
	private String deviceInfo;
	private String algoritmos;
	private String fechaInicio;
	private String fechaFin;
	private long duracionMS;
	private int cantImagenes;
	private int cantTransformaciones;
	private int cantAlgoritmos;
	private String imagePath;

	public String getPathDirectory() {
		return pathDirectory;
	}

	public void setPathDirectory(String pathDirectory) {
		this.pathDirectory = pathDirectory;
	}

	public String getPathDBFile() {
		return pathDBFile;
	}

	public void setPathDBFile(String pathDBFile) {
		this.pathDBFile = pathDBFile;
	}

	public String getAlgoritmos() {
		return algoritmos;
	}

	public void setAlgoritmos(String algoritmos) {
		this.algoritmos = algoritmos;
	}

	public int getCantImagenes() {
		return cantImagenes;
	}

	public void setCantImagenes(int cantImagenes) {
		this.cantImagenes = cantImagenes;
	}

	public int getCantTransformaciones() {
		return cantTransformaciones;
	}

	public void setCantTransformaciones(int cantTransformaciones) {
		this.cantTransformaciones = cantTransformaciones;
	}

	public String getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}

	public long getDuracionMS() {
		return duracionMS;
	}

	public void setDuracionMS(long duracionMS) {
		this.duracionMS = duracionMS;
	}

	public int getCantAlgoritmos() {
		return cantAlgoritmos;
	}

	public void setCantAlgoritmos(int cantAlgoritmos) {
		this.cantAlgoritmos = cantAlgoritmos;
	}

	public String getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public JSONObject toJson() {
		try {
			JSONObject json = new JSONObject();
			json.put("pathDirectory", this.pathDirectory);
			json.put("pathDBFile", this.pathDBFile);
			json.put("deviceInfo", this.deviceInfo);
			json.put("algoritmos", this.algoritmos);
			json.put("fechaInicio", this.fechaInicio);
			json.put("fechaFin", this.fechaFin);
			String strTime = "";
			double segundos = (double)this.duracionMS / 1000.0;
			double minutos = segundos / 60.0; 
			double horas = minutos / 60.0;
			int intHoras = (int) Math.floor(horas);
			minutos = (horas - (double) intHoras) * 60.0;
			int intMinutos = (int) Math.floor(minutos);
			segundos = (minutos - (double) intMinutos) * 60.0;
			int intSegundos = (int) Math.floor(segundos);
			int milisegundos = (int) Math
					.round((segundos - (double) intSegundos) * 1000.0);
			if (intHoras > 0)
				strTime += intHoras + "h ";
			if (intMinutos > 0)
				strTime += intMinutos + "' ";
			if (intSegundos > 0)
				strTime += intSegundos + "\" ";
			strTime += milisegundos + " ms";
			String strDuracion = "" + this.duracionMS + " [ms] -> " + strTime;
			json.put("duracionMS", strDuracion);
			json.put("cantImagenes", this.cantImagenes);
			json.put("cantTransformaciones", this.cantTransformaciones);
			json.put("cantAlgoritmos", this.cantAlgoritmos);
			return json;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
}
