package ar.edu.unicen.exa.intia.imgProc.mobile.utils;

import java.util.ArrayList;
import java.util.List;

import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.ConversorClasesAnalogas;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.InputValidationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.ImageTransformation;

public class AlgTransformacionUtils {
	
	public static String getLabelEjeX(AlgTransformacion tx) {
		if (tx.getTipo() == AlgTransformacionEnum.BRIGHTNESS)
			return "Ajuste de Brillo";
		else if (tx.getTipo() == AlgTransformacionEnum.GAUSSIAN_BLUR)
			return "Ajuste de Foco";
		else if (tx.getTipo() == AlgTransformacionEnum.ROTATION)
			return "Angulo de Rotacion";
		else if (tx.getTipo() == AlgTransformacionEnum.SCALING)
			return "Factor de Escala";
		return "";
	}
	
	public static String getLabelEjeY(AlgTransformacion tx) {
		return "% Coincidencias";
	}
	
	public static List<String> getTicksEjeX(AlgTransformacion tx) {
		List<String> result = new ArrayList<String>();
		
		try {
			ImageTransformation txAnalogo = ConversorClasesAnalogas.armarAlgTransformacionAnalogo(tx);
			List<Double> steps = txAnalogo.getConfigArguments();
			for (Double step : steps) {
				result.add(""+step.doubleValue());
			}
		} catch (InputValidationException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public static List<String> getTicksEjeY(AlgTransformacion tx) {
		List<String> result = new ArrayList<String>();
		for (double v=0; v<=100.0; v+=10.0) {
			result.add(""+v);
		}
		return result;
	}
}
