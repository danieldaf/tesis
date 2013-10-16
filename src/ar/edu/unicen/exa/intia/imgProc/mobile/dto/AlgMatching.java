package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import org.opencv.features2d.DescriptorMatcher;

public enum AlgMatching {
	
	BRUTEFORCE(DescriptorMatcher.BRUTEFORCE, "BRUTEFORCE", "BF"),
	BRUTEFORCE_HAMMING(DescriptorMatcher.BRUTEFORCE_HAMMING, "BRUTEFORCE_HAMMING", "BFH"),
	FLANNBASED(DescriptorMatcher.FLANNBASED, "FLANNBASED", "FB");
	
	private int id;
	private String nombre;
	private String nombreCorto;

	private AlgMatching(int id, String nombre, String nombreCorto) {
		this.id = id;
		this.nombre = nombre;
		this.nombreCorto = nombreCorto;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getNombreCorto() {
		return nombreCorto;
	}

	@Override
	public String toString() {
		return nombre;
	}

	public static AlgMatching fromId(int id) {
		for (AlgMatching alg : values()) {
			if (alg.id == id)
				return alg;
		}
		return null;
	}

	public static AlgMatching fromNombre(String nombre) {
		for (AlgMatching alg : values()) {
			if (alg.nombre.equals(nombre))
				return alg;
		}
		return null;
	}
}
