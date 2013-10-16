package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class AlgCombinacion {
	
	private Long id;
	private String nombre;
	private AlgDeteccion detector;
	private AlgExtraccion extractor;
	private AlgMatching matcher;

	public AlgCombinacion(Long id) {
		this.id = id;
	}
			
	public AlgCombinacion(Long id, String nombre, AlgDeteccion detector, AlgExtraccion extractor, AlgMatching matcher) {
		this.id = id;
		this.nombre = nombre;
		this.detector = detector;
		this.extractor = extractor;
		this.matcher = matcher;
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNombre() {
		if (nombre == null || nombre.trim().isEmpty())
			return toString();
		return nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public AlgDeteccion getDetector() {
		return detector;
	}

	public AlgExtraccion getExtractor() {
		return extractor;
	}

	public AlgMatching getMatcher() {
		return matcher;
	}
	
	public String toString() {
		String result = detector.getNombre()+"/"+extractor.getNombre()+"/"+matcher.getNombreCorto();
		return result;
	}
}
