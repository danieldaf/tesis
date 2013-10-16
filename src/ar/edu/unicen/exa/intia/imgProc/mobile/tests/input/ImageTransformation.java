package ar.edu.unicen.exa.intia.imgProc.mobile.tests.input;

import java.util.ArrayList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.KeyPoint;

/**
 * Esta es una clase abstracta representa una transformacion generica.
 * Se entiende que una misma transformacion puede ser aplicada de diferentes formas de acuerdo a sus valores.
 * 
 * Esta clase deberia de contener todos los valores iniciales representativos de ser aplicados.
 * El criterio de que valores iniciales almacenar dependera de la transformacion en si. Deberia
 * de abarcar con ellas la mayor diversidad de variaciones.
 * Esta clase, deberia de contener toda la informacion.
 *
 */
public abstract class ImageTransformation {
	
    protected String name;

    /**
     * Este metodo contiene todos los valores de configuracion iniciales validos para generar 
     * las transformaciones significativas a aplicar. 
     * El subconjunto limitado de valores, tiene que ser lo suficientemente representativo para
     * poder afirmar que la transformacion ah sido aplicada con todos sus valores posibles.
     * 
     * Por el tipo de transformaciones aplicadas, se almacena como un listado de flotantes.
     * No osbtante deberia ser mas generico, para poder abarcar transformaciones mas complejas
     * que requieran mas de un parametro para sus valores iniciales.
     * 
     * @return
     */
	public abstract List<Double> getConfigArguments();
    
	public abstract void importConfigFromJson(String jsonObj);
	
	public abstract String exportConfigToJson();
    
	/**
	 * Este metodo se encarga de aplicar un paso de la transformacion de acuerdo con los parametros dado.
	 * El parametro 't' deberia ser un valor recuperado del listado obtenido por el metodo getX(). no obstante
	 * puede ser cualquier otro valor valido para el algoritmos de transformacion.
	 * 
	 * @param t parametro de transformacion
	 * @param source imagen origen
	 * @param result imagen transformada
	 */
	public abstract void transform(double t, Mat source, Mat result);

	protected ImageTransformation(String transformationName) {
		this.name = transformationName;
	}

	public boolean canTransformKeypoints() {
		return false;
	}
	
	public void transform(double t, MatOfKeyPoint source, MatOfKeyPoint result) {
		//TODO no hace nada (es asi, falso todo)
	}

	/**
	 * Este metodo permite calcular la matriz de homografia para proyectar un punto de espacio de la imagen original
	 * a su ubicacion final, luego de haber aplicado la transformacion.
	 * DEBE SER sobreescrito por cada transformacion que modifique la posicion de los pixeles.
	 * 
	 * La implementacion abstracta, no realiza ninguna transformacion, por que la implementacion por defecto retorna
	 * la matriz identidad, dejando cada punto en su misma ubicacion.
	 * 
	 * @param t parametro de transformacion @see this.transform(float, Mat, Mat)
	 * @param source Espacio de corrdenadas de referencia. Esta matriz representa el espacio de coordenadas 
	 * antes de aplicar la transformacion.
	 * @return La matriz para proyectar cualquier punto a su posicion final luego de aplicar la transformacion.
	 */
	public Mat getHomography(double t, Mat source) {
		Mat result = Mat.eye(3, 3, CvType.CV_64FC1);
		return result;
	}

