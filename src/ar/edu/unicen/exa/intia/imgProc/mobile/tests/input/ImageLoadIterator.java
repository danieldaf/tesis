package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ListIterator;


public interface ImageLoadIterator extends ListIterator<MatImagen> {
	
	public void moveToFirst();
	public int size();

}
