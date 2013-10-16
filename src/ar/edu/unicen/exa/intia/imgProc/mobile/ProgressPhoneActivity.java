/**
 * 
 */
package ar.edu.unicen.exa.intia.imgProc.mobile;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ProgressChannel;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ProgressChannel.ParLog;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;

/**
 * @author Daniel Fuentes
 *
 */
@EActivity(R.layout.progress_phone)
public class ProgressPhoneActivity extends Activity implements ProgressHandler {
	
	@SuppressLint("SimpleDateFormat")
	private static final SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss.SSS");

	@ViewById(R.id.btnPausarTest)
	protected Button btnPausar;
	@ViewById(R.id.btnReanudarTest)
	protected Button btnReanudar;
	@ViewById(R.id.btnFinalizarTest)
	protected Button btnFinalizar;
	@ViewById(R.id.btnCerrarTest)
	protected Button btnCerrar;
	
	@ViewById(R.id.prgPruebas)
	protected ProgressBar prgPruebas;
	@ViewById(R.id.txtProgreso)
	protected TextView txtProgreso;
	@ViewById(R.id.txtLogPruebas)
	protected TextView txtLogPruebas;
	
	@Bean
	protected ModeloDeDatos modeloDatos;
	@Bean
	protected ProgressChannel progressChannel;
	
	private Intent serviceIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		serviceIntent = new Intent(this, SrvPruebaPerformance_.class);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
		
