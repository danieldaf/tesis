package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Realiza una desenfoque de la imagen, aplicando un filtro gausiano.
 * 
 * @author Daniel Fuentes
 *
 */
public class ImageGaussianBlurTransform extends ImageTransformation {

    private int m_maxKernelSize;
    private List<Double> m_args;
    
    /**
     * Aplica un filtro de desenfoque, difuminando desde 1 pixel hasta maxKenelSize pixles.
     * 
     * @param maxKernelSize Cantidad maxima de pixeles a agrupar para difuminar.
     */
    public ImageGaussianBlurTransform(int maxKernelSize) {
		super("Gaussian blur");
		this.m_maxKernelSize = maxKernelSize;
		buildConfig();
	}
    public ImageGaussianBlurTransform() {
    	super("Gaussian blur");
    }
    
    protected void buildConfig() {
		m_args = new ArrayList<Double>();
		for (int arg = 1; arg <= m_maxKernelSize; arg++)
	        m_args.add((double)arg);
    }

    /**
     *  Retorna un listado con todos los GRADOS DE DESENFOQUE a aplicar para poder decir que esta transformacion
     *  a sido aplicada en todas variaciones posibles. 
     */
	@Override
	public List<Double> getConfigArguments() {
		return m_args;
	}

	/**
	 * Aplica un desenfoque de 't' nivles sobre la imagen 'source' y deja la imagen final en 'result'
	 * @see super.transform(float, Mat, Mat)
	 */
	@Override
	public void transform(double t, Mat source, Mat result) {
	    int kernelSize = (int)t * 2 + 1;
	    Imgproc.GaussianBlur(source, result, new Size(kernelSize,kernelSize), 0);
	}

	@Override
	public void importConfigFromJson(String jsonObj) {
		try {
			JSONObject json = new JSONObject(jsonObj);
			m_maxKernelSize = json.getInt("maxKernelSize");
			buildConfig();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String exportConfigToJson() {
		String result = null;
		JSONObject json = new JSONObject();
		try {
			json.put("maxKernelSize", m_maxKernelSize);
			result = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
