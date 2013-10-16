package ar.edu.unicen.exa.intia.imgProc.mobile.tests.output;

import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;

import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.FeatureAlgorithm;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.MatImagen;

/**
 * Esta interfaz encapsula los metodos para almacenar todos los posibles datos resultados generados.
 * Estadisticas, sumarizacion, graficos, imagenes, etc.
 * 
 * @author Daniel Fuentes
 *
 */
public class StatisticResults {
	
	protected FeatureAlgorithm algorithm;
	protected ImageTransformation transformation;
	protected double valueTransformation;
	protected FrameMatchingStatistics frameStatics;
	protected MatImagen sourceImage;
	protected Mat transformedImage;
	protected MatOfKeyPoint sourceKeypoints;
	protected MatOfKeyPoint transformedKeypoints;
	
	public FrameMatchingStatistics getFrameStatics() {
		return frameStatics;
	}
	public void setFrameStatics(FrameMatchingStatistics frameStatics) {
		this.frameStatics = frameStatics;
	}
	public MatImagen getSourceImage() {
		return sourceImage;
	}
	public void setSourceImage(MatImagen sourceImage) {
		this.sourceImage = sourceImage;
	}
	public Mat getTransformedImage() {
		return transformedImage;
	}
	public void setTransformedImage(Mat transformedImage) {
		this.transformedImage = transformedImage;
	}
	public double getValueTransformation() {
		return valueTransformation;
	}
	public void setValueTransformation(double valueTransformation) {
		this.valueTransformation = valueTransformation;
	}
	public MatOfKeyPoint getSourceKeypoints() {
		return sourceKeypoints;
	}
	public void setSourceKeypoints(MatOfKeyPoint sourceKeypoints) {
		this.sourceKeypoints = sourceKeypoints;
	}
	public MatOfKeyPoint getTransformedKeypoints() {
		return transformedKeypoints;
	}
	public void setTransformedKeypoints(MatOfKeyPoint transformedKeypoints) {
		this.transformedKeypoints = transformedKeypoints;
	}
	public FeatureAlgorithm getAlgorithm() {
		return algorithm;
	}
	public void setAlgorithm(FeatureAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	public ImageTransformation getTransformation() {
		return transformation;
	}
	public void setTransformation(ImageTransformation transformation) {
		this.transformation = transformation;
	}
}
