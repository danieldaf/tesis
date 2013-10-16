package ar.edu.unicen.exa.intia.imgProc.mobile.tests;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.KeyPoint;
import org.opencv.imgproc.Imgproc;

import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.FeatureAlgorithm;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageLoadIterator;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.MatImagen;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.BenchResults;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.FrameMatchingStatistics;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.StatisticResults;

/**
 *
 * Clase principal para lanzar la ejecucion de los algoritmos.
 * El modo de uso pensado, es crear un instacia de la misma.
 * Establecer lo siguiente:
 * 
 * Parametros de configuracion de entrada
 * - Listado de combinaciones de algoritmos a emplear detector/extractor/matcher
 * - Listado de algoritmos de transformaciones a utilizar (rotacion, escalado, brillo, etc)
 * - Origen de las imegenes a emplear para las pruebas (recurso local, foto, galeria)
 * 
 * Parametros de configuracion de salida
 * - Listado de estadisticas a relevar (velocidad, robustes, eficiencia, eficacia, error cometido)
 * - Listado datos resultantes retornados (puntos encontrados, coincidencias, homografias, imagenes transformadas)
 * 
 * Lanzar a ejecutar y recuperar los resultados generados.
 * 
 * OBS: tambien podria ser 'parametrizado' por algun tipo de estrategia la forma de guardar los resultados.
 * Cada uno por separadao. Al igual que la recuperacion de las imagenes.
 * Asi como las imagenes son recuperadas en forma trasparente asi sean de la galeria como de un recurso.
 * Las estadisticas e imagenes resultates podrian almacenarse de forma transparente en memoria, en archivos o en base de dato.
 * 
 * @author Daniel Fuentes
 *
 */
public class EjecutorDePruebas {
	
	//in
	protected List<AlgCombinacion> algorithms = new ArrayList<AlgCombinacion>();
	protected List<ImageTransformation> transformations = new ArrayList<ImageTransformation>();
	protected ImageLoadIterator imageIterator = null;
	
	//mid
	protected ProgressHandler progressHandler = null;
	
	//out
	protected BenchResults resultsHandler = null;
	
	private String titleAlgoritmo;
//	private String titleImagen;
	private String titleTransformacion;
	private int progressValue;
	
	private String monitorFlags = "Monitor de Flags";
	
	private boolean flagPausar = false;
	private boolean flagReanudar = false;
	private boolean flagAbortar = false;
	
	private boolean flagIsRunning = false;
	private boolean flagIsPaused = false;
	
	public EjecutorDePruebas(){
	}
	
	public void pausar() {
		synchronized (monitorFlags) {
			if (flagIsRunning && !flagIsPaused) {
				flagPausar = true;
				monitorFlags.notifyAll();
			}
		}
	}
	
	public void reanudar() {
		synchronized (monitorFlags) {
			if (flagIsRunning && flagIsPaused) {
				flagReanudar = true;
				monitorFlags.notifyAll();
			}
		}
	}
	
	public void abortar() {
		synchronized (monitorFlags) {
			if (flagIsRunning) {
				flagAbortar = true;
				monitorFlags.notifyAll();
			}
		}
	}

	private void verificarFlags() throws ExecutionEvaluationException {
		boolean ciclar;
		boolean fuePausada = false;
		synchronized (monitorFlags) {
			do {
				ciclar = false;
				//verificamos si estamos corriendo y debemos pausar
				if (flagIsRunning && !flagIsPaused && flagPausar) {
					flagPausar = false;
					flagIsPaused = true;
				}
				
				//verificamos si estamos pausados y debemos reanudar
				if (flagIsRunning && flagIsPaused && flagReanudar) {
					flagReanudar = false;
					flagIsPaused = false;
				}
				
				//verificamos si estamos corriendo y debemos abortar
				if (flagIsRunning && flagAbortar) {
					flagIsRunning = true;
					progressHandler.canceled();
					progressHandler.updateProgress(progressValue, "Evaluacion abortada.");
					throw new ExecutionEvaluationException(ExecutionExceptionEnum.EVALUACION_ABORTADA);
				}
				
				//verificamos estado de los flags
				if (flagIsRunning && flagIsPaused) {
					try {
						progressHandler.updateProgress(progressValue, "Evaluacion pausada");
						progressHandler.paused();
						ciclar = true;
						fuePausada = true;
						monitorFlags.wait();
					} catch (InterruptedException e) {
					}
				}
			} while (ciclar);
			if (fuePausada) {
				progressHandler.updateProgress(progressValue, "Evaluacion reanudada");
				progressHandler.resumed();
			}
		}
	}
	
