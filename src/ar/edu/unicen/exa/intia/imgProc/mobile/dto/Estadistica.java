package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class Estadistica {

	protected Long id;
	protected AlgCombinacion algoritmo;
	protected AlgTransformacion transformacion;
	protected ImagenOrigen imagenOrigen;
	protected ImagenTransformada imagenTransformada;

	protected Integer totalKeypoints;
	protected Double argumentValue;
    /**
     * Porcentaje calculado entre de los descriptores detectados entre las caracteristicas de la imagen original y las de la imagen
     * transformada coincidentes; sobre la cantidad de puntos de interes detectados en la imagen.
     */
	protected Double percentOfMatches;
	protected Double ratioTestFalseLevel;
    /**
     * Distancia media (promedio) entre las características coincidentes
     */
	protected Double meanDistance;
    /**
     * Desviación entre las características coincidentes
     */
	protected Double stdDevDistance;
    /**
     * Porcentaje coincidencias consideradas correctas.
     * Es decir que la coincidencia encontrada coincide con la coincidencia esperada de acuerdo a la homografía.
     */
	protected Double correctMatchesPercent;
    /**
     * Porcentaje de error entre la homografía calculada y la homografía esperada de la transformación.
     * La homografía calculada es un promedio de las homografías individuales de cada característica coincidente.
     */
	protected Double homographyError;
    /**
     * Tiempo en ms, de la demora producto de las opereraciones de extracción+detección+matching de características
     */
	protected Double consumedTimeMs;

    double consumedTimeMsDetector;
    double consumedTimeMsExtractor;
    double consumedTimeMsMatcher;
	
//      @see EjecutorDePruebas.computeReprojectionError()
//      Los items del vector representan de todos las caracteristicas encontradas:
      
    /** 
     * La distancia la media entre las matches encontrados y lo esperados
     */
	protected Double distanciaMediaProyecciones;
	/**
	 * La desviacion estandar entre las matches encontrados y lo esperados
	 */
	protected Double desviacionEstandarProyecciones;
	/** 
	 * La mayor distancia entre las matches encontrados y lo esperados
	 */
	protected Double distanciaMaximaProyecciones;
	/**
	 * La menor distancia entre las matches encontrados y lo esperados
	 */
	protected Double distanciaMinimaProyecciones;

	protected Boolean isValid;
	
	public Estadistica(Long id) {
		this.id = id;
	}
	
	public Estadistica(Long id, AlgCombinacion algoritmo, AlgTransformacion transformacion, ImagenOrigen imagenOrigen, ImagenTransformada imagenTransformada) {
		this.id = id;
		this.algoritmo = algoritmo;
		this.transformacion = transformacion;
		this.imagenOrigen = imagenOrigen;
		this.imagenTransformada = imagenTransformada;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AlgCombinacion getAlgoritmo() {
		return algoritmo;
	}
	public void setAlgoritmo(AlgCombinacion algoritmo) {
		this.algoritmo = algoritmo;
	}
	public AlgTransformacion getTransformacion() {
		return transformacion;
	}
	public void setTransformacion(AlgTransformacion transformacion) {
		this.transformacion = transformacion;
	}
	public ImagenOrigen getImagenOrigen() {
		return imagenOrigen;
	}
	public void setImagenOrigen(ImagenOrigen imagenOrigen) {
		this.imagenOrigen = imagenOrigen;
	}
	public ImagenTransformada getImagenTransformada() {
		return imagenTransformada;
	}
	public void setImagenTransformada(ImagenTransformada imagenTransformada) {
		this.imagenTransformada = imagenTransformada;
	}
	public Integer getTotalKeypoints() {
		return totalKeypoints;
	}
	public void setTotalKeypoints(Integer totalKeypoints) {
		this.totalKeypoints = totalKeypoints;
	}
	public Double getArgumentValue() {
		return argumentValue;
	}
	public void setArgumentValue(Double argumentValue) {
		this.argumentValue = argumentValue;
	}
	public Double getPercentOfMatches() {
		return percentOfMatches;
	}
	public void setPercentOfMatches(Double percentOfMatches) {
		this.percentOfMatches = percentOfMatches;
	}
	public Double getRatioTestFalseLevel() {
		return ratioTestFalseLevel;
	}
	public void setRatioTestFalseLevel(Double ratioTestFalseLevel) {
		this.ratioTestFalseLevel = ratioTestFalseLevel;
	}
	public Double getMeanDistance() {
		return meanDistance;
	}
	public void setMeanDistance(Double meanDistance) {
		this.meanDistance = meanDistance;
	}
	public Double getStdDevDistance() {
		return stdDevDistance;
	}
	public void setStdDevDistance(Double stdDevDistance) {
		this.stdDevDistance = stdDevDistance;
	}
	public Double getCorrectMatchesPercent() {
		return correctMatchesPercent;
	}
	public void setCorrectMatchesPercent(Double correctMatchesPercent) {
		this.correctMatchesPercent = correctMatchesPercent;
	}
	public Double getHomographyError() {
		return homographyError;
	}
	public void setHomographyError(Double homographyError) {
		this.homographyError = homographyError;
	}
	public Double getConsumedTimeMs() {
		return consumedTimeMs;
	}
	public void setConsumedTimeMs(Double consumedTimeMs) {
		this.consumedTimeMs = consumedTimeMs;
	}
	public Double getDistanciaMediaProyecciones() {
		return distanciaMediaProyecciones;
	}
	public void setDistanciaMediaProyecciones(Double distanciaMediaProyecciones) {
		this.distanciaMediaProyecciones = distanciaMediaProyecciones;
	}
	public Double getDesviacionEstandarProyecciones() {
		return desviacionEstandarProyecciones;
	}
	public void setDesviacionEstandarProyecciones(Double desviacionEstandarProyecciones) {
		this.desviacionEstandarProyecciones = desviacionEstandarProyecciones;
	}
	public Double getDistanciaMaximaProyecciones() {
		return distanciaMaximaProyecciones;
	}
	public void setDistanciaMaximaProyecciones(Double distanciaMaximaProyecciones) {
		this.distanciaMaximaProyecciones = distanciaMaximaProyecciones;
	}
	public Double getDistanciaMinimaProyecciones() {
		return distanciaMinimaProyecciones;
	}
	public void setDistanciaMinimaProyecciones(Double distanciaMinimaProyecciones) {
		this.distanciaMinimaProyecciones = distanciaMinimaProyecciones;
	}
	public Boolean getIsValid() {
		return isValid;
	}
	public void setIsValid(Boolean isValid) {
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
}
