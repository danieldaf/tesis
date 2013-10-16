package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.imgproc.Imgproc;

//TODO: implementar una translacion bidimensiona
public class ImageTranslationTransformation extends ImageTransformation {

    private double m_startAngleInDeg;
    private double m_endAngleInDeg;
    private double m_step;
    
    private Point m_rotationCenterInUnitSpace;
    
    private List<Double> m_args;
    
    public ImageTranslationTransformation(double startAngleInDeg, double endAngleInDeg, double step, Point rotationCenterInUnitSpace) {
		super("Rotation");
		this.m_startAngleInDeg = startAngleInDeg;
		this.m_endAngleInDeg = endAngleInDeg;
		this.m_step = step;
		this.m_rotationCenterInUnitSpace = rotationCenterInUnitSpace;
		buildConfig();
    }
    
    protected void buildConfig() {
		// Fill the arguments
		m_args = new ArrayList<Double>();
		for (double arg = m_startAngleInDeg; arg <= m_endAngleInDeg; arg += m_step)
			m_args.add(arg);
	}

    /**
     *  Retorna un listado con todos los ANGULOS a aplicar para poder decir que esta transformacion
     *  a sido aplicada en todas variaciones posibles. 
     */
	@Override
	public List<Double> getConfigArguments() {
		return m_args;
	}

	/**
	 * Aplica una rotacion de 't' grados sobre la imagen 'source' y deja la imagen final en 'result'
	 * @see super.transform(float, Mat, Mat)
	 */
	@Override
	public void transform(double t, Mat source, Mat result) {
	    Point center = new Point(source.cols() * m_rotationCenterInUnitSpace.x, source.rows() * m_rotationCenterInUnitSpace.y);
	    Mat rotationMat = Imgproc.getRotationMatrix2D(center, t, 1);
	    Imgproc.warpAffine(source, result, rotationMat, source.size(), Imgproc.INTER_CUBIC);
	}
	
	/**
	 * Retorna una matriz que permite rotar en 't' grados el espacio de coordenadas 'source'
	 */
	@Override
	public Mat getHomography(double t, Mat source) {
	    Point center = new Point(source.cols() * m_rotationCenterInUnitSpace.x, source.rows() * m_rotationCenterInUnitSpace.y);
	    Mat rotationMat = Imgproc.getRotationMatrix2D(center, t, 1);
	    
	    Mat h = Mat.eye(3,3, CvType.CV_64FC1);
	    rotationMat.copyTo(new Mat(h, new Range(0,2), new Range(0,3)));
	    return h;
	}

	@Override
	public void importConfigFromJson(String jsonObj) {
		try {
			JSONObject json = new JSONObject(jsonObj);
			m_startAngleInDeg = json.getDouble("startAngleInDeg");
			m_endAngleInDeg = json.getDouble("endAngleInDeg");
			m_step = json.getDouble("step");
			double x = json.getDouble("rotationCenterInUnitSpaceX");
			double y = json.getDouble("rotationCenterInUnitSpaceY");
			m_rotationCenterInUnitSpace = new Point(x, y);
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
			json.put("startAngleInDeg", m_startAngleInDeg);
			json.put("endAngleInDeg", m_endAngleInDeg);
			json.put("step", m_step);
			json.put("rotationCenterInUnitSpaceX", m_rotationCenterInUnitSpace.x);
			json.put("rotationCenterInUnitSpaceY", m_rotationCenterInUnitSpace.y);
			result = json.toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return result;
	}
}

