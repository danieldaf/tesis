package ar.edu.unicen.exa.intia.imgProc.mobile.tests;

import java.util.List;

import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ResultadosConfig;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageLoadIterator;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;

/**
 * 
 * Interfaz de servicios que debe utilizar quien quiera comunicarse con el
 * servicio de ejecucion de pruebas.
 * 
 * @author Daniel Fuentes
 * 
 */
public interface ISrvPruebaPerformanceBinder {
	
	//------------------------ PARAMETROS DE CONFIGURACION ------------------------ 
	/**
	 * Listado de transformaciones
	 */
	public void asignarAlgoritmosDeTransformaciones(List<AlgTransformacion> lstTransformaciones);
	
	/**
	 * Listado de tripa de combinaciones de algoritmos Dectetor/Extractor/Matcher
	 */
	public void asignarAlgoritmosDeEvaluacion(List<AlgCombinacion> lstAlgoritmos);
	
	/**
	 * Implementacion de data provider de imagenes
	 */
	public void asignarImagenesDePrueba(ImageLoadIterator imgIterator);
	
	/**
	 * Asignacion del handler para informarnos sobre el avance de las pruebas.
	 */
	public void asignarProgressHandler(ProgressHandler progressHandler);
	
	/**
	 * Parametros de configuracion de los resultados a generar
	 * 
	 * @param resultadosConfig
	 */
	public void configurarResultadosGenerados(ResultadosConfig resultadosConfig);

	//------------------------ METODOS DE PROCESAMIENTO ------------------------
	public void configurarProcesamiento() throws InputValidationException, ExecutionEvaluationException;
	
	public void iniciarProcesamiento() throws InputValidationException, ExecutionEvaluationException;
}