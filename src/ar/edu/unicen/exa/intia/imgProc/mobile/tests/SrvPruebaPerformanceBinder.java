package ar.edu.unicen.exa.intia.imgProc.mobile.tests;

import java.util.ArrayList;
import java.util.List;

import android.os.Binder;
import ar.edu.unicen.exa.intia.imgProc.mobile.SrvPruebaPerformance;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ResultadosConfig;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageLoadIterator;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;

/**
 * 
 * Esta clase es la interfaz de comunicacion que debera emplear quienes quieran
 * utilizar le servicio de ejecucion de pruebas.
 * Esta clase mantiene la session por cliente que se comunica con el servicio.
 * 
 * @author Daniel Fuentes
 * 
 */
public class SrvPruebaPerformanceBinder extends Binder implements ISrvPruebaPerformanceBinder {
	
	protected SrvPruebaPerformance service = null;
	
	protected ProgressHandler progressHandler = null;
	protected ResultadosConfig resultadosConfig = new ResultadosConfig();
	protected List<AlgTransformacion> lstTransformaciones = new ArrayList<AlgTransformacion>();
	protected List<AlgCombinacion> lstAlgoritmos = new ArrayList<AlgCombinacion>();
	protected ImageLoadIterator imgIterator = null;
	
	public SrvPruebaPerformanceBinder(SrvPruebaPerformance service) {
		this.service = service;
	}

	//------------------------ PARAMETROS DE CONFIGURACION ------------------------
	@Override
	public void asignarAlgoritmosDeTransformaciones(
			List<AlgTransformacion> lstTransformaciones) {
		this.lstTransformaciones = lstTransformaciones;
		service.forzarReconfiguracion();
	}

	@Override
	public void asignarAlgoritmosDeEvaluacion(List<AlgCombinacion> lstAlgoritmos) {
		this.lstAlgoritmos = lstAlgoritmos;
		service.forzarReconfiguracion();
	}

	@Override
	public void asignarImagenesDePrueba(ImageLoadIterator imgIterator) {
		this.imgIterator = imgIterator;
		service.forzarReconfiguracion();
	}

	@Override
	public void asignarProgressHandler(ProgressHandler progressHandler) {
		this.progressHandler = progressHandler;
		service.forzarReconfiguracion();
	}

	@Override
	public void configurarResultadosGenerados(ResultadosConfig resultadosConfig) {
		this.resultadosConfig = resultadosConfig;
		service.forzarReconfiguracion();
	}

	//------------------------ METODOS DE PROCESAMIENTO ------------------------
	@Override
	public void configurarProcesamiento() throws InputValidationException, ExecutionEvaluationException {
		service.configurarProcesamiento(this);
	}
	
	@Override
	public void iniciarProcesamiento() throws InputValidationException, ExecutionEvaluationException {
		service.iniciarProcesamiento(this);
	}

	//-- Getters & Setters
	public ProgressHandler getProgressHandler() {
		return progressHandler;
	}

	public ResultadosConfig getResultadosConfig() {
		return resultadosConfig;
	}

	public List<AlgTransformacion> getLstTransformaciones() {
		return lstTransformaciones;
	}

	public List<AlgCombinacion> getLstAlgoritmos() {
		return lstAlgoritmos;
	}

	public ImageLoadIterator getImgIterator() {
		return imgIterator;
	}
	
}