	public boolean isRunning() {
		return flagIsRunning;
	}
	
	public boolean isPaused() {
		return flagIsPaused;
	}
	
	public boolean isAborted() {
		return flagAbortar && !flagIsRunning;
	}
	
	public void procesar() throws InputValidationException, ExecutionEvaluationException {
		if (algorithms == null || algorithms.isEmpty() || transformations == null || transformations.isEmpty()
				|| imageIterator == null || !imageIterator.hasNext() || progressHandler == null) {
			throw new InputValidationException(InputExceptionEnum.PARAMETROS_DE_ENTRADA_REQUERIDOS);
		}
		
		synchronized (monitorFlags) {
			flagIsPaused = false;
			flagIsRunning = true;
			
			flagPausar = false;
			flagReanudar = false;
			flagAbortar = false;
		}
		
		progressHandler.started();
		
		if (resultsHandler != null)
			resultsHandler.iniciar();
		
		progressValue = 0;
		
		int txStepsTotales = 0;
		for (ImageTransformation t : transformations) {
			txStepsTotales += t.getConfigArguments().size();
		}
		
        int maxProgressValue = (algorithms.size()*txStepsTotales*imageIterator.size());
        progressHandler.updateRange(progressValue, maxProgressValue, "Iniciando");
        progressHandler.beforeProgress();
        
        /*
         * Iteracion por todas las imagenes.
         * Por cada una de ellas se aplican las transformaciones configuradas. 
         * Utilizando uno a uno los metodos(combinaciones de algoritmos pre configurados).         * 
         * 
         * Se aplica el algoritmos y estima su grado de acierto para cada tripla, mientras se
         * contabiliza estadisticamente las metricas.
         * Todo esto se realiza dentro del metodo 'performEstimation'.
         * 
         */
        try {
	        //Itera en segunda instancia por el conjunto de metodos armados
	        for (int algIndex = 0; algIndex < algorithms.size(); algIndex++) {
	        	verificarFlags();
	        	AlgCombinacion algC = algorithms.get(algIndex);
	        	FeatureAlgorithm alg = ConversorClasesAnalogas.armarAlgoritmoAnalogo(algC);
	        	titleAlgoritmo = alg.getDescription();
	//          FeatureAlgorithm alg = algorithms.get(algIndex);//Selecciona uno de ellos
	            progressHandler.updateProgress(progressValue, "Probando "+alg.getDescription());
	
	            // Comienza la iteracion por las imagenes seleccionadas a evaluar
	            imageIterator.moveToFirst();
	            while (imageIterator.hasNext()) {
	            	verificarFlags();
	            	
	            	MatImagen infoImage = imageIterator.next(); //Obtiene una imagen
	//            	titleImagen = infoImage.getName();
	            	progressHandler.updateProgress(progressValue, "Cargando imagen '"+infoImage.getName()+"'");
	
	            	//Itera finalmente por el conjunto de transformaciones a realizar en la imagen
	                for (int transformIndex = 0; transformIndex < transformations.size(); transformIndex++) {
	                	verificarFlags();
	                	//Se selecciona una de ellas
	                	ImageTransformation trans = transformations.get(transformIndex); //.obj
	                	titleTransformacion = trans.getName();
	                	progressHandler.updateProgress(progressValue, titleAlgoritmo+", "+titleTransformacion+" 0/"+trans.getConfigArguments().size());
	                	/*
	                	 * Mediante la transformacion seleccionada, el algoritmo y la imagen de prueba. Se clona esta ultima para 
	                	 * trabajar sobre ella realizando la transformacion seleccionada y obteniendo las estadisticas recolectadas
	                	 * de acuerdo a las metricas de la aplicacion. 
	                	 */                	
	                	performEstimation(alg, trans, infoImage);
	                	progressHandler.updateProgress(progressValue, trans.getName()+" finalizada");
	                }
	                infoImage.getMatImagen().release();
	            }
	        }
	
        } finally {
			if (resultsHandler != null)
				resultsHandler.finalizar();
	        progressHandler.afterProgress();
	
	        synchronized (monitorFlags) {
				flagIsRunning = false;
			}
	        progressHandler.finished();
        }
	}
	
