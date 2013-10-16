package ar.edu.unicen.exa.intia.imgProc.mobile;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import ar.edu.unicen.exa.intia.imgProc.mobile.dao.MySQLiteOpenHelper;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ProgressChannel;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.BenchResultsSqlite;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.ConversorClasesAnalogas;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.EjecutorDePruebas;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.SrvPruebaPerformanceBinder;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputExceptionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTransformation;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.BenchResults;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EService;

@EService
public class SrvPruebaPerformance extends Service implements ProgressHandler {
	
	public static final int ONGOING_NOTIFICATION_ID = 13;
	public static final int RCODE_SHOW = 1;

	public static final String ACTION_PAUSE = "Pausar Procesamiento";
	public static final String ACTION_RESUME = "Reanudar Procesamiento";
	public static final String ACTION_ABORT = "Cancelar Procesamiento";
	
	public static final String TAG = SrvPruebaPerformance.class.getName();
	protected static final String dbName = "evaluacionAlgoritmos.sqlite";
	
	protected MySQLiteOpenHelper sqliteHelper;
	protected BenchResults benchResults;
	
	protected Thread threadEjecutor = null;
	protected final EjecutorDePruebas ejecutor = new EjecutorDePruebas();
	
	private boolean isConfigured = false;
	
	private PendingIntent pintentShowActivity;

	private NotificationManager notificationManager;
	private Notification.Builder notificationBuilder;
	
