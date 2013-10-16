package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public class TransformacionChartData extends ChartData {
	
	protected AlgTransformacion algTransformacion;
	
	public TransformacionChartData(AlgTransformacion algTransformacion) {
		this.algTransformacion = algTransformacion;
	}

	public AlgTransformacion getAlgTransformacion() {
		return algTransformacion;
	}

}
