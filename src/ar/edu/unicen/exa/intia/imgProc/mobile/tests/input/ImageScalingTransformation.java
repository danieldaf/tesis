package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

/**
 * Aplica una cambio de escala en las imagenes.
 * @author Daniel Fuentes
 *
 */
public class ImageScalingTransformation extends ImageTransformation {
    
	private double m_minScale;
    private double m_maxScale;
    private double m_step;
    
    private List<Double> m_args;
    
    /**
     * Realiza un ajuste de escala de la imagen, desde un minimo a un maximo 
     * 
     * @param minScale Porcentaje minimo de escalado 
     * @param maxScale Porcentaje maximo de escalado
     * @param step Cuanto debe aumentar en un paso de escalado para llegar del minimo al maximo valor
     */
	public ImageScalingTransformation(double minScale, double maxScale, double step) {
		super("Scaling");
		this.m_minScale = minScale;
		this.m_maxScale = maxScale;
		this.m_step = step;
		buildConfig();
	}
	public ImageScalingTransformation() {
		super("Scaling");
	}
	
	protected void buildConfig() {
	    // Fill the arguments
		m_args = new ArrayList<Double>();
	    for (double arg = m_minScale; arg <= m_maxScale; arg += m_step)
	        m_args.add(arg);
	}

    /**
     *  Retorna un listado con todos los PORCENTAJE DE ESCALA a aplicar para poder decir que esta transformacion
     *  a sido aplicada en todas variaciones posibles. 
     */
	@Override
	public List<Double> getConfigArguments() {
		return m_args;
	}

	/**
	 * Aplica un escalado de 't' porciento sobre la imagen 'source' y deja la imagen final en 'result'
	 * @see super.transform(float, Mat, Mat)
	 */
	@Override
	public void transform(double t, Mat source, Mat result) {
	    Size dstSize = new Size((int)(source.cols() * t + 0.5f), (int)(source.rows() * t + 0.5f));
	    Imgproc.resize(source, result, dstSize, 0f, 0f, Imgproc.INTER_AREA);
	}

	/**
	 * Retorna una matriz que permite escalar en 't' unidades el espacio de coordenadas 'source'
	 */
	@Override
	public Mat getHomography(double t, Mat source) {
	    Mat h = Mat.eye(3,3, CvType.CV_64FC1);
	    h.put(0, 0, t);
	    h.put(1, 1, t);
	    return h;
	}
	
	@Override
	public void importConfigFromJson(String jsonObj) {
		try {
			JSONObject json = new JSONObject(jsonObj);
			m_minScale = json.getDouble("minScale");
			m_maxScale = json.getDouble("maxScale");
			m_step = json.getDouble("step");
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
			json.put("minScale", m_minScale);
			json.put("maxScale", m_maxScale);
			json.put("step", m_step);
			result = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}


}