	@Bean
	protected ProgressChannel progressChannel;

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
				Log.i(TAG, "OpenCV loaded successfully");
				break;
			default:
				super.onManagerConnected(status);
				break;
			}
		}
	};
	
	private void createThread() {
		if (threadEjecutor == null || threadEjecutor.getState() == Thread.State.TERMINATED) {
			threadEjecutor = new Thread(new Runnable() {
				@Override
				public void run() {
	//				Debug.resetAllCounts();
	//				Debug.startMethodTracing("tesis");
	//				Debug.startNativeTracing();
					try {
						ejecutor.procesar();
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
	//					Debug.stopNativeTracing();
	//					Debug.stopMethodTracing();
					}
				}
				
			}, "Thread de Evaluacion de Algoritmos");
		}
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		Intent activityIntent = new Intent(this, ProgressPhoneActivity_.class);
		pintentShowActivity = PendingIntent.getActivity(this, RCODE_SHOW, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		this.notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		this.notificationBuilder = new Notification.Builder(this);
		this.sqliteHelper = new MySQLiteOpenHelper(this.getApplicationContext(), dbName);
		this.benchResults = new BenchResultsSqlite(this.sqliteHelper);
		createThread();
//		OpenCV carga dinaminca
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}
	
	@AfterInject
	protected void setupChannel() {
		progressChannel.addHandler(this);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		String action = intent.getAction();
		if (ACTION_PAUSE.equals(action)) 
			pausarProcesamiento();
		else if (ACTION_RESUME.equals(action))
			reanudarProcesamiento();
		else if (ACTION_ABORT.equals(action))
			cancelarProcesamiento();
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent intent) {
		return new SrvPruebaPerformanceBinder(this);
	}

	// --- Metodos de procesamiento
	
	public void configurarProcesamiento(SrvPruebaPerformanceBinder binder) throws InputValidationException, ExecutionEvaluationException {
		if (threadEjecutor.isAlive())
			throw new ExecutionEvaluationException(ExecutionExceptionEnum.EVALUACION_ACTUALMENTE_EN_EJECUCION);
		createThread();
		validarParametrosDeEntrada(binder);
		prepararDatosDeEntrada(binder);
		isConfigured = true;
	}
	
	private void pausarProcesamiento() {
		ejecutor.pausar();
	}
	
	private void reanudarProcesamiento() {
		ejecutor.reanudar();
	}
	
	private void cancelarProcesamiento() {
		ejecutor.abortar();
	}

	public void iniciarProcesamiento(SrvPruebaPerformanceBinder binder) throws InputValidationException, ExecutionEvaluationException {
		if (!this.isConfigured) // && binderConfigurator == binder)
			configurarProcesamiento(binder);

		Notification notification = buildNotificaction(100, 0, true, "Iniciando procesamiento...");

		ejecutor.setProgressHandler(binder.getProgressHandler());
		benchResults.setConfig(binder.getResultadosConfig());
		ejecutor.setResultsHandler(benchResults);
		
		startForeground(ONGOING_NOTIFICATION_ID, notification);		
		
		threadEjecutor.start();		
	}

	public void forzarReconfiguracion() {
		isConfigured = false;
	}

	protected void validarParametrosDeEntrada(SrvPruebaPerformanceBinder binder) throws InputValidationException {
		if (binder.getLstTransformaciones() == null || binder.getLstTransformaciones().isEmpty())
			throw new InputValidationException(InputExceptionEnum.ALGORITMOS_TRANSFORMACION_REQUERIDOS);
		if (binder.getLstAlgoritmos() == null || binder.getLstAlgoritmos().isEmpty())
			throw new InputValidationException(InputExceptionEnum.ALGORITMOS_EVALUACION_REQUERIDOS);
		if (binder.getImgIterator() == null || binder.getImgIterator().size() == 0)
			throw new InputValidationException(InputExceptionEnum.IMAGENES_DE_ENTRADA_REQUERIDA);
	}
	
	protected void prepararDatosDeEntrada(SrvPruebaPerformanceBinder binder) throws InputValidationException {
		List<ImageTransformation> transformaciones = new ArrayList<ImageTransformation>(binder.getLstTransformaciones().size());
		ListIterator<AlgTransformacion> itInputTransformaciones = binder.getLstTransformaciones().listIterator();
		while (itInputTransformaciones.hasNext()) {
			AlgTransformacion txIn = itInputTransformaciones.next();
			ImageTransformation txOut = ConversorClasesAnalogas.armarAlgTransformacionAnalogo(txIn);
			transformaciones.add(txOut);
		}
		ejecutor.setTransformations(transformaciones);
		ejecutor.setAlgorithms(binder.getLstAlgoritmos());
		ejecutor.setImageIterator(binder.getImgIterator());
	}
	
	private Notification buildNotificaction(int maxValue, int value, boolean infinito, String title) {
		double porcentaje = maxValue != 0?(((double)value / (double)maxValue)*100.0):0.0;
		
		Notification notification = notificationBuilder
			.setContentTitle(getText(R.string.app_name))
			.setContentText("%"+(int)Math.floor(porcentaje)+" "+title)
			.setContentIntent(pintentShowActivity)
			.setSmallIcon(R.drawable.ic_notification)
			.setOngoing(true)
			.setProgress(maxValue, value, infinito).getNotification();
		notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
		return notification;
	}

	// -- ProgressHandler
	private int _minValue, _maxValue;
	@Override
	public void updateRange(int minValue, int maxValue, String title) {
		_minValue = minValue;
		_maxValue = maxValue;
		
		Notification notification = buildNotificaction(maxValue-minValue, 0, true, title);
		notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
	}

	@Override
	public void beforeProgress() {
	}

	@Override
	public void afterProgress() {
	}

	@Override
	public void updateProgress(int progressValue, String detail) {
		int _value = progressValue - _minValue;
		Notification notification = buildNotificaction(_maxValue-_minValue, _value, false, detail);
		notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
	}
	// -- ProgressHandler

	@Override
	public void started() {
	}

	@Override
	public void paused() {
	}

	@Override
	public void resumed() {
	}

	@Override
	public void canceled() {
		Notification notification = notificationBuilder
			.setContentTitle(getText(R.string.app_name))
			.setContentText(getText(R.string.prueba_interrumpida))
			.setSmallIcon(R.drawable.ic_notification)
			.setAutoCancel(true)
			.getNotification();
		notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
		Toast.makeText(getApplicationContext(), R.string.prueba_interrumpida, Toast.LENGTH_LONG).show();
	}

	@Override
	public void finished() {
		if (!ejecutor.isAborted()) {
			Notification notification = notificationBuilder
				.setContentTitle(getText(R.string.app_name))
				.setContentText(getText(R.string.prueba_finalizada_satisfacgoriamente))
				.setSmallIcon(R.drawable.ic_notification)
				.setAutoCancel(true)
				.getNotification();
			notificationManager.notify(ONGOING_NOTIFICATION_ID, notification);
			Toast.makeText(getApplicationContext(), R.string.prueba_finalizada_satisfacgoriamente, Toast.LENGTH_LONG).show();
		}
	}
}