package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class ImagenTransformada {

	private Long id;
	private ImagenOrigen imagenOrigen;
	private AlgTransformacion transformacion;
	private String jsonCaracteristicas;
	private String uri;

	public ImagenTransformada(Long id) {
		this.id = id;
	}
	
	public ImagenTransformada(Long id, ImagenOrigen imagenOrigen,
			AlgTransformacion transformacion, String jsonCaracteristicas,
			String uri) {
		this.id = id;
		this.imagenOrigen = imagenOrigen;
		this.transformacion = transformacion;
		this.jsonCaracteristicas = jsonCaracteristicas;
		this.uri = uri;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public ImagenOrigen getImagenOrigen() {
		return imagenOrigen;
	}

	public void setImagenOrigen(ImagenOrigen imagenOrigen) {
		this.imagenOrigen = imagenOrigen;
	}

	public AlgTransformacion getTransformacion() {
		return transformacion;
	}

	public void setTransformacion(AlgTransformacion transformacion) {
		this.transformacion = transformacion;
	}

	public String getJsonCaracteristicas() {
		return jsonCaracteristicas;
	}

	public void setJsonCaracteristicas(String jsonCaracteristicas) {
		this.jsonCaracteristicas = jsonCaracteristicas;
	}
}
