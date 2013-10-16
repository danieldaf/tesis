package ar.edu.unicen.exa.intia.imgProc.mobile.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.ProgressHandler;

import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.api.Scope;

@EBean(scope = Scope.Singleton)
public class ProgressChannel implements ProgressHandler {
	
	private List<ProgressHandler> handlers = new ArrayList<ProgressHandler>();
	
	private int minValue, maxValue, progressValue;
	private List<ParLog> lineasLog = new ArrayList<ParLog>();
	
	private boolean _isStarted = false;
	private boolean _isPaused = false;
	private boolean _isCanceled = false;
	private boolean _isStoped = false;
	
	public class ParLog {
		private Date fecha;
		private String linea;
		
		public ParLog(String linea) {
			this.fecha = new Date();
			this.linea = linea;
		}
		
		public Date getFecha() {
			return fecha;
		}
		public String getLinea() {
			return linea;
		}
	}
	
	public void addHandler(ProgressHandler handler) {
		if (!handlers.contains(handler))
			handlers.add(handler);
	}
	
	public void removeHandler(ProgressHandler hanlder) {
		handlers.remove(hanlder);
	}
		
	public int getMinValue() {
		return minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public int getProgressValue() {
		return progressValue;
	}

	public List<ParLog> getLineasLog() {
		return lineasLog;
	}

	//-- ProgressHandler
	@Override
	public void updateRange(int minValue, int maxValue, String title) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		for (ProgressHandler handler : handlers) {
			handler.updateRange(minValue, maxValue, title);
		}
	}

	@Override
	public void beforeProgress() {
		lineasLog.clear();
		for (ProgressHandler handler : handlers) {
			handler.beforeProgress();
		}
	}

	@Override
	public void afterProgress() { 
		for (ProgressHandler handler : handlers) {
			handler.afterProgress();
		}
	}

	@Override
	public void updateProgress(int progressValue, String detail) {
		this.progressValue = progressValue;
		for (ProgressHandler handler : handlers) {
			handler.updateProgress(progressValue, detail);
		}
		if (!lineasLog.isEmpty()) {
			ParLog parPrev = lineasLog.get(lineasLog.size()-1);
			int posMatch = parPrev.linea.indexOf('/', parPrev.linea.length()-7);
			if (posMatch == -1)
				posMatch = parPrev.linea.length();
			else {
				int posMatch2 = parPrev.linea.indexOf(' ', posMatch-3);
				if (posMatch2 > -1 && posMatch2 <  posMatch)
					posMatch = posMatch2;
				else
					posMatch = parPrev.linea.length();
			}
			String prefix = parPrev.linea.substring(0, posMatch);
			if (detail.startsWith(prefix))
				parPrev.linea = detail;
			else
				lineasLog.add(new ParLog(detail));
		} else
			lineasLog.add(new ParLog(detail));
	}

	@Override
	public void started() {
		minValue=0;
		maxValue=100;
		progressValue=0;
		lineasLog.clear();
		
		_isStarted = true;
		_isPaused = false;
		_isCanceled = false;
		_isStoped = false;
	}

	@Override
	public void paused() {
		_isPaused = true;
	}

	@Override
	public void resumed() {
		_isPaused = false;
	}

	@Override
	public void canceled() {
		_isCanceled = true;
	}

	@Override
	public void finished() {
		_isStoped = true;
	}
	//-- ProgressHandler
	
	public boolean isRunning() {
		return _isStarted && !_isStoped;
	}
	
	public boolean isPaused() {
		return _isStarted && _isPaused;
	}
	
	public boolean isCanceled() {
		return _isStarted && _isCanceled;
	}
}