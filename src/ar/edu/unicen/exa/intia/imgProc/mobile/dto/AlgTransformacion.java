package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class AlgTransformacion {
	
	private Long id;
	private AlgTransformacionEnum tipo;
	private String jsonCaracteristicas;
	
	public AlgTransformacion(Long id) {
		this.id = id;
	}

	public AlgTransformacion(Long id, AlgTransformacionEnum tipo, String jsonCaracteristicas) {
		this.id = id;
		this.tipo = tipo;
		this.jsonCaracteristicas = jsonCaracteristicas;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public AlgTransformacionEnum getTipo() {
		return tipo;
	}
	
	public void setTipo(AlgTransformacionEnum tipo) {
		this.tipo = tipo;
	}
	
	public String getJsonCaracteristicas() {
		return jsonCaracteristicas;
	}
	
	public void setJsonCaracteristicas(String jsonCaracteristicas) {
		this.jsonCaracteristicas = jsonCaracteristicas;
	}
}