package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class EstadisticaEficiencia {

	private String algoritmo;
	private double tiempoDeteccionPorKeypoints;
	private double tiempoExtraccionPorKeypoints;
	private double tiempoMatchingPorKeypoints;
	private double tiempoDetExtPorKeypoints;
	private double tiempoTotalPorKeypoints;
	
	public String getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(String algoritmo) {
		this.algoritmo = algoritmo;
	}
	public double getTiempoDeteccionPorKeypoints() {
		return tiempoDeteccionPorKeypoints;
	}
	public void setTiempoDeteccionPorKeypoints(double tiempoDeteccionPorKeypoints) {
		this.tiempoDeteccionPorKeypoints = tiempoDeteccionPorKeypoints;
	}
	public double getTiempoExtraccionPorKeypoints() {
		return tiempoExtraccionPorKeypoints;
	}
	public void setTiempoExtraccionPorKeypoints(double tiempoExtraccionPorKeypoints) {
		this.tiempoExtraccionPorKeypoints = tiempoExtraccionPorKeypoints;
	}
	public double getTiempoMatchingPorKeypoints() {
		return tiempoMatchingPorKeypoints;
	}
	public void setTiempoMatchingPorKeypoints(double tiempoMatchingPorKeypoints) {
		this.tiempoMatchingPorKeypoints = tiempoMatchingPorKeypoints;
	}
	public double getTiempoDetExtPorKeypoints() {
		return tiempoDetExtPorKeypoints;
	}
	public void setTiempoDetExtPorKeypoints(double tiempoDetExtPorKeypoints) {
		this.tiempoDetExtPorKeypoints = tiempoDetExtPorKeypoints;
	}
	public double getTiempoTotalPorKeypoints() {
		return tiempoTotalPorKeypoints;
	}
	public void setTiempoTotalPorKeypoints(double tiempoTotalPorKeypoints) {
		this.tiempoTotalPorKeypoints = tiempoTotalPorKeypoints;
	}
}