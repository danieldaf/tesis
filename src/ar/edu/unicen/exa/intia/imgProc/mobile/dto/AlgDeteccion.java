package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import org.opencv.features2d.FeatureDetector;

public enum AlgDeteccion {
	
	BRISK(FeatureDetector.BRISK, "BRISK"),
	ORB(FeatureDetector.ORB, "ORB"),
	SURF(FeatureDetector.SURF, "SURF"),
	SIFT(FeatureDetector.SIFT, "SIFT");
	
	private int id;
	private String nombre;

	private AlgDeteccion(int id, String nombre) {
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
	
	public static AlgDeteccion fromId(int id) {
		for (AlgDeteccion alg : values()) {
			if (alg.id == id)
				return alg;
		}
		return null;
	}

	public static AlgDeteccion fromNombre(String nombre) {
		for (AlgDeteccion alg : values()) {
			if (alg.nombre.equals(nombre))
				return alg;
		}
		return null;
	}
}
