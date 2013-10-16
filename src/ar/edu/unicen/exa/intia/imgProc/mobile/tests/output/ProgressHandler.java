package ar.edu.unicen.exa.intia.imgProc.mobile.tests.output;

public interface ProgressHandler {
	
	void started();
	void paused();
	void resumed();
	void canceled();
	void finished();
	
	void updateRange(int minValue, int maxValue, String title);

	void beforeProgress();
	void afterProgress();
	
	void updateProgress(int progressValue, String detail);

}
