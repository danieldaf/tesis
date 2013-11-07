package ar.edu.unicen.exa.intia.imgProc.mobile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.ViewFlipper;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ResultadosConfig;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ProgressChannel;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.SrvPruebaPerformanceBinder;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.FileImageLoadIterator;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.ListAlgoritmos;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.ListSourceImagenes;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.ListTransformaciones;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;
import com.googlecode.androidannotations.annotations.ViewById;

@EActivity(R.layout.pruebas_phone)
public class PruebasPhoneActivity extends Activity implements TabListener { 

	public static final String TAG = PruebasPhoneActivity.class.getName();
	private static final String TAB_SELECTED = "tabIndexSelected";
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat fmtDatePatternPath = new SimpleDateFormat("yyyyMMdd-HHmmss");

	@ViewById(R.id.view_tabs)
	protected ViewFlipper viewTabs;
	
	@FragmentById(R.id.list_algoritmos_fragment)
	protected ListAlgoritmos listAlgoritmosFragment;
	@FragmentById(R.id.list_transformaciones_fragment)
	protected ListTransformaciones listTransformacionesFragment;
	@FragmentById(R.id.list_source_imagenes_fragment)
	protected ListSourceImagenes listSourceImagenesFragment;
	
	@Bean
	protected ModeloDeDatos modeloDatos;
	@Bean
	protected ProgressChannel progressChannel;

	protected Tab tabListAlgoritmos;
	protected Tab tabListTransformaciones;
	protected Tab tabListSourceImagenes;
	
	protected boolean listAlgoritmosFragmentFocusOn = false;
	protected boolean listTransformacionesFragmentFocusOn = false;
	protected boolean listSourceImagenesFragmentFocusOn = false;

	private MyServiceConnection serviceConnection;
	private SrvPruebaPerformanceBinder srvPruebaPerformance;
	private Intent serviceIntent;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS: {
				Log.i(TAG, "OpenCV loaded successfully");
			}
				break;
			default: {
				super.onManagerConnected(status);
			}
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		serviceIntent = new Intent(this, SrvPruebaPerformance_.class);
		startService(serviceIntent);
//		Linqueo dinamico
//		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}
	
	@Override
	protected void onDestroy() {
		if (serviceConnection != null && srvPruebaPerformance != null)
			unbindService(serviceConnection);
		super.onDestroy();
	}
	
