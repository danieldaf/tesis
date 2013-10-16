package ar.edu.unicen.exa.intia.imgProc.mobile.tests.output;

import org.opencv.core.Scalar;

public class FrameMatchingStatistics {

    int totalKeypoints;

    double argumentValue;
    /**
     * Porcentaje calculado entre de los descriptores detectados entre las caracteristicas de la imagen original y las de la imagen
     * transformada coincidentes; sobre la cantidad de puntos de interes detectados en la imagen.
     */
    double percentOfMatches;
    /**
     * ?
     */
    double ratioTestFalseLevel;
    /**
     * Distancia media (promedio) entre las características coincidentes
     */
    double meanDistance;
    /**
     * Desviación entre las características coincidentes
     */
    double stdDevDistance;
    /**
     * Porcentaje coincidencias consideradas correctas.
     * Es decir que la coincidencia encontrada coincide con la coincidencia esperada de acuerdo a la homografía.
     */
    double correctMatchesPercent;
    /**
     * Porcentaje de error entre la homografía calculada y la homografía esperada de la transformación.
     * La homografía calculada es un promedio de las homografías individuales de cada característica coincidente.
     */
    double homographyError;

    /**
     * Tiempo en ms, de la demora producto de las opereraciones de extracción+detección+matching de características
     */
    double consumedTimeMs;
    
    double consumedTimeMsDetector;
    double consumedTimeMsExtractor;
    double consumedTimeMsMatcher;
    
    /**
     * @see EjecutorDePruebas.computeReprojectionError()
     * Los items del vector representan de todos las caracteristicas encontradas:
     * 
     * - la distancia la media entre las matches encontrados y lo esperados
	 * - la desviacion estandar entre las matches encontrados y lo esperados
	 * - la mayor distancia entre las matches encontrados y lo esperados
	 * - la menor distancia entre las matches encontrados y lo esperados

     */
    Scalar reprojectionError;
    boolean isValid;
    
    public double matchingRatio() { 
    	return correctMatchesPercent * percentOfMatches * 100; 
    }
    
    public float patternLocalization() { 
    	return (float)(correctMatchesPercent * percentOfMatches * (1.0 - homographyError)); 
    }

	public int getTotalKeypoints() {
		return totalKeypoints;
	}

	public void setTotalKeypoints(int totalKeypoints) {
		this.totalKeypoints = totalKeypoints;
	}

	public double getArgumentValue() {
		return argumentValue;
	}

	public void setArgumentValue(double argumentValue) {
		this.argumentValue = argumentValue;
	}

	public double getPercentOfMatches() {
		return percentOfMatches;
	}

	public void setPercentOfMatches(double percentOfMatches) {
		this.percentOfMatches = percentOfMatches;
	}

	public double getRatioTestFalseLevel() {
		return ratioTestFalseLevel;
	}

	public void setRatioTestFalseLevel(double ratioTestFalseLevel) {
		this.ratioTestFalseLevel = ratioTestFalseLevel;
	}

	public double getMeanDistance() {
		return meanDistance;
	}

	public void setMeanDistance(double meanDistance) {
		this.meanDistance = meanDistance;
	}

	public double getStdDevDistance() {
		return stdDevDistance;
	}

	public void setStdDevDistance(double stdDevDistance) {
		this.stdDevDistance = stdDevDistance;
	}

	public double getCorrectMatchesPercent() {
		return correctMatchesPercent;
	}

	public void setCorrectMatchesPercent(double correctMatchesPercent) {
		this.correctMatchesPercent = correctMatchesPercent;
	}

	public double getHomographyError() {
		return homographyError;
	}

	public void setHomographyError(double homographyError) {
		this.homographyError = homographyError;
	}

	public double getConsumedTimeMs() {
		return consumedTimeMs;
	}

	public void setConsumedTimeMs(double consumedTimeMs) {
		this.consumedTimeMs = consumedTimeMs;
	}

	public Scalar getReprojectionError() {
		return reprojectionError;
	}

	public void setReprojectionError(Scalar reprojectionError) {
		this.reprojectionError = reprojectionError;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public double getConsumedTimeMsDetector() {
		return consumedTimeMsDetector;
	}

	public void setConsumedTimeMsDetector(double consumedTimeMsDetector) {
		this.consumedTimeMsDetector = consumedTimeMsDetector;
	}

	public double getConsumedTimeMsExtractor() {
		return consumedTimeMsExtractor;
	}

	public void setConsumedTimeMsExtractor(double consumedTimeMsExtractor) {
		this.consumedTimeMsExtractor = consumedTimeMsExtractor;
	}

	public double getConsumedTimeMsMatcher() {
		return consumedTimeMsMatcher;
	}

	public void setConsumedTimeMsMatcher(double consumedTimeMsMatcher) {
		this.consumedTimeMsMatcher = consumedTimeMsMatcher;
	}
    
//	public Float tryGetValue(StatisticElement element) {		
//		Float value = null;
//	    if (!isValid)
//	        return value;
//	    
//	    switch (element) {
//	        case  StatisticsElementPointsCount:
//	            value = (float)totalKeypoints;
//	            break;
//	            
//	        case StatisticsElementPercentOfCorrectMatches:
//	            value = correctMatchesPercent * 100;
//	            break;
//	            
//	        case StatisticsElementPercentOfMatches:
//	            value = percentOfMatches * 100;
//	            break;
//	            
//	        case StatisticsElementMeanDistance:
//	            value = meanDistance;
//	            break;
//	            
//	        case StatisticsElementHomographyError:
//	            value = homographyError;
//	            break;
//	            
//	        case StatisticsElementMatchingRatio:
//	            value = matchingRatio();
//	            break;
//	            
//	        case StatisticsElementPatternLocalization:
//	            value = patternLocalization();
//	            break;
//	    }
//	    return value;
//	}
	
//	public void writeElement(DataOutputStream dataStream, StatisticElement elem) throws IOException {
//	    Float value = tryGetValue(elem);
//	    
//	    if (value != null) {
//	    	dataStream.writeBytes(""+value.toString()+"\t");
//	    } else {
//	    	dataStream.writeBytes("null\t");
//	    }
//	}

}
