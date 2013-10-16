package ar.edu.unicen.exa.intia.imgProc.mobile.model;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgDeteccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgExtraccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgMatching;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ResultadosConfig;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class ModeloDeDatos {

	private List<AlgCombinacion> listAlgoritmos = new ArrayList<AlgCombinacion>();
	private List<AlgTransformacion> listTransformaciones = new ArrayList<AlgTransformacion>();
	private List<ImagenOrigen> listImagenes = new ArrayList<ImagenOrigen>();

	private ResultadosConfig resultadosConfig = new ResultadosConfig();

	//800x640
	//640x480
	private int preferedWidth=800;
	private int preferedHeigth=640;

	public ModeloDeDatos() {
		resultadosConfig.setExportarSqlite(true);
		resultadosConfig.setSaveSourceImages(true);
		resultadosConfig.setSaveSourceKeypoints(false);
		resultadosConfig.setSaveTransformedImages(false);
		resultadosConfig.setSaveTransformedKeypoints(false);
		
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.ORB, AlgExtraccion.ORB,
				AlgMatching.BRUTEFORCE_HAMMING));
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.BRISK, AlgExtraccion.BRISK,
				AlgMatching.BRUTEFORCE_HAMMING));
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.ORB, AlgExtraccion.FREAK,
				AlgMatching.BRUTEFORCE_HAMMING));
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.BRISK, AlgExtraccion.FREAK,
				AlgMatching.BRUTEFORCE_HAMMING));
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.ORB, AlgExtraccion.BRISK,
				AlgMatching.BRUTEFORCE_HAMMING));
		listAlgoritmos.add(new AlgCombinacion(null, null,
				AlgDeteccion.BRISK, AlgExtraccion.ORB,
				AlgMatching.BRUTEFORCE_HAMMING));

		listTransformaciones.add(new AlgTransformacion(null,
				AlgTransformacionEnum.BRIGHTNESS,
				AlgTransformacionEnum.BRIGHTNESS
						.getDefaultJsonCaracteristicas()));
		listTransformaciones.add(new AlgTransformacion(null,
				AlgTransformacionEnum.GAUSSIAN_BLUR,
				AlgTransformacionEnum.GAUSSIAN_BLUR
						.getDefaultJsonCaracteristicas()));
		listTransformaciones.add(new AlgTransformacion(null,
				AlgTransformacionEnum.ROTATION, 
				AlgTransformacionEnum.ROTATION
						.getDefaultJsonCaracteristicas()));
		listTransformaciones.add(new AlgTransformacion(null,
				AlgTransformacionEnum.SCALING, 
				AlgTransformacionEnum.SCALING
						.getDefaultJsonCaracteristicas()));

	}

	public List<AlgCombinacion> getListAlgoritmos() {
		return listAlgoritmos;
	}

	public void setListAlgoritmos(List<AlgCombinacion> listAlgoritmos) {
		this.listAlgoritmos = listAlgoritmos;
	}

	public List<AlgTransformacion> getListTransformaciones() {
		return listTransformaciones;
	}

	public void setListTransformaciones(
			List<AlgTransformacion> listTransformaciones) {
		this.listTransformaciones = listTransformaciones;
	}

	public List<ImagenOrigen> getListImagenes() {
		return this.listImagenes;
	}

	public void setListImagenes(List<ImagenOrigen> listImagenes) {
		this.listImagenes = listImagenes;
	}

	public ResultadosConfig getResultadosConfig() {
		return this.resultadosConfig;
	}

	public void setResultadosConfig(ResultadosConfig resultadosConfig) {
		this.resultadosConfig = resultadosConfig;
	}

	public int getPreferedWidth() {
		return preferedWidth;
	}

	public void setPreferedWidth(int preferedWidth) {
		this.preferedWidth = preferedWidth;
	}

	public int getPreferedHeigth() {
		return preferedHeigth;
	}

	public void setPreferedHeigth(int preferedHeigth) {
		this.preferedHeigth = preferedHeigth;
	}
}
