package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * Realiza un ajuste del brillo de la imagen.
 * 
 * @author Daniel Fuentes
 *
 */
public class ImageBrightnessTransform extends ImageTransformation {

    private int m_min;
    private int m_max;
    private int m_step;
    private List<Double> m_args;
    
    /**
     * Ajusta el brillo de una imagen de un minimo a un maximo establecido.
     * 
     * @param min Intensidad minima de brillo a aplicar
     * @param max Intensidad maxima de brillo a aplicar
     * @param step Intensidad a avanzar para ir del brillo minimo al brillo maximo
     */
    public ImageBrightnessTransform(int min, int max, int step) {
		super("Brightness change");
		this.m_min = min;
		this.m_max = max;
		this.m_step = step;
		buildConfig();
	}
    public ImageBrightnessTransform() {
    	super("Brightness change");
    }
    
    protected void buildConfig() {
		m_args = new ArrayList<Double>();
	    for (int arg = m_min; arg <= m_max; arg += m_step)
	        m_args.add((double)arg);
    }

    /**
     *  Retorna un listado con todos los GRADOS DE BRILLO a aplicar para poder decir que esta transformacion
     *  a sido aplicada en todas variaciones posibles. 
     */
	@Override
	public List<Double> getConfigArguments() {
	    return m_args;
	}

	/**
	 * Aplica un ajuste del brillo de 't' nivles sobre la imagen 'source' y deja la imagen final en 'result'
	 * @see super.transform(float, Mat, Mat)
	 */
	@Override
	public void transform(double t, Mat source, Mat result) {
		Scalar scalar = new Scalar(t,t,t,t);
		Core.add(source, scalar, result);
	}

	@Override
	public void importConfigFromJson(String jsonObj) {
		try {
			JSONObject json = new JSONObject(jsonObj);
			m_min = json.getInt("min");
			m_max = json.getInt("max");
			m_step = json.getInt("step");
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
			json.put("min", m_min);
			json.put("max", m_max);
			json.put("step", m_step);
			result = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}

}
