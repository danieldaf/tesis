package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import org.opencv.features2d.DescriptorExtractor;

public enum AlgExtraccion {
	
	BRISK(DescriptorExtractor.BRISK, "BRISK"),
	ORB(DescriptorExtractor.ORB, "ORB"),
	FREAK(DescriptorExtractor.FREAK, "FREAK"),
	SURF(DescriptorExtractor.SURF, "SURF"),
	SIFT(DescriptorExtractor.SIFT, "SIFT");
	
	private int id;
	private String nombre;

	private AlgExtraccion(int id, String nombre) {
		this.id = id;
		this.nombre = nombre;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	@Override
	public String toString() {
		return nombre;
	}

	public static AlgExtraccion fromId(int id) {
		for (AlgExtraccion alg : values()) {
			if (alg.id == id)
				return alg;
		}
		return null;
	}

	public static AlgExtraccion fromNombre(String nombre) {
		for (AlgExtraccion alg : values()) {
			if (alg.nombre.equals(nombre))
				return alg;
		}
		return null;
	}
}
