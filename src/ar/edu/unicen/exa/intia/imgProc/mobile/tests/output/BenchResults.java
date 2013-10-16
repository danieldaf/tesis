package ar.edu.unicen.exa.intia.imgProc.mobile.tests.output;

import java.io.File;

import android.os.Environment;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ResultadosConfig;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;


public abstract class BenchResults {
	
	protected ResultadosConfig config;
	
	public void setConfig(ResultadosConfig config){
		this.config = config;
	}
	
	public void iniciar() {
	}

	public void finalizar() {
	}
	
	public abstract void addStatsResult(StatisticResults statResult) throws ExecutionEvaluationException;
	
	protected void crearOutFolder() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state) && config.getFolderPathOutResourcesFull() != null &&
				(config.isSaveSourceImages() || config.isSaveTransformedImages() || config.isExportarSqlite())) {
			File dir = new File(Environment.getExternalStorageDirectory() + config.getFolderPathOutResourcesFull());
			if (!dir.exists()) {
				dir.mkdirs();
			}
		}
	}
	
}
