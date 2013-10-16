package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class ImagenOrigen {

	private Long id;
	private String nombre;
	private String originalUri;
	private String uri;
	
	public ImagenOrigen(Long id) {
		this.id = id;
	}

	public ImagenOrigen(Long id, String nombre, String originalUri, String uri) {
		this.id = id;
		this.nombre = nombre;
		this.originalUri = originalUri;
		this.uri = uri;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getOriginalUri() {
		return originalUri;
	}
	
	public void setOriginalUri(String originalUri) {
		this.originalUri = originalUri;
	}

	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
}
