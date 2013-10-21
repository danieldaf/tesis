/**
 * 
 */
package ar.edu.unicen.exa.intia.imgProc.mobile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;
import ar.edu.unicen.exa.intia.imgProc.mobile.dao.EjecucionDao;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ChartData;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EficienciaChartData;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EjecucionDto;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EstadisticaCoincidencias;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EstadisticaEficiencia;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.SerieData;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.TransformacionChartData;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.AlgTransformacionUtils;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.EjecucionUtils;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.estadisticas)
public class EstadisticasActivity extends Activity {
	
	public static String EXTRA_PATH = "pathEjecucion";
	private static Map<AlgTransformacionEnum, String> CHART_ID_TAGS = new HashMap<AlgTransformacionEnum, String>();
//	private static Map<AlgTransformacionEnum, String> CHART_EFICIANCIA_ID_TAGS = new HashMap<AlgTransformacionEnum, String>();
	private static String URL_FILE = "file:///android_asset/html/estadisticas.html";
	private static String JS_PORTNAME = "backend";
	private static String SVG_DELIMITER = "@";
	
	static {
		CHART_ID_TAGS.put(AlgTransformacionEnum.BRIGHTNESS, "chart_brillo");
		CHART_ID_TAGS.put(AlgTransformacionEnum.GAUSSIAN_BLUR, "chart_desenfoque");
		CHART_ID_TAGS.put(AlgTransformacionEnum.ROTATION, "chart_rotacion");
		CHART_ID_TAGS.put(AlgTransformacionEnum.SCALING, "chart_escala");

//		CHART_EFICIANCIA_ID_TAGS.put(AlgTransformacionEnum.BRIGHTNESS, "chart_eficiancia_brillo");
//		CHART_EFICIANCIA_ID_TAGS.put(AlgTransformacionEnum.GAUSSIAN_BLUR, "chart_eficiancia_desenfoque");
//		CHART_EFICIANCIA_ID_TAGS.put(AlgTransformacionEnum.ROTATION, "chart_eficiancia_rotacion");
//		CHART_EFICIANCIA_ID_TAGS.put(AlgTransformacionEnum.SCALING, "chart_eficiancia_escala");
	}
	
	@ViewById(R.id.web_estadisticas)
	protected WebView webview;
	
	protected String pathEjecucion = null;
	protected EjecucionDto ejecucion  = null;
	protected EjecucionDao dao = null;

	//extras
	private String algMasEficaz = null;
	private double tiempoMasEficaz = Double.MAX_VALUE;
	
	//db cache
	private EficienciaChartData eficienciaChart = null;
	private List<SerieData> seriesEficiencia = null;
//	private List<EficienciaChartData> eficienciaPorTransformacionChart = null;
//	private Map<String, List<SerieData>> seriesEficienciaPorTransformacion = new HashMap<String, List<SerieData>>();
	private Map<String, String> coloresAsignados = new HashMap<String, String>();
	private List<TransformacionChartData> listGraficos = null;
	private Map<String, List<SerieData>> mapSeries = new HashMap<String, List<SerieData>>();

	// html cache
	private String _infoEjecucion = null;
	private String _graficoEficiencia = null;
	private String _seriesEficiencia = null;
	private String _listadoGraficos = null;
	private Map<String, String> _seriesGraficos = new HashMap<String, String>();
	
