package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class EstadisticaCoincidencias {
	
	private long idEstadistica;
	private String algoritmos;
	private String trasnformacion;
	private double countMatches;
	private double percentAcum;
	private double percentCorrectAcum;
	private String pasoTransformacion;
	private double valor;
	
	public long getIdEstadistica() {
		return idEstadistica;
	}
	public void setIdEstadistica(long idEstadistica) {
		this.idEstadistica = idEstadistica;
	}
	public String getAlgoritmos() {
		return algoritmos;
	}
	public void setAlgoritmos(String algoritmos) {
		this.algoritmos = algoritmos;
	}
	public String getTrasnformacion() {
		return trasnformacion;
	}
	public void setTrasnformacion(String trasnformacion) {
		this.trasnformacion = trasnformacion;
	}
	public double getCountMatches() {
		return countMatches;
	}
	public void setCountMatches(double countMatches) {
		this.countMatches = countMatches;
	}
	public double getPercentAcum() {
		return percentAcum;
	}
	public void setPercentAcum(double percentAcum) {
		this.percentAcum = percentAcum;
	}
	public double getPercentCorrectAcum() {
		return percentCorrectAcum;
	}
	public void setPercentCorrectAcum(double percentCorrectAcum) {
		this.percentCorrectAcum = percentCorrectAcum;
	}
	public String getPasoTransformacion() {
		return pasoTransformacion;
	}
	public void setPasoTransformacion(String pasoTransformacion) {
		this.pasoTransformacion = pasoTransformacion;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	
}