	/**
	 * Este metodo se encarga de aplicar la transformacion a la imagen, ambas dadas por parametro.
	 * Y luego sumarizar las metricas de los resultados obtenidos. 
	 * 
	 * @param alg
	 * @param transformation
	 * @param sourceImage
	 * @param stat
	 * @return
	 * @throws InputValidationException 
	 */
	protected boolean performEstimation(FeatureAlgorithm alg, ImageTransformation transformation, MatImagen sourceImageOriginal) throws ExecutionEvaluationException {
	    MatOfKeyPoint sourceKpOut = new MatOfKeyPoint();
	    List<KeyPoint> sourceKpList;
	    
	    Mat sourceImage = sourceImageOriginal.getMatImagen().clone();
	    Mat sourceDesc = new Mat();
	    
	    MatImagen sourceImagenOriginalClone = new MatImagen();
	    sourceImagenOriginalClone.setName(sourceImageOriginal.getName());
	    sourceImagenOriginalClone.setMatImagen(sourceImage);
	    sourceImagenOriginalClone.setUri(sourceImageOriginal.getUri());

	    /*
	     * Convertimos la imagen a una escala de grises. JUSTIFICAR POR QUE.
	     */
	    Mat gray = new Mat();
	    if (sourceImage.channels() == 3)
	    	Imgproc.cvtColor(sourceImage, gray, Imgproc.COLOR_BGR2GRAY);
	    else if (sourceImage.channels() == 4)
	    	Imgproc.cvtColor(sourceImage, gray, Imgproc.COLOR_BGRA2GRAY);
	    else if(sourceImage.channels() == 1)
	        gray = sourceImage;
	    
	    verificarFlags();
	    
	    //if (!alg.extractFeatures(gray, sourceKpOut, sourceDesc))
	    if (alg.extractFeatures(gray, sourceKpOut, sourceDesc) == null)	
	        return false;
	    
	    List<Double> transformationStep = transformation.getConfigArguments();
	    int count = transformationStep.size();
	    
	    Mat transformedImage = new Mat();
	    MatOfKeyPoint resKpRealOut = new MatOfKeyPoint();
	    List<KeyPoint> resKpRealList = null;
	    Mat resDesc = new Mat();
	    MatOfDMatch matches = new MatOfDMatch();
	    List<DMatch> matchesList;
	    
	    // To convert ticks to milliseconds
//	    SystemClock.elapsedRealtime();
	    double toMsMul = 1000. / Core.getTickFrequency();

	    for (int i = 0; i < count; i++) {
	    	verificarFlags();
	        double arg = transformationStep.get(i);
	        
	        FrameMatchingStatistics frameStaticOut = new FrameMatchingStatistics();
	        transformation.transform(arg, gray, transformedImage);

	        Mat expectedHomography = transformation.getHomography(arg, gray);
	        
	        //cv::imshow("transformedImage",transformedImage);
	        //cv::waitKey(-1);
	        verificarFlags();
	        long start = Core.getTickCount();
	        FeatureAlgorithm.Time timeConsumed= alg.extractFeatures(transformedImage, resKpRealOut, resDesc);
	        
	        // Initialize required fields
	        frameStaticOut.setValid(resKpRealOut.total() > 0);
	        frameStaticOut.setArgumentValue(arg);
	        
	        if (!frameStaticOut.isValid()) {
	        	expectedHomography.release();
	            continue;
	        }
	        
	        long startMatcher = Core.getTickCount();
	        if (alg.isKnMatchSupported()) { //Para estas pruebas ningun algoritmos esta configurado como que permite KnMatche
	            List<MatOfDMatch> knMatches = new ArrayList<MatOfDMatch>();
	            alg.matchFeatures(sourceDesc, resDesc, 2, knMatches);
	            matchesList = matches.toList();
	            ratioTest(knMatches, 0.75f, matchesList);
	            matches.fromList(matchesList);
	            
	            // Compute percent of false matches that were rejected by ratio test
	            frameStaticOut.setRatioTestFalseLevel((double)(knMatches.size() - matches.total()) / (double) knMatches.size());
	        } else {
	            alg.matchFeatures(sourceDesc, resDesc, matches);
	        }
	        long end = Core.getTickCount();
	        verificarFlags();
	        
	        MatOfDMatch correctMatches = new MatOfDMatch();
	        List<DMatch> correctMatchesList;
	        Mat homography = new Mat();
	        boolean homographyFound = ImageTransformation.findHomography(sourceKpOut, resKpRealOut, matches, correctMatches, homography);
	        //boolean homographyFound = ImageTransformation::findHomographySubPix(sourceKp, gray, resKpReal, transformedImage, matches, correctMatches, homography);
	        correctMatchesList = correctMatches.toList();

	        // Some simple stat:
	        frameStaticOut.setValid(homographyFound);
	        frameStaticOut.setTotalKeypoints((int)resKpRealOut.total());
	        frameStaticOut.setConsumedTimeMs((end - start) * toMsMul);
	        frameStaticOut.setConsumedTimeMsDetector(timeConsumed.consumedTimeMsDetector * toMsMul);
	        frameStaticOut.setConsumedTimeMsExtractor(timeConsumed.consumedTimeMsExtractor * toMsMul);
	        frameStaticOut.setConsumedTimeMsMatcher((end - startMatcher) * toMsMul);
	        
	        // Compute overall percent of matched keypoints
	        frameStaticOut.setPercentOfMatches((double) matches.total() / (double)(Math.min(sourceKpOut.total(), resKpRealOut.total())));
	        frameStaticOut.setCorrectMatchesPercent((double) correctMatches.total() / (double)matches.total());
	        
	        // Compute matching statistics
	        verificarFlags();
	        if (homographyFound) {
	        	
	            //float error = cv::norm(cv::Mat::eye(3,3, CV_64FC1) - r, cv::NORM_INF);
	        	Mat r = expectedHomography.mul(homography.inv());
	            Mat tmpMat = Mat.eye(3,3, CvType.CV_64FC1);
	            //Mat tmpMat = Mat.eye(3,3, CvType.CV_64FC1) - r;
	            //tmpMat = tmpMat. - r;
	            //TODO: VERIFICAR LA CORRECTITUD SEMANTICA
	            for (int pos1=0; pos1<3; pos1++) {
	            	for (int pos2=0; pos2<3; pos2++) {
	            		double valor = tmpMat.get(pos1, pos2)[0];
	            		valor = valor - r.get(pos1, pos2)[0];
	            		tmpMat.put(pos1, pos2, valor);
	            	}
	            }
	            double error = Core.norm(tmpMat, Core.NORM_INF);
	            r.release();
	            tmpMat.release();

	            computeMatchesDistanceStatistics(correctMatchesList, frameStaticOut);
	            sourceKpList = sourceKpOut.toList();
	            resKpRealList = resKpRealOut.toList();
	            frameStaticOut.setReprojectionError(computeReprojectionError(sourceKpList, resKpRealList, correctMatchesList, homography));
	            frameStaticOut.setHomographyError(Math.min(error, 1.0));
	            
	            verificarFlags();
	            asentarResultado(alg, transformation, arg, sourceImagenOriginalClone, transformedImage.clone(), frameStaticOut, sourceKpOut, new MatOfKeyPoint(resKpRealOut.clone()));

//	            if (error >= 1) {
//	            	
//	            	System.out.println("H expected: "+ expectedHomography.toString());
//	            	System.out.println(expectedHomography.dump());
//	            	System.out.println("---");
//	            	System.out.println("H actual: "+ homography.toString());
//	            	System.out.println(homography.dump());
//	            	System.out.println("---");
//	            	System.out.println("H error: "+ error);
//	            	System.out.println("R error: "+ frameStaticOut.getReprojectionError().toString());
	            	
//	            	Mat matchesImg = new Mat();
//	            	MatOfByte mask = new MatOfByte();
//	            	Features2d.drawMatches(transformedImage, 
//	            			resKpReal, 
//	            			gray, 
//	            			sourceKp, 
//	            			correctMatches, 
//	            			matchesImg, 
//	            			Scalar.all(-1.0), 
//	            			Scalar.all(-1.0),
//	            			mask, 
//	            			Features2d.NOT_DRAW_SINGLE_POINTS);

//	                cv::imshow("Matches", matchesImg);
//	                cv::waitKey(-1);
//	            }
	        } 
	        
	        progressHandler.updateProgress(++progressValue, titleAlgoritmo+", "+titleTransformacion+" "+(i+1)+"/"+count);
	        
	        //release de matrices
	        expectedHomography.release();
	        correctMatches.release();
	        homography.release();
	    }
	    
	    //release de matrices
	    gray.release();
	    transformedImage.release();
	    resKpRealOut.release();
	    resDesc.release();
	    matches.release();
	    return true;
	}
	
