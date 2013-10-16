package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.List;

import org.opencv.android.Utils;
import org.opencv.core.Mat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.BitmapUtils;

public class FileImageLoadIterator implements ImageLoadIterator {
	
	private List<ImagenOrigen> listaImagenes;
	private int posImgAct;
	
	private int preferedWidth;
	private int preferedHeight;
	
	private MatImagen imgPrev = null;
	
	public FileImageLoadIterator(List<ImagenOrigen> listaImagenes, int preferedWidth, int preferedHeight) {
		this.listaImagenes = listaImagenes;
		this.posImgAct = 0;
		this.preferedWidth = preferedWidth;
		this.preferedHeight = preferedHeight;
	}

	@Override
	public void add(MatImagen object) {
	}

	@Override
	public boolean hasNext() {
		return listaImagenes != null && listaImagenes.size() > posImgAct;
	}

	@Override
	public boolean hasPrevious() {
		return listaImagenes != null && !listaImagenes.isEmpty() && posImgAct > 0 && imgPrev != null;
	}

	@Override
	public MatImagen next() {
		MatImagen result = null;
		ImagenOrigen imgAct = listaImagenes.get(posImgAct);
		
		String filePath = imgAct.getOriginalUri();
		if (!filePath.startsWith("file://"))
			throw new RuntimeException("'"+filePath+"' no es un URI de archivo valido.");
		filePath = filePath.substring("file://".length());
		
		Mat matBmp = new Mat();
		
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, opts);
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = BitmapUtils.calculateInSampleSize(opts, preferedWidth, preferedHeight);
		Bitmap bmp = BitmapFactory.decodeFile(filePath, opts);
		Utils.bitmapToMat(bmp, matBmp);
		
		imgPrev = result;
		result = new MatImagen();
		result.setName(imgAct.getNombre());
		result.setUri(Uri.parse(imgAct.getOriginalUri()));
		result.setMatImagen(matBmp);
		posImgAct++;

		return result;
	}

	@Override
	public int nextIndex() {
		return posImgAct+1;
	}

	@Override
	public MatImagen previous() {
		return imgPrev;
	}

	@Override
	public int previousIndex() {
		return posImgAct-1;
	}

	@Override
	public void remove() {
	}

	@Override
	public void set(MatImagen object) {
	}

	@Override
	public int size() {
		return listaImagenes.size();
	}
	
	@Override
	public void moveToFirst() {
		this.posImgAct = 0;
		imgPrev = null;
	}

}