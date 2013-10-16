package ar.edu.unicen.exa.intia.imgProc.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import ar.edu.unicen.exa.intia.imgProc.mobile.extractorCaracteristicas.pruebasPerfomance.R;
import ar.edu.unicen.exa.intia.imgProc.mobile.model.ModeloDeDatos;
import ar.edu.unicen.exa.intia.imgProc.mobile.view.fragments.ListEjecuciones;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.FragmentById;

@EActivity(R.layout.ejecuciones_phone)
public class EjecucionesPhoneActivity extends Activity {

	public static final String TAG = EjecucionesPhoneActivity.class.getName();
	
	@FragmentById(R.id.list_ejecuciones_fragment)
	protected ListEjecuciones listEjecucionesFragment;
	
	@Bean
	protected ModeloDeDatos modeloDatos;
	
	@Override
	protected void onCreate(Bundle savedState) {
		super.onCreate(savedState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.ejecuciones_phone, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.mnu_refresh_list_ejecuciones:
			this.listEjecucionesFragment.refreshListado();
			return true;
		case R.id.mnu_launch_test_activity:
			invocarPruebas();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
		
	private void invocarPruebas() {
		Intent pruebasActivity = new Intent(this, PruebasPhoneActivity_.class);
		startActivity(pruebasActivity);
	}
}