	////////////////////////////////////
	//Estos metodos deben invocar al handler de procesamientos de cosas de salida que tiene un metodo similar
	//pero pasandole como nulos las cosas que no hacen falta guardar
	/**
	 * Metodo nuevo que se acarga de despachar los resultados estadisticos generadiso
	 * @throws InputValidationException 
	 */
	protected void asentarResultado(FeatureAlgorithm algorithm, ImageTransformation transformation, double valueTransformation, MatImagen sourceImage, Mat transformedImageOut, FrameMatchingStatistics frameStaticOut, MatOfKeyPoint sourceKeypoints, MatOfKeyPoint transformesKeypoints) throws ExecutionEvaluationException {
		if (resultsHandler != null) {
			StatisticResults statResult = new StatisticResults();
			statResult.setAlgorithm(algorithm);
			statResult.setTransformation(transformation);
			statResult.setValueTransformation(valueTransformation);
			statResult.setFrameStatics(frameStaticOut);
			statResult.setSourceImage(sourceImage);
			statResult.setTransformedImage(transformedImageOut);
			statResult.setSourceKeypoints(sourceKeypoints);
			statResult.setTransformedKeypoints(transformesKeypoints);
			resultsHandler.addStatsResult(statResult);
		}
	}
 	////////////////////////////////////
	
