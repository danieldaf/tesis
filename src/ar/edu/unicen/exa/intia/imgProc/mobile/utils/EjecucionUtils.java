package ar.edu.unicen.exa.intia.imgProc.mobile.utils;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.os.Environment;
import ar.edu.unicen.exa.intia.imgProc.mobile.dao.EjecucionDao;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EjecucionDto;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.BenchResultsSqlite;

public class EjecucionUtils {
	
	public static EjecucionDto buildFromPath(String strPathEjecucion) {
		File pathEjecucion = new File(strPathEjecucion);
		return buildFromPath(pathEjecucion);
	}
	
	public static EjecucionDto buildFromPath(File pathEjecucion) {
		EjecucionDto item = null;
		if (pathEjecucion.exists() && pathEjecucion.canRead() && pathEjecucion.isDirectory()) {
			File[] dbEjecucion = pathEjecucion.listFiles(new FilenameFilter() {
				@Override
				public boolean accept(File dir, String filename) {
					if (BenchResultsSqlite.DEFAULT_DB_NAME.equals(filename))
						return true;
					return false;
				}
			});
			
			if (dbEjecucion != null && dbEjecucion.length > 0) {
				item = new EjecucionDto();
				item.setPathDirectory(pathEjecucion.getAbsolutePath());
				item.setPathDBFile(dbEjecucion[0].getAbsolutePath());
				EjecucionDao dao = new EjecucionDao(item.getPathDBFile());
				item.setDeviceInfo(dao.getDeviceInfo());
				item.setFechaInicio(dao.getFechaEjecucion());
				if (item.getFechaInicio() == null)
					item.setFechaInicio(pathEjecucion.getName());
				item.setFechaFin(dao.getFechaEjecucionFin());
				item.setDuracionMS(dao.getDuracionMSEjecucion());
				item.setAlgoritmos(dao.getAlgoritmos());
				item.setCantImagenes(dao.getCantImagenes());
				item.setCantTransformaciones(dao.getCantTransformaciones());
				item.setCantAlgoritmos(dao.getCantAlgoritmos());
				item.setImagePath(dao.getImagenDistintiva(item.getPathDirectory()));
				dao.release();
			}
		}
		return item;
	}
	
	public static List<EjecucionDto> obtenerListadoEjecuciones(String pathBase) {
		List<EjecucionDto> result = new ArrayList<EjecucionDto>();

		String state = Environment.getExternalStorageState();
		//BroadcastReceiver para monitorear el estado del sdcard
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File dirBase = new File(Environment.getExternalStorageDirectory() + pathBase);
			if (dirBase.exists() && dirBase.canRead() && dirBase.isDirectory()) {
				
				File[] pathEjecuciones = dirBase.listFiles();
				for (File pathEjecucion : pathEjecuciones) {
					EjecucionDto item = buildFromPath(pathEjecucion);
					if (item != null)
						result.add(item);
				}
				
				Collections.sort(result, new Comparator<EjecucionDto>() {
					@Override
					public int compare(EjecucionDto dto1, EjecucionDto dto2) {
						int result = dto2.getFechaInicio().compareTo(dto1.getFechaInicio());
						return result;
					}
				});
			}
		}
		
		return result;
	}

}