	@AfterViews
	@UiThread
	protected void initUi() {
		prgPruebas.setIndeterminate(true);
		updateListLog();

		btnPausar.setEnabled(false);
		btnPausar.setVisibility(View.GONE);
		btnReanudar.setEnabled(false);
		btnReanudar.setVisibility(View.GONE);
		btnFinalizar.setEnabled(false);
		btnFinalizar.setVisibility(View.GONE);
		btnCerrar.setEnabled(true);
		btnCerrar.setVisibility(View.VISIBLE);
		
		if (progressChannel.isRunning() && !progressChannel.isCanceled()) {
			btnFinalizar.setEnabled(true);
			btnFinalizar.setVisibility(View.VISIBLE);
			btnCerrar.setEnabled(false);
			btnCerrar.setVisibility(View.GONE);

			if (progressChannel.isPaused()) {
				btnPausar.setEnabled(false);
				btnPausar.setVisibility(View.GONE);
				btnReanudar.setEnabled(true);
				btnReanudar.setVisibility(View.VISIBLE);
			} else {
				btnPausar.setEnabled(true);
				btnPausar.setVisibility(View.VISIBLE);
				btnReanudar.setEnabled(false);
				btnReanudar.setVisibility(View.GONE);
			}
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		progressChannel.addHandler(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		progressChannel.removeHandler(this);
	}
	
	@Override 
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void updateListLog() {
		prgPruebas.setMax(progressChannel.getMaxValue());
		prgPruebas.setProgress(progressChannel.getProgressValue());
		String texto = "";
		String detail = "";
		List<ParLog> lineasLog = progressChannel.getLineasLog();
		for (ParLog parLog : lineasLog) {
			String tiempo = fmt.format(parLog.getFecha());
			detail = parLog.getLinea();
			texto = tiempo+"\n"+detail+"\n\n"+texto;			
		}
		txtLogPruebas.setText(texto);
		
		double maxValue = prgPruebas.getMax();
		double porcentaje = maxValue != 0?(((double)progressChannel.getProgressValue()/ (double)maxValue)*100.0):0.0;
		txtProgreso.setText("%"+(int)Math.floor(porcentaje)+" "+detail);
	}

	private int lineasAdded = 0;
	protected void addTxtLog(String texto) {
		if (lineasAdded < 10) {
			String tiempo = fmt.format(new Date());
			String prev = txtLogPruebas.getText().toString();
			String txt = tiempo+"\n"+texto+"\n\n"+prev;
			txtLogPruebas.setText(txt);
		} else {
			lineasAdded = 0;
			updateListLog();
		}
		lineasAdded++;
	}
	
	@Click(R.id.btnPausarTest)
	protected void onClickPausar() {
		serviceIntent.setAction(SrvPruebaPerformance.ACTION_PAUSE);
		startService(serviceIntent);
		btnPausar.setEnabled(false);
		btnPausar.setVisibility(View.GONE);
		btnReanudar.setEnabled(true);
		btnReanudar.setVisibility(View.VISIBLE);
		btnFinalizar.setEnabled(true);
		btnFinalizar.setVisibility(View.VISIBLE);
		btnCerrar.setEnabled(false);
		btnCerrar.setVisibility(View.GONE);		
	}

	@Click(R.id.btnReanudarTest)
	protected void onClickReanudar() {
		serviceIntent.setAction(SrvPruebaPerformance.ACTION_RESUME);
		startService(serviceIntent);
		btnPausar.setEnabled(true);
		btnPausar.setVisibility(View.VISIBLE);
		btnReanudar.setEnabled(false);
		btnReanudar.setVisibility(View.GONE);
		btnFinalizar.setEnabled(true);
		btnFinalizar.setVisibility(View.VISIBLE);
		btnCerrar.setEnabled(false);
		btnCerrar.setVisibility(View.GONE);		
	}
	
	@Click(R.id.btnFinalizarTest)
	protected void onClickAbortar() {
		serviceIntent.setAction(SrvPruebaPerformance.ACTION_ABORT);
		startService(serviceIntent);
		btnPausar.setEnabled(false);
		btnPausar.setVisibility(View.GONE);
		btnReanudar.setEnabled(false);
		btnReanudar.setVisibility(View.GONE);
		btnFinalizar.setEnabled(false);
		btnFinalizar.setVisibility(View.GONE);
		btnCerrar.setEnabled(true);
		btnCerrar.setVisibility(View.VISIBLE);
	}
	
	@Click(R.id.btnCerrarTest)
	protected void onClickCerrar() {		
		stopService(serviceIntent);
		NavUtils.navigateUpFromSameTask(this);
//		finish();
	}

	//-- ProgressHandler
	@UiThread
	@Override
	public void updateRange(int minValue, int maxValue, String title) {
		prgPruebas.setMax(maxValue);
		prgPruebas.setProgress(minValue);
		prgPruebas.setIndeterminate(false);
		addTxtLog(title);
	}

	@UiThread
	@Override
	public void beforeProgress() {
		addTxtLog("Iniciando pruebas...");
		prgPruebas.setIndeterminate(true);
		btnPausar.setEnabled(true);
		btnPausar.setVisibility(View.VISIBLE);
		btnReanudar.setEnabled(false);
		btnReanudar.setVisibility(View.GONE);
		btnFinalizar.setEnabled(true);
		btnFinalizar.setVisibility(View.VISIBLE);
		btnCerrar.setEnabled(false);
		btnCerrar.setVisibility(View.GONE);
	}

	@UiThread
	@Override
	public void afterProgress() { 
		addTxtLog("Pruebas finalizadas.");
		btnPausar.setEnabled(false);
		btnPausar.setVisibility(View.GONE);
		btnReanudar.setEnabled(false);
		btnReanudar.setVisibility(View.GONE);
		btnFinalizar.setEnabled(false);
		btnFinalizar.setVisibility(View.GONE);
		btnCerrar.setEnabled(true);
		btnCerrar.setVisibility(View.VISIBLE);
	}

	@UiThread
	@Override
	public void updateProgress(int progressValue, String detail) {
		double maxValue = prgPruebas.getMax();
		double porcentaje = maxValue != 0?(((double)progressValue/ (double)maxValue)*100.0):0.0;
		txtProgreso.setText("%"+(int)Math.floor(porcentaje)+" "+detail);
		prgPruebas.setProgress(progressValue);
		prgPruebas.setIndeterminate(false);
		addTxtLog(detail);
	}

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
	}

	@Override
	public void finished() {
	}
	//-- ProgressHandler
}