	protected void ratioTest(List<MatOfDMatch> knMatches, float maxRatio, List<DMatch> goodMatches) {
		goodMatches.clear();
		for (int i=0; i<knMatches.size(); i++) {
			List<DMatch> matches = knMatches.get(i).toList();
			DMatch best = matches.get(0);
			DMatch good = matches.get(1);
			
			//assert(best.distance <= good.distance);
			if (best.distance <= good.distance) {
				float ratio = best.distance / good.distance;
				if (ratio <= maxRatio)
					goodMatches.add(best);
			}
		}
	}
	
	/**
	 * De todos los matches pasados como parametro, calcula la media de la distancia (distancia promedio)
	 * asi como tambien la desviacion estandar de los mismos.
	 * 
	 * Es decir calcula la la media y desviacion estandar de la calidad de los matches pasados como parametro. 
	 * @param matches
	 * @param statistics
	 * @return
	 */
	protected boolean computeMatchesDistanceStatistics(List<DMatch> matches, FrameMatchingStatistics statistics) {
	    if (matches.isEmpty())
	        return false;
	    List<Double> distances = new ArrayList<Double>(matches.size());
	    for (int i=0; i<matches.size(); i++)
//	        distances.set(i, (double)matches.get(i).distance);
	    	distances.add((double)matches.get(i).distance);
	    
	    MatOfDouble src = new MatOfDouble();
	    src.fromList(distances);
	    MatOfDouble mean = new MatOfDouble();
	    MatOfDouble dev = new MatOfDouble();
	    Core.meanStdDev(src, mean, dev);
	    
	    statistics.setMeanDistance(mean.toList().get(0).doubleValue());
	    statistics.setStdDevDistance(dev.toList().get(0).doubleValue());
	    
	    src.release();
	    mean.release();
	    dev.release();
	    
	    return false;
	}
	