	@AfterViews
	public void crearUI() {
		ActionBar bar = getActionBar();
		bar.setDisplayHomeAsUpEnabled(true);
		bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		tabListAlgoritmos = bar.newTab();
		tabListAlgoritmos.setText(R.string.show_alg_combinaciones);
		tabListAlgoritmos.setTabListener(this);
		bar.addTab(tabListAlgoritmos);

		tabListTransformaciones = bar.newTab();
		tabListTransformaciones.setText(R.string.show_alg_transformaciones);
		tabListTransformaciones.setTabListener(this);
		bar.addTab(tabListTransformaciones);

		tabListSourceImagenes = bar.newTab();
		tabListSourceImagenes.setText(R.string.show_src_imagenes);
		tabListSourceImagenes.setTabListener(this);
		bar.addTab(tabListSourceImagenes);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.pruebas_phone, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		MenuItem mnuAddAlgorithmItem = menu.findItem(R.id.mnu_list_add_algorithm_item);
		MenuItem mnuAddTransformationItem = menu.findItem(R.id.mnu_list_add_transformation_item);
		MenuItem mnuAddImageItem = menu.findItem(R.id.mnu_list_add_image_item);
		mnuAddAlgorithmItem.setVisible(listAlgoritmosFragmentFocusOn);
		mnuAddTransformationItem.setVisible(listTransformacionesFragmentFocusOn);
		mnuAddImageItem.setVisible(listSourceImagenesFragmentFocusOn);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.mnu_iniciar_prueba:
			inicioDePruebas();
			return true;
		case R.id.mnu_ver_ejecuciones:
			verEjecuciones();
			return true;
		case R.id.mnu_list_add_algorithm_item:
			listAlgoritmosFragment.invokeAlgoritmoAddAction();
			return true;
		case R.id.mnu_list_add_transformation_item:
			listTransformacionesFragment.invokeTransformacionAction();
			return true;
		case R.id.mnu_list_add_image_item:
			listSourceImagenesFragment.invokeSourceImageAction();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(TAB_SELECTED, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		int tabSelected = savedInstanceState.getInt(TAB_SELECTED);
		getActionBar().setSelectedNavigationItem(tabSelected);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (viewTabs != null) {
			listAlgoritmosFragmentFocusOn = false;
			listTransformacionesFragmentFocusOn = false;
			listSourceImagenesFragmentFocusOn = false;
			
			listAlgoritmosFragment.focusOut();
			listTransformacionesFragment.focusOut();
			listSourceImagenesFragment.focusOut();
			
			if (tab == tabListAlgoritmos) {
				viewTabs.setDisplayedChild(0);
				listAlgoritmosFragmentFocusOn = true;
			} else if (tab == tabListTransformaciones) {
				viewTabs.setDisplayedChild(1);
				listTransformacionesFragmentFocusOn = true;
			} else if (tab == tabListSourceImagenes) {
				viewTabs.setDisplayedChild(2);
				listSourceImagenesFragmentFocusOn = true;
			}
			this.invalidateOptionsMenu();
		}
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}
	
	public void inicioDePruebas() {
		listAlgoritmosFragment.flushDatos();
		listTransformacionesFragment.flushDatos();
		listSourceImagenesFragment.flushDatos();
		
		if (modeloDatos.getListAlgoritmos().isEmpty()) {
			Toast.makeText(this, "Debe definir al menos un algoritmo", Toast.LENGTH_SHORT).show();
			return;
		}
		if (modeloDatos.getListTransformaciones().isEmpty()) {
			Toast.makeText(this, "Debe definir al menos una transformación", Toast.LENGTH_SHORT).show();
			return;
		}
		if (modeloDatos.getListImagenes().isEmpty()) {
			Toast.makeText(this, "Debe definir al menos una imagen", Toast.LENGTH_SHORT).show();
			return;
		}

		if (srvPruebaPerformance == null) {
			serviceConnection = new MyServiceConnection();
			boolean serviceBinded = bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
			if (!serviceBinded) {
				Toast.makeText(this, "No se pudo iniciar el servicio de ejecucion de pruebas.", Toast.LENGTH_SHORT).show();
				return;
			}
		} else {
			lanzarPruebas();
		}
	}

	private void lanzarPruebas() {
		List<AlgCombinacion> lstA = new ArrayList<AlgCombinacion>();
		List<AlgTransformacion> lstT = new ArrayList<AlgTransformacion>();
		List<ImagenOrigen> lstI = new ArrayList<ImagenOrigen>();
		
		lstA.addAll(modeloDatos.getListAlgoritmos());
		lstT.addAll(modeloDatos.getListTransformaciones());
		lstI.addAll(modeloDatos.getListImagenes());
		
		srvPruebaPerformance.asignarAlgoritmosDeEvaluacion(lstA);
		srvPruebaPerformance.asignarAlgoritmosDeTransformaciones(lstT);
		srvPruebaPerformance.asignarImagenesDePrueba(new FileImageLoadIterator(lstI, modeloDatos.getPreferedWidth(), modeloDatos.getPreferedHeigth()));
		ResultadosConfig resultadosConfig = modeloDatos.getResultadosConfig(); 
		String dir = fmtDatePatternPath.format(new Date());
		resultadosConfig.setFolderPathOutResources(dir);
		srvPruebaPerformance.configurarResultadosGenerados(resultadosConfig);
		
		srvPruebaPerformance.asignarProgressHandler(progressChannel);
		
		try {
			srvPruebaPerformance.configurarProcesamiento();
			srvPruebaPerformance.iniciarProcesamiento();
			Intent prgIntent = new Intent(this, ProgressPhoneActivity_.class);
			startActivity(prgIntent);
//			NavUtils.navigateUpFromSameTask(this);
			finish();
		} catch (InputValidationException e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		} catch (ExecutionEvaluationException e) {
			Toast.makeText(this, e.getTipo().toString(), Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}
	
	private void verEjecuciones() {
		Intent intent = new Intent(this, EjecucionesPhoneActivity_.class);
		startActivity(intent);
	}
	
	private class MyServiceConnection implements ServiceConnection {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			srvPruebaPerformance = (SrvPruebaPerformanceBinder)service;
			lanzarPruebas();
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
			srvPruebaPerformance = null;
		}
		
	}

}