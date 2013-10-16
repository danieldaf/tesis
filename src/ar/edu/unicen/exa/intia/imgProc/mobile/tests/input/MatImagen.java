package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import org.opencv.core.Mat;

import android.net.Uri;

public class MatImagen {
	
	protected String name;
	protected Mat matImagen;
	protected Uri uri;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Mat getMatImagen() {
		return matImagen;
	}
	public void setMatImagen(Mat matImagen) {
		this.matImagen = matImagen;
	} 
	public Uri getUri() {
		return uri;
	}
	public void setUri(Uri uri) {
		this.uri = uri;
	}

}
