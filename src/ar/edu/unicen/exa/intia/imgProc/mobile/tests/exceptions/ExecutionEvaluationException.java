package ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions;

public class ExecutionEvaluationException extends Exception {

	private static final long serialVersionUID = -6006954948843717368L;
	private ExecutionExceptionEnum tipo;
	
	public ExecutionEvaluationException(ExecutionExceptionEnum tipo) {
		super();
		this.tipo = tipo;
	}

	public ExecutionEvaluationException(ExecutionExceptionEnum tipo, String message) {
		super(message);
		this.tipo = tipo;
	}
	
	public ExecutionExceptionEnum getTipo() {
		return tipo;
	}
}
