package ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions;

public class InputValidationException extends Exception {
	
	private static final long serialVersionUID = -4806975504711494891L;
	private InputExceptionEnum tipo;
	
	public InputValidationException(InputExceptionEnum tipo) {
		super();
		this.tipo = tipo;
	}

	public InputValidationException(InputExceptionEnum tipo, String message) {
		super(message);
		this.tipo = tipo;
	}
	
	public InputExceptionEnum getTipo() {
		return tipo;
	}
}
