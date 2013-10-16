package ar.edu.unicen.exa.intia.imgProc.mobile.utils;

import android.os.Build;

public class DeviceInfoUtils {

	public static String getDeviceName() {
		String cpu = Build.CPU_ABI;
		String cpu2 = Build.CPU_ABI2;
		if (cpu2 != null && cpu2.trim().length() > 0 && !cpu.contains(cpu2))
			cpu = cpu + "/" + cpu2;
		String manufacturer = Build.MANUFACTURER;
		String model = Build.MODEL;
		String result;
		if (model.startsWith(manufacturer)) {
			result = capitalize(model);
		} else {
			result = capitalize(manufacturer) + " " + model;
		}
		result += " (" + cpu +")";
		return result;
	}

	private static String capitalize(String s) {
		if (s == null || s.length() == 0) {
			return "";
		}
		char first = s.charAt(0);
		if (Character.isUpperCase(first)) {
			return s;
		} else {
			return Character.toUpperCase(first) + s.substring(1);
		}
	}
}
