package ar.edu.unicen.exa.intia.imgProc.mobile.tests;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.opencv.core.MatOfKeyPoint;

import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgDeteccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgExtraccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgMatching;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.Estadistica;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.KeyPoint;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.CombinedTransform;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.FeatureAlgorithm;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageBrightnessTransform;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageGaussianBlurTransform;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageRotationTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageScalingTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTranslationTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.FrameMatchingStatistics;

public class ConversorClasesAnalogas {

	protected static Map<AlgTransformacionEnum, Class<?>> classTransformationType = new HashMap<AlgTransformacionEnum, Class<?>>();
	protected static Map<Class<?>, AlgTransformacionEnum> typeTransformationClass = new HashMap<Class<?>, AlgTransformacionEnum>();
	
	static {
		classTransformationType.put(AlgTransformacionEnum.COMBINACION, CombinedTransform.class);
		classTransformationType.put(AlgTransformacionEnum.BRIGHTNESS, ImageBrightnessTransform.class);
		classTransformationType.put(AlgTransformacionEnum.GAUSSIAN_BLUR, ImageGaussianBlurTransform.class);
		classTransformationType.put(AlgTransformacionEnum.ROTATION, ImageRotationTransformation.class);
		classTransformationType.put(AlgTransformacionEnum.SCALING, ImageScalingTransformation.class);
		classTransformationType.put(AlgTransformacionEnum.TRANSLATION, ImageTranslationTransformation.class);
		
		Iterator<AlgTransformacionEnum> it = classTransformationType.keySet().iterator();
		while (it.hasNext()) {
			AlgTransformacionEnum tipo = it.next();
			Class<?> clazz = classTransformationType.get(tipo);
			typeTransformationClass.put(clazz, tipo);
		}
	}
	
	public static ImageTransformation armarAlgTransformacionAnalogo(AlgTransformacion txIn) throws InputValidationException{
		ImageTransformation txOut = null;
		if (txIn.getTipo() == null)
			throw new InputValidationException(InputExceptionEnum.ALGORITMO_TRANSFORMACION_INCOMPLETO);
		Class<?> clazz = classTransformationType.get(txIn.getTipo());
		if (clazz == null)
			throw new InputValidationException(InputExceptionEnum.ALGORITMO_TRANSFORMACION_DESCONOCIDO);
		try {
			txOut = ImageTransformation.class.cast(clazz.newInstance());
			txOut.setName(txIn.getTipo().getNombre());
			txOut.importConfigFromJson(txIn.getJsonCaracteristicas());
		} catch (InstantiationException e) {
			e.printStackTrace();
			throw new InputValidationException(InputExceptionEnum.ALGORITMO_TRANSFORMACION_INACCESIBLE, e.getMessage());
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new InputValidationException(InputExceptionEnum.ALGORITMO_TRANSFORMACION_CONSTRUCTOR_NO_VISIBLE, e.getMessage());
		}
		return txOut;
	}
	
	public static AlgTransformacion armarAlgTransformacionAnalogo(ImageTransformation txIn) throws ExecutionEvaluationException {
		AlgTransformacion txOut = null;
		AlgTransformacionEnum tipo = typeTransformationClass.get(txIn.getClass());
		if (tipo == null) 
			throw new ExecutionEvaluationException(ExecutionExceptionEnum.ALGORITMO_TRANSFORMACION_NO_MAPEADO);
		String jsonCaracteristicas = txIn.exportConfigToJson();
		txOut = new AlgTransformacion(null, tipo, jsonCaracteristicas);
		return txOut;
	}
	
	public static FeatureAlgorithm armarAlgoritmoAnalogo(AlgCombinacion algIn) throws InputValidationException {
		FeatureAlgorithm algOut = null;
		if (algIn.getDetector() == null || algIn.getExtractor() == null || algIn.getMatcher() == null)
			throw new InputValidationException(InputExceptionEnum.ALGORITMO_EVALUACION_INCOMPLETO);
		algOut = new FeatureAlgorithm(algIn.getNombre(), 
					algIn.getDetector().getId(), algIn.getExtractor().getId(), algIn.getMatcher().getId());
		return algOut;
	}
	
	public static AlgCombinacion armarAlgoritmoAnalogo(FeatureAlgorithm algIn) throws ExecutionEvaluationException {
		AlgCombinacion algOut = null;
		AlgDeteccion algDeteccion = AlgDeteccion.fromId(algIn.getIdDetector());
		AlgExtraccion algExtraccion = AlgExtraccion.fromId(algIn.getIdExtractor());
		AlgMatching algMatching = AlgMatching.fromId(algIn.getIdMatcher());
		if (algDeteccion == null || algExtraccion == null || algMatching == null)
			throw new ExecutionEvaluationException(ExecutionExceptionEnum.ALGORITMO_EVALUACION_NO_MAPEADO);
		algOut = new AlgCombinacion(null, algIn.getDescription(), algDeteccion, algExtraccion, algMatching);
		return algOut;
	}
	
	public static Estadistica armarEstadisticaAnaloga(FrameMatchingStatistics statIn) {
		Estadistica statOut = null;
		statOut = new Estadistica(null);
		statOut.setTotalKeypoints(statIn.getTotalKeypoints());
		statOut.setArgumentValue(statIn.getArgumentValue());
		statOut.setPercentOfMatches(statIn.getPercentOfMatches());
		statOut.setRatioTestFalseLevel(statIn.getRatioTestFalseLevel());
		statOut.setMeanDistance(statIn.getMeanDistance());
		statOut.setStdDevDistance(statIn.getStdDevDistance());
		statOut.setCorrectMatchesPercent(statIn.getCorrectMatchesPercent());
		statOut.setHomographyError(statIn.getHomographyError());
		statOut.setConsumedTimeMs(statIn.getConsumedTimeMs());
		statOut.setConsumedTimeMsDetector(statIn.getConsumedTimeMsDetector());
		statOut.setConsumedTimeMsExtractor(statIn.getConsumedTimeMsExtractor());
		statOut.setConsumedTimeMsMatcher(statIn.getConsumedTimeMsMatcher());
		double[] reprojection = statIn.getReprojectionError().val;
		statOut.setDistanciaMediaProyecciones(reprojection[0]);
		statOut.setDesviacionEstandarProyecciones(reprojection[1]);
		statOut.setDistanciaMaximaProyecciones(reprojection[2]);
		statOut.setDistanciaMinimaProyecciones(reprojection[3]);
		statOut.setIsValid(statIn.isValid());
		return statOut;
	}
	
	public static KeyPoint armarKeyPointAnalogo(org.opencv.features2d.KeyPoint keyPointIn) {
		KeyPoint keyPointOut = new KeyPoint(null, null, keyPointIn.pt.x, keyPointIn.pt.y, 
					(double)keyPointIn.size, (double)keyPointIn.angle, 
					(double)keyPointIn.response, keyPointIn.octave, keyPointIn.class_id);
		return keyPointOut;
	}
	
	public static List<KeyPoint> armarKeyPointAnalogo(MatOfKeyPoint keyPointsIn) {
		List<KeyPoint> keyPointsOut = new ArrayList<KeyPoint>();
		Iterator<org.opencv.features2d.KeyPoint> itKeyPointsIn = keyPointsIn.toList().iterator();
		while (itKeyPointsIn.hasNext()) {
			org.opencv.features2d.KeyPoint keyPointIn = itKeyPointsIn.next();
			KeyPoint keyPointOut = armarKeyPointAnalogo(keyPointIn);
			keyPointsOut.add(keyPointOut);
		}
		return keyPointsOut;
	}
}