	//exports
	private Map<String, String> graficosAExportar = new HashMap<String, String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		Intent intent = getIntent();
		pathEjecucion = intent.getStringExtra(EXTRA_PATH);
		if (pathEjecucion != null)
			ejecucion = EjecucionUtils.buildFromPath(pathEjecucion);
		if (ejecucion == null) {
			Toast.makeText(this, R.string.estadistics_activity_launch_failed, Toast.LENGTH_LONG).show();
			finish();
		}
		dao = new EjecucionDao(ejecucion.getPathDBFile());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.estadisticas, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case android.R.id.home:
	        NavUtils.navigateUpFromSameTask(this);
	        return true;
	    case R.id.mnu_export_charts:
	    	exportCharts();
	    	exportCVS();
	    	return true;
	    }
	    return super.onOptionsItemSelected(item);
	}	
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("_infoEjecucion", _infoEjecucion);
		outState.putString("_graficoEficiencia", _graficoEficiencia);
		outState.putString("_seriesEficiencia", _seriesEficiencia);
		outState.putString("_listadoGraficos", _listadoGraficos);
		for (String alg : _seriesGraficos.keySet()) {
			outState.putString("key:"+alg, _seriesGraficos.get(alg));
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		_infoEjecucion = savedInstanceState.getString("_infoEjecucion");
		_graficoEficiencia = savedInstanceState.getString("_graficoEficiencia");
		_seriesEficiencia = savedInstanceState.getString("_seriesEficiencia");
		_listadoGraficos = savedInstanceState.getString("_listadoGraficos");
		for (String keys : savedInstanceState.keySet()) {
			if (keys.startsWith("key:")) {
				_seriesGraficos.put(keys.substring("key:".length()), savedInstanceState.getString(keys));
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (dao != null) {
			dao.release();
			dao = null;
		}
	}
	
	@SuppressLint({ "SetJavaScriptEnabled" })
	@AfterViews
	protected void load() {
		WebSettings webSettings = webview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setDisplayZoomControls(false);
		webview.loadUrl(URL_FILE);
		webview.addJavascriptInterface(this, JS_PORTNAME);
	}
	
	private void buildListGraficos() {
		listGraficos = new ArrayList<TransformacionChartData>();
//		eficienciaPorTransformacionChart = new ArrayList<EficienciaChartData>();
		
		List<AlgTransformacion> lstTx = dao.getTransformaciones();
		if (lstTx != null) { 
			for (AlgTransformacion tx : lstTx) {
				TransformacionChartData chart = new TransformacionChartData(tx);
				chart.setIdTag(CHART_ID_TAGS.get(tx.getTipo()));
				chart.setWidth(600);
				chart.setHeight(400);
				chart.setTitulo(tx.getTipo().getNombre());
				chart.setLabelEjeX(AlgTransformacionUtils.getLabelEjeX(tx));
				chart.setLabelEjeY(AlgTransformacionUtils.getLabelEjeY(tx));
				chart.setTicksEjeX(AlgTransformacionUtils.getTicksEjeX(tx));
				chart.setTicksEjeY(AlgTransformacionUtils.getTicksEjeY(tx));
				listGraficos.add(chart);
				
//				EficienciaChartData chartE = new EficienciaChartData(tx)
			}
		}
	}
	
	private void buildSeriesGrafico(String idTag) {
		TransformacionChartData chartAct = null;
		for (ChartData chart : listGraficos) {
			if (chart.getIdTag().equals(idTag) && chart instanceof TransformacionChartData) {
				chartAct = (TransformacionChartData)chart;
				break;
			}
		}
		if (chartAct != null) {
			List<SerieData> listSeries = new ArrayList<SerieData>();
			List<String> stepsValidos = AlgTransformacionUtils.getTicksEjeX(chartAct.getAlgTransformacion());
			
			Map<String, List<EstadisticaCoincidencias>> listaItemsByAlg = dao.obtenerPorcentajeDeMatchesCorrectos(chartAct.getAlgTransformacion());
			float nroAlg = 0.0f;
			float cantAlg = (float)listaItemsByAlg.keySet().size();
			for (String algoritmo : listaItemsByAlg.keySet()) {
				List<EstadisticaCoincidencias> listaItems = listaItemsByAlg.get(algoritmo);
				List<Double> valores =  new ArrayList<Double>();
				
				//Asignamos color
				if (coloresAsignados.get(algoritmo) == null) {
					float h, s, v;
					h = 359f / cantAlg * nroAlg;
					s = 1.0f;
					v = 0.5f;
					int clr = Color.HSVToColor(0, new float[] {h, s, v});
					String color = Integer.toHexString(clr);
					while (color.length() < 6) 
						color = "0"+color;
					color = "#"+color;
					coloresAsignados.put(algoritmo, color);
					nroAlg += 1.0f;
				}
				
				for (int pos = 0, maxPos = listaItems.size(), maxPosVar = listaItems.size(), posStep = 0; pos<maxPos; pos++) {
					EstadisticaCoincidencias item = listaItems.get(pos);
					String stepAct = stepsValidos.get(posStep);
					
					while (maxPosVar < stepsValidos.size() && !stepAct.equals(item.getPasoTransformacion())) {
						valores.add(0.0);
						maxPosVar++;
						posStep++;
						stepAct = stepsValidos.get(posStep);
					}
					
					valores.add(item.getValor());
				}
								
				SerieData serie = new SerieData(algoritmo, coloresAsignados.get(algoritmo), valores);
//				SerieData serie;
//				String colorPrevio = coloresAsignados.get(algoritmo);
//				if (colorPrevio == null) {
//					serie = new SerieData(algoritmo, valores);
//					colorPrevio = serie.getColor();
//					coloresAsignados.put(algoritmo, colorPrevio);
//				} else {
//					serie = new SerieData(algoritmo, colorPrevio, valores);
//				}
				listSeries.add(serie);
			}
			mapSeries.put(idTag, listSeries);
		}
	}
	
	private void buildGraficoEficiencia() {
		List<EstadisticaEficiencia> estadisticas = dao.obtenerPorcentajeEficienciaPorAlgoritmos();
		
		List<String> itemColors = new ArrayList<String>();
		List<String> itemLabels = new ArrayList<String>();
		itemLabels.add(getText(R.string.chart_eficiencia_leyenda_total).toString());
//		itemLabels.add("Tiempo Det+Ext+Match");
		itemLabels.add(getText(R.string.chart_eficiencia_leyenda_matching).toString());
		itemLabels.add(getText(R.string.chart_eficiencia_leyenda_extraccion).toString());
		itemLabels.add(getText(R.string.chart_eficiencia_leyenda_deteccion).toString());
		
		itemColors.add("#9165b0");
//		itemColors.add("red");
		itemColors.add("#209145");
		itemColors.add("#e8c034");
		itemColors.add("#616bef");
		
		eficienciaChart = new EficienciaChartData();
		eficienciaChart.setIdTag("chart_performance");
		eficienciaChart.setWidth(500);
		eficienciaChart.setHeight(500);
		eficienciaChart.setTitulo(getText(R.string.chart_eficiencia_titulo).toString());
		eficienciaChart.setLabelEjeX(getText(R.string.chart_eficiencia_labelEjeX).toString());
		eficienciaChart.setUnidadMetrica(getText(R.string.chart_eficiencia_unidadMetrica).toString());
		
		algMasEficaz = null;
		tiempoMasEficaz = Double.MAX_VALUE;
		
		double tiempoMax = 1.0;
		
		seriesEficiencia = new ArrayList<SerieData>();
		for (EstadisticaEficiencia itemEstadistica : estadisticas) {
			SerieData serie = null;
			
			if (itemEstadistica.getTiempoTotalPorKeypoints() > tiempoMax)
				tiempoMax = itemEstadistica.getTiempoTotalPorKeypoints();
			
			List<Double> valores = new ArrayList<Double>();
			valores.add(Math.round(itemEstadistica.getTiempoTotalPorKeypoints()*1000.0)/1000.0);
//			valores.add(Math.round(itemEstadistica.getTiempoDetExtPorKeypoints()*1000.0)/1000.0);
			valores.add(Math.round(itemEstadistica.getTiempoMatchingPorKeypoints()*1000.0)/1000.0);
			valores.add(Math.round(itemEstadistica.getTiempoExtraccionPorKeypoints()*1000.0)/1000.0);
			valores.add(Math.round(itemEstadistica.getTiempoDeteccionPorKeypoints()*1000.0)/1000.0);

			serie = new SerieData(itemEstadistica.getAlgoritmo(), valores);
			seriesEficiencia.add(serie);
			
			if (algMasEficaz == null || tiempoMasEficaz > itemEstadistica.getTiempoTotalPorKeypoints()) {
				algMasEficaz = itemEstadistica.getAlgoritmo();
				tiempoMasEficaz = itemEstadistica.getTiempoTotalPorKeypoints();
			}
		}

		List<String> ticksEjeX = new ArrayList<String>();
		for (double tiempoAct=0.0; tiempoAct<=tiempoMax+1; tiempoAct+=0.25) {
			ticksEjeX.add(String.valueOf(tiempoAct));
		}
		eficienciaChart.setTicksEjeX(ticksEjeX);		
		
		tiempoMasEficaz = Math.round(tiempoMasEficaz*1000.0)/1000.0;
		
		eficienciaChart.setItemColors(itemColors);
		eficienciaChart.setItemLabels(itemLabels);
	}
	
	// --- Metodos accedidos desde estadisticas.html
	// --- Todos retornan objetos json o string simples
	
	/**
	 * Retorna el objeto EjecucionDto convertido a  JSON
	 */
	@JavascriptInterface
	public String recuperarInfoEjecucion() {
		if (_infoEjecucion == null)
			_infoEjecucion = ejecucion.toJson().toString();
		return _infoEjecucion;
	}
	
	/**
	 * Retorna un PerformanceChartData convertido a JSON
	 * @return
	 */
	@JavascriptInterface
	public String recuperarDatosEficiencia() {
		if (_graficoEficiencia == null) {
			if (eficienciaChart == null)
				buildGraficoEficiencia();
			_graficoEficiencia = eficienciaChart.toJson().toString();
		}
		return _graficoEficiencia;
	}
	
	@JavascriptInterface
	public String recuperarSeriesEficiencia() {
		if (_seriesEficiencia == null) {
			if (eficienciaChart == null)
				buildGraficoEficiencia();
			
			JSONArray result = new JSONArray();
			if (seriesEficiencia != null && !seriesEficiencia.isEmpty()) {
				for (SerieData serie: seriesEficiencia)
					result.put(serie.toJson());
			}

			_seriesEficiencia = result.toString();
		}
		return _seriesEficiencia;
	}
	
	/**
	 * Retorna un List<TransformacionChartData> convertido a JSON 
	 * con todos los graficos disponibles SIN SUS SERIES
	 */
	@JavascriptInterface
	public String recuperarListadoGraficos() {
		if (_listadoGraficos == null) {
			buildListGraficos();
			
			JSONArray result = new JSONArray();
			if (listGraficos != null && !listGraficos.isEmpty()) {
				for (ChartData grafico : listGraficos)
					result.put(grafico.toJson());
			}
			_listadoGraficos = result.toString();
		}
		return _listadoGraficos;
	}
	
	/**
	 * Retorna un List<SerieData> convertido a JSON 
	 * para el tipo de grafico dado
	 */
	@JavascriptInterface
	public String recuperarSeriesGrafico(String idTag) {
		String result = _seriesGraficos.get(idTag);
		if (result == null) {
			buildSeriesGrafico(idTag);
			
			List<SerieData> series = mapSeries.get(idTag);
			if (series != null && !series.isEmpty()) {
				JSONArray json = new JSONArray();
				for (SerieData serieData : series) {
					json.put(serieData.toJson());
				}
				result = json.toString();
				_seriesGraficos.put(idTag, result);
			}
		}
		return result;
	}
	
	@JavascriptInterface
	public void asentarGraficosAExportar(String strCharts) {
		graficosAExportar.clear();
		if (strCharts != null) {
			String[] strChart = strCharts.replaceAll("\n", "").replaceAll("\t", "").trim().split("\\"+SVG_DELIMITER);
			for (String chart : strChart) {
				chart = chart.trim();				
				int pos = chart.indexOf("<svg id=");
				if (pos != -1) {
					int pos1 = chart.indexOf('"', pos);
					int pos2 = chart.indexOf('"', pos1+1);
					String key = chart.substring(pos1+1, pos2);
					graficosAExportar.put(key, chart);
				}
			}
		}
	}
	
	@JavascriptInterface
	public String getSVGDelimiter() {
		return SVG_DELIMITER;
	}
	
	private void exportCharts() {
		if (graficosAExportar.isEmpty()) {
			Toast.makeText(this, R.string.no_charts_to_export, Toast.LENGTH_SHORT).show();
			return;
		}
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_SHORT).show();
			return;
		}
		Iterator<String> keyCharts = graficosAExportar.keySet().iterator();
		while (keyCharts.hasNext()) {
			String key = keyCharts.next();
			String chart = graficosAExportar.get(key);
			String fileNameSVG = key+".svg";
			try {
				File foutSVG = new File(pathEjecucion+File.separator+fileNameSVG);
				if (!foutSVG.exists()) {
					foutSVG.createNewFile();
				}
				FileOutputStream outSVG = new FileOutputStream(foutSVG);
				outSVG.write(chart.getBytes("UTF-8"));
				outSVG.close();
			} catch (IOException e) {
				e.printStackTrace();
				Toast.makeText(this, "Error al exporar "+fileNameSVG+":"+e.getMessage(), Toast.LENGTH_LONG).show();
			}
		}
		Toast.makeText(this, graficosAExportar.size()+" Gráficos exportados con exito en: "+pathEjecucion, Toast.LENGTH_LONG).show();
	}
	
	private void exportCVS() {
		String state = Environment.getExternalStorageState();
		if (!Environment.MEDIA_MOUNTED.equals(state)) {
			Toast.makeText(this, R.string.no_sdcard, Toast.LENGTH_SHORT).show();
			return;
		}
		
		String csvResult = "";
		
		TransformacionChartData chartAct = null;
		for (ChartData chart : listGraficos) {
			if (chart instanceof TransformacionChartData) {
				chartAct = (TransformacionChartData)chart;
				List<String> stepsValidos = AlgTransformacionUtils.getTicksEjeX(chartAct.getAlgTransformacion());
				
				csvResult += "\n"+chartAct.getAlgTransformacion().getTipo().getNombre();
				csvResult += "\nArgument";
				for (String step : stepsValidos) {
					csvResult += ","+step;
				}
				
				Map<String, List<EstadisticaCoincidencias>> listaItemsByAlg = dao.obtenerPorcentajeDeMatchesCorrectos(chartAct.getAlgTransformacion());
				for (String algoritmo : listaItemsByAlg.keySet()) {
					List<EstadisticaCoincidencias> listaItems = listaItemsByAlg.get(algoritmo);
					
					csvResult += "\n"+algoritmo;
					
					for (int pos = 0, maxPos = listaItems.size(), maxPosVar = listaItems.size(), posStep = 0; pos<maxPos; pos++) {
						EstadisticaCoincidencias item = listaItems.get(pos);
						String stepAct = stepsValidos.get(posStep);
						
						while (maxPosVar < stepsValidos.size() && !stepAct.equals(item.getPasoTransformacion())) {
							csvResult += ",0.0";
							maxPosVar++;
							posStep++;
							stepAct = stepsValidos.get(posStep);
						}
						
						csvResult += ","+Double.toString(item.getValor());
					}
									
				}

			}
		}
		
		if (csvResult == null || csvResult.trim().isEmpty()) {
			Toast.makeText(this, "No hay datos estadisticos para exportar a CSV.", Toast.LENGTH_LONG).show();
			return;
		}
		
		String filePath = pathEjecucion+File.separator+"eficiencia.csv";
		try {
			File foutCSV = new File(filePath);
			if (!foutCSV.exists()) {
				foutCSV.createNewFile();
			}
			FileOutputStream outCSV = new FileOutputStream(foutCSV);
			outCSV.write(csvResult.getBytes("UTF-8"));
			outCSV.close();

			Toast.makeText(this, "Datos estadisticos exportados a CSV en el archivo: "+filePath, Toast.LENGTH_LONG).show();		
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Error al exporar CSV:"+e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
}
