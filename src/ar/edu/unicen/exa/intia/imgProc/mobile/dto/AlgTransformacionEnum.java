package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

public enum AlgTransformacionEnum {
	COMBINACION("Combinacion", null, false),
	BRIGHTNESS("Brillo", "{'min':'-127', 'max':'+127', 'step':'10'}", true),
	GAUSSIAN_BLUR("Desenfoque", "{'maxKernelSize':'20'}", true),
	ROTATION("Rotacion", "{'startAngleInDeg':'0', 'endAngleInDeg':'360', 'step':'10', 'rotationCenterInUnitSpaceX':'0.5', 'rotationCenterInUnitSpaceY':'0.5'}", true),
	SCALING("Escala", "{'minScale':'0.125', 'maxScale':'2.0', 'step':'0.125'}", true),
	TRANSLATION("Traslacion", null, false);
	
	private String nombre;
	private String defaultJsonCaracteristicas;
	private boolean enabled;
	
	private AlgTransformacionEnum(String nombre, String jsonCaracteristicas, boolean enabled) {
		this.nombre = nombre;
		this.defaultJsonCaracteristicas = jsonCaracteristicas;
		this.enabled = enabled;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getDefaultJsonCaracteristicas() {
		return defaultJsonCaracteristicas;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public static AlgTransformacionEnum valueFromNombre(String nombre) {
		AlgTransformacionEnum[] valores = AlgTransformacionEnum.values();
		for (int pos=0; pos<valores.length; pos++) {
			if (valores[pos].nombre.equalsIgnoreCase(nombre))
				return valores[pos];
		}
		return null;
	}
}