	/**
	 * Calcula y retorna metricas de los puntos matcheados.
	 * A grandes rasgos, calcula las diferencias entre el punto proyectado por la homografia de la transformacion, contra
	 * el punto encontrado por el algoritmo.
	 * Para todos los matches calcula su distancias.
	 * Y de todas las ditancias obtiene:estandar
	 * - la media
	 * - la desviacion estandar
	 * - la mayor distancia
	 * - la menor distancia
	 * 
	 * Estos valores son devueltos como un scalar que luego es impreso en las metricas.
	 * 
	 * @param source
	 * @param query
	 * @param matches
	 * @param homography
	 * @return
	 */
	protected Scalar computeReprojectionError(List<KeyPoint> source, List<KeyPoint> query, List<DMatch> matches, Mat homography) {
		if (matches.size() <= 0)
			return null;
		
		int pointsCount = matches.size();
		List<Point> srcPoints = new ArrayList<Point>();
		List<Point> dstPoints = new ArrayList<Point>();
		List<Double> distances = new ArrayList<Double>();
	    for (int i = 0; i < pointsCount; i++) {
	    	DMatch match = matches.get(i);
	        srcPoints.add(source.get(match.trainIdx).pt);
	        dstPoints.add(query.get(match.queryIdx).pt);
	    }
	    
	    MatOfPoint2f srcPointsMat = new MatOfPoint2f();
	    MatOfPoint2f dstPointsMat = new MatOfPoint2f();
	    srcPointsMat.fromList(srcPoints);
	    dstPointsMat.fromList(srcPoints);
	    //FIXME: se esta rompiendo aca!!!
	    ///home/daniel/local/proys/tesis/opencv/opencv/modules/core/src/matmul.cpp:1926: error: (-215) scn + 1 == m.cols && (depth == CV_32F || depth == CV_64F) in function void cv::perspectiveTransform(cv::InputArray, cv::OutputArray, cv::InputArray)
	    Core.perspectiveTransform(dstPointsMat, dstPointsMat, homography.inv());
	    for (int i = 0; i < pointsCount; i++) {
	        Point src = srcPoints.get(i);
	        Point dst = dstPoints.get(i);

	        Point v = new Point(src.x-dst.x, src.y-dst.y);
	        distances.add(Math.sqrt(v.dot(v)));
	    }

	    MatOfDouble mean = new MatOfDouble();
	    MatOfDouble dev = new MatOfDouble();
	    MatOfDouble distancesMat = new MatOfDouble();
	    distancesMat.fromList(distances);
	    Core.meanStdDev(distancesMat, mean, dev);

	    double v0 = mean.toList().get(0);
	    double v1 = dev.toList().get(0);
	    double v2 = distances.get(0);
	    double v3 = distances.get(0);
	    for (Double valor : distances) {
			if (valor > v2)
				v2 = valor;
			if (valor < v3)
				v3 = valor;
		}
	    Scalar result = new Scalar(v0, v1, v2, v3);
	    
	    srcPointsMat.release();
	    dstPointsMat.release();
	    mean.release();
	    dev.release();
	    distancesMat.release();
	    return result;
	}

	public void setAlgorithms(List<AlgCombinacion> algorithms) {
		this.algorithms = algorithms;
	}
	
	public void setTransformations(List<ImageTransformation> transformations) {
		this.transformations = transformations;
	}

	public void setImageIterator(ImageLoadIterator imageIterator) {
		this.imageIterator = imageIterator;
	}

	public void setProgressHandler(ProgressHandler progressHandler) {
		this.progressHandler = progressHandler;
	}

	public BenchResults getResultsHandler() {
		return resultsHandler;
	}

	public void setResultsHandler(BenchResults resultsHandler) {
		this.resultsHandler = resultsHandler;
	}
}