	/**
	 * TODO: analizar mas en detalle este metodo. Lo que sigue es una explicacion aproximada de lo que hace.
	 * Pero debe ser reanalizada en profundidad aun.
	 * ----
	 * 
	 * Este metodo calcula la matriz de homografía aproximada en base a conocer la posicion original y donde fue detectado 
	 * una serie de puntos carateristicos. Es decir el cambio de coordenadas que deberia aplicarse para poder transformar 
	 * los puntos caracteristicos originales a sus ubicaciones donde fueron detectados.
	 * Dicha matriz puede no existir o ser ligeramente distinta para todos los puntos detectados.
	 * Con lo cual el resultado es una matriz aproximada, es un promedio de todas las matrices de homografia calculada
	 * para cada punto caracteristico detectado ponderado por la calidad de matching del mismo. 
	 * (VERIFICAR ESTO PERO CREO ENTENDER QUE ASI FUNCIONA)
	 * 
	 * @param source El listado de los puntos caracteristicos detectados en la imagen patron (original)
	 * @param result El listado de los puntos caracteristicos detectados en la imagen transformada
	 * @param input El listado de todos los puntos caracteristicos de 'source' que fueron encontrados en 'result' 
	 * 	(lso matches que el algoritmo dice haber encontrado)
	 * @param inliers Este listado se llena aqui dentro. RETORNA un subconjunto del listado de 'input'. Contiene todos los 
	 * 	puntos que algoritmos matcheo correctamente y a su ves su matriz de homografia es lo sufiente cercana a la 
	 * 	matriz de homografia resultante calculada.
	 * @param homography Esta matriz RETORNA la matriz de homografia calculada en base al promedio ponderado de cada 
	 * 	matriz de homografia particular.
	 * @return True o False si se pudo encontrar una matriz de homografia aproximada para al menos 4 de los puntos que 
	 * 	algoritmo hico matching.
	 */
    public static boolean findHomography(MatOfKeyPoint source, MatOfKeyPoint result, MatOfDMatch  input, MatOfDMatch inliers, Mat homography) {
    	List<DMatch> inputList = input.toList();
    	List<KeyPoint> sourceList = source.toList();
    	List<KeyPoint> resultList = result.toList();
    	
        if (inputList.size() < 4)
            return false;
        
        long pointsCount = inputList.size();
        float reprojectionThreshold = 2;

        //Prepare src and dst points
        List<Point> srcPointsList = new ArrayList<Point>();
        List<Point> dstPointsList = new ArrayList<Point>();
        for (int i = 0; i < pointsCount; i++) {
        	DMatch inputDmatch = inputList.get(i);
            srcPointsList.add(sourceList.get(inputDmatch.trainIdx).pt);
            dstPointsList.add(resultList.get(inputDmatch.queryIdx).pt);
        }
        MatOfPoint2f srcPoints = new MatOfPoint2f(srcPointsList.toArray(new Point[0]));
        MatOfPoint2f dstPoints = new MatOfPoint2f(dstPointsList.toArray(new Point[0]));
          
        // Find homography using RANSAC algorithm
        Mat status = new Mat();
        Calib3d.findHomography(srcPoints, dstPoints, Calib3d.FM_RANSAC, reprojectionThreshold, status).copyTo(homography);
        
        // Warp dstPoints to srcPoints domain using inverted homography transformation
        MatOfPoint2f srcReprojected = new MatOfPoint2f();
        Core.perspectiveTransform(dstPoints, srcReprojected, homography.inv());

        // Pass only matches with low reprojection error (less than reprojectionThreshold value in pixels)
        List<DMatch> inliersList = new ArrayList<DMatch>(); 
        List<Point> srcReprojectedList = srcReprojected.toList();
        for (int i = 0; i < pointsCount; i++) {
            Point actual =  srcPointsList.get(i);
            Point expect = srcReprojectedList.get(i);
            Point v = new Point(actual.x - expect.x, actual.y - expect.y);
            double distanceSquared = v.dot(v);
            
            if (/*status[i] && */distanceSquared <= reprojectionThreshold * reprojectionThreshold) {
            	inliersList.add(inputList.get(i));
            }
        }
        inliers.fromList(inliersList);
        
        // Test for bad case
        if (inliersList.size() < 4)
            return false;
        
        // Now use only good points to find refined homography:
        List<Point> refinedSrcList = new ArrayList<Point>();
        List<Point> refinedDstList = new ArrayList<Point>();
        for (int i = 0; i < inliersList.size(); i++) {
        	DMatch item = inliersList.get(i);
            refinedSrcList.add(sourceList.get(item.trainIdx).pt);
            refinedDstList.add(resultList.get(item.queryIdx).pt);
        }
        MatOfPoint2f refinedSrc = new MatOfPoint2f(refinedSrcList.toArray(new Point[0]));
        MatOfPoint2f refinedDst = new MatOfPoint2f(refinedDstList.toArray(new Point[0]));
        
        // Use least squares method to find precise homography 0 == Calib3d.CV_ITERATIVE
        Mat homography2 = Calib3d.findHomography(refinedSrc, refinedDst, Calib3d.CV_ITERATIVE, reprojectionThreshold);

        // Reproject again:
        Core.perspectiveTransform(dstPoints, srcReprojected, homography2.inv());
        inliersList.clear();
        
        for (int i = 0; i < pointsCount; i++) {
            Point actual = srcPointsList.get(i);
            Point expect = srcReprojectedList.get(i);
            Point v = new Point(actual.x - expect.x, actual.y - expect.y);
            double distanceSquared = v.dot(v);

            if (distanceSquared <= reprojectionThreshold * reprojectionThreshold) {
                inliersList.add(inputList.get(i));
            }
        }
        inliers.fromList(inliersList);
     
        //homography = homography2;
        homography2.copyTo(homography);
        return inliersList.size() >= 4;
    }
    
    public String getName() {
    	return this.name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof ImageTransformation)) {
			return false;
		}
		ImageTransformation other = (ImageTransformation) obj;
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
    
}
