package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EstadisticaCoincidencias;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.EstadisticaEficiencia;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.BitmapUtils;

public class EjecucionDao {
	
	private SQLiteDatabase database;

	private AlgTransformacionDao algTransformacionDao = new AlgTransformacionDao();
	
	public EjecucionDao(String dbFileName) {
		database = SQLiteDatabase.openDatabase(dbFileName, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	public void release() {
		database.close();
	}
	
	public String getDeviceInfo() {
		String result = null;
		Cursor cursor = database.rawQuery("select valor from parametro where nombre = ?", new String[] {"DEVICE_INFO"});
		if (cursor.moveToFirst()) 
			result = cursor.getString(0);
		cursor.close();
		return result;
	}
	
	public String getFechaEjecucion() {
		String result = null;
		Cursor cursor = database.rawQuery("select valor from parametro where nombre = ?", new String[] {"FECHA_EJECUCION"});
		if (cursor.moveToFirst()) 
			result = cursor.getString(0);
		cursor.close();
		return result;
	}
	
	public String getFechaEjecucionFin() {
		String result = null;
		Cursor cursor = database.rawQuery("select valor from parametro where nombre = ?", new String[] {"FECHA_EJECUCION_FIN"});
		if (cursor.moveToFirst()) 
			result = cursor.getString(0);
		cursor.close();
		return result;
	}

	public long getDuracionMSEjecucion() {
		long result = 0;
		Cursor cursor = database.rawQuery("select valor from parametro where nombre = ?", new String[] {"DURACION_EJECUCION_MS"});
		if (cursor.moveToFirst()) 
			result = cursor.getLong(0);
		cursor.close();
		return result;
	}

	public String getAlgoritmos() {
		String result = null;
		Cursor cursor = database.query(true, "algoritmo", new String[] {"nombre"}, null, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				if (result == null) 
					result = cursor.getString(0);
				else
					result += ", "+cursor.getString(0);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return result;
	}
	
	public int getCantImagenes() {
		int result = 0;
		Cursor cursor = database.rawQuery("select count(nombre) from imagen_origen", null);
		if (cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public int getCantTransformaciones() {
		int result = 0;
		Cursor cursor = database.rawQuery("select count(*) from transformacion", null);
		if (cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public int getCantAlgoritmos() {
		int result = 0;
		Cursor cursor = database.rawQuery("select count(*) from algoritmo", null);
		if (cursor.moveToFirst())
			result = cursor.getInt(0);
		cursor.close();
		return result;
	}
	
	public List<AlgTransformacion> getTransformaciones() {
		return algTransformacionDao.buildListFromCursor(database.query(algTransformacionDao.TABLA, algTransformacionDao.COLUMNAS, 
				null, null, null, null, algTransformacionDao.ORDER_BY));
	}
	
	public String getImagenDistintiva(String pathBase) {
		String result = null;
		Cursor cursor = database.rawQuery("select uri from imagen_origen", null);
		if (cursor.moveToFirst()) {
			do {
				String path = cursor.getString(0);
				File f = new File(BitmapUtils.pathFromUri(Uri.parse(path)));
				if (f != null && f.exists() && f.canRead()) {
					result = path;
					break;
				} else {
					String np = pathBase + File.separator + f.getName();
					f = new File(np);
					if (f != null && f.exists() && f.canRead()) {
						result = BitmapUtils.uriFromPath(np).toString();
						break;
					}
				}
			} while (cursor.moveToNext());
		}
		cursor.close();
		return result;
	}

	public Map<String, List<EstadisticaCoincidencias>> obtenerPorcentajeDeMatchesCorrectos(AlgTransformacion algTransformacion) {
		Map<String, List<EstadisticaCoincidencias>> result = null;
		String sql = "";
		sql += " select ";
		sql += "   E._id,";
		sql += "   A.nombre as Algoritmo,";
		sql += "   T.nombre as Transformacion,";
		sql += "   count(E.percent_of_matches) as 'Count_Matches',";
		sql += "   sum(E.percent_of_matches) as 'Percent_Acum',";
		sql += "   sum(E.correct_matches_percent) as 'Percent_Correct_Acum',";
		sql += "   E.argument_value as 'Paso_Transformacion'";
		sql += " from ";
		sql += "   estadistica as E";
		sql += "   left join algoritmo as A on (A._id=E.id_algoritmo)";
		sql += "   left join transformacion as T on (T._id=E.id_transformacion)";
		sql += " where ";
		sql += "   T._id=?";
		sql += " group by T.nombre, A.nombre, E.argument_value";
		sql += " order by T.nombre, A.nombre, E.argument_value";
		
		Cursor cursor = database.rawQuery(sql, new String[] {algTransformacion.getId().toString()});
		if (cursor.moveToFirst()) {
			result = new HashMap<String, List<EstadisticaCoincidencias>>();
			do {
				EstadisticaCoincidencias item = new EstadisticaCoincidencias();
				item.setIdEstadistica(cursor.getLong(0));
				item.setAlgoritmos(cursor.getString(1));
				item.setTrasnformacion(cursor.getString(2));
				item.setCountMatches(cursor.getDouble(3));
				item.setPercentAcum(cursor.getDouble(4));
				item.setPercentCorrectAcum(cursor.getDouble(5));
				item.setPasoTransformacion(cursor.getString(6));
				
				double valor = 0.0;
				if (item.getPercentAcum() != 0.0)
					valor = item.getPercentCorrectAcum() / item.getPercentAcum() * 100.0;
				item.setValor(valor);
				
				List<EstadisticaCoincidencias> lista = result.get(item.getAlgoritmos());
				if (lista == null) {
					lista = new ArrayList<EstadisticaCoincidencias>();
					result.put(item.getAlgoritmos(), lista);
				}
				lista.add(item);
			} while(cursor.moveToNext());
		}
		cursor.close();
		return result;
	}
	
	public List<EstadisticaEficiencia> obtenerPorcentajeEficienciaPorAlgoritmos() {
		List<EstadisticaEficiencia> result = null;
		String sql = "";
		sql += " select ";
		sql += "   A.nombre as Algoritmo,";
		sql += "   sum(E.total_keypoints) as 'TotalKeypoints',";
		sql += "   sum(E.consumed_time_ms_detector) as 'TimeDetector',";
		sql += "   sum(E.consumed_time_ms_extractor) as 'TimeExtractor',";
		sql += "   sum(E.consumed_time_ms_matcher) as 'TimeMatcher',";
		sql += "   sum(E.consumed_time_ms) as 'TimeTotal',";
		sql += "   count(E._id) as 'CountEstadisticas'";
		sql += " from ";
		sql += "   estadistica as E";
		sql += "   left join algoritmo as A on (A._id=E.id_algoritmo)";
		sql += " group by A.nombre";
//		(E.consumed_time_ms_detector/E.total_keypoints1) as "TiempoDeteccionPorKeypoints",
//		(E.consumed_time_ms_extractor/E.total_keypoints) as "TiempoExtraccionTiempoPorKeypoints",
//		((E.consumed_time_ms_detector+E.consumed_time_ms_extractor)/E.total_keypoints) as "TiempoDetExtPorKeypoints",
//		(E.consumed_time_ms/E.total_keypoints) as "TiempoTotalPorKeypoints",
		
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.moveToFirst()) {
			result = new ArrayList<EstadisticaEficiencia>();
			do {
				EstadisticaEficiencia item = new EstadisticaEficiencia();
				item.setAlgoritmo(cursor.getString(0));
				double totalKeyPoints = cursor.getDouble(1);
				item.setTiempoDeteccionPorKeypoints(cursor.getDouble(2)/totalKeyPoints );
				item.setTiempoExtraccionPorKeypoints(cursor.getDouble(3)/totalKeyPoints);
				item.setTiempoMatchingPorKeypoints(cursor.getDouble(4)/totalKeyPoints);
				item.setTiempoDetExtPorKeypoints((cursor.getDouble(2)+cursor.getDouble(3)+cursor.getDouble(4))/totalKeyPoints);
				item.setTiempoTotalPorKeypoints(cursor.getDouble(5)/totalKeyPoints);
				result.add(item);
			} while(cursor.moveToNext());
		}

		return result;
	}

	//TODO: no usado en ninguna lado.... aun...
	public List<EstadisticaEficiencia> obtenerPorcentajeEficienciaPorAlgoritmos(AlgTransformacion algTransformacion) {
		List<EstadisticaEficiencia> result = null;
		String sql = "";
		sql += " select ";
		sql += "   A.nombre as Algoritmo,";
		sql += "   sum(E.total_keypoints) as 'TotalKeypoints',";
		sql += "   sum(E.consumed_time_ms_detector) as 'TimeDetector',";
		sql += "   sum(E.consumed_time_ms_extractor) as 'TimeExtractor',";
		sql += "   sum(E.consumed_time_ms_matcher) as 'TimeMatcher',";
		sql += "   sum(E.consumed_time_ms) as 'TimeTotal',";
		sql += "   count(E._id) as 'CountEstadisticas'";
		sql += " from ";
		sql += "   estadistica as E";
		sql += "   left join algoritmo as A on (A._id=E.id_algoritmo)";
		sql += " where ";
		sql += "   E.id_transformacion = ? ";
		sql += " group by A.nombre";
//		(E.consumed_time_ms_detector/E.total_keypoints1) as "TiempoDeteccionPorKeypoints",
//		(E.consumed_time_ms_extractor/E.total_keypoints) as "TiempoExtraccionTiempoPorKeypoints",
//		((E.consumed_time_ms_detector+E.consumed_time_ms_extractor)/E.total_keypoints) as "TiempoDetExtPorKeypoints",
//		(E.consumed_time_ms/E.total_keypoints) as "TiempoTotalPorKeypoints",
		
		Cursor cursor = database.rawQuery(sql, new String[] {algTransformacion.getId().toString()});
		if (cursor.moveToFirst()) {
			result = new ArrayList<EstadisticaEficiencia>();
			do {
				EstadisticaEficiencia item = new EstadisticaEficiencia();
				item.setAlgoritmo(cursor.getString(0));
				double totalKeyPoints = cursor.getDouble(1);
				item.setTiempoDeteccionPorKeypoints(cursor.getDouble(2)/totalKeyPoints );
				item.setTiempoExtraccionPorKeypoints(cursor.getDouble(3)/totalKeyPoints);
				item.setTiempoMatchingPorKeypoints(cursor.getDouble(4)/totalKeyPoints);
				item.setTiempoDetExtPorKeypoints((cursor.getDouble(2)+cursor.getDouble(3)+cursor.getDouble(4))/totalKeyPoints);
				item.setTiempoTotalPorKeypoints(cursor.getDouble(5)/totalKeyPoints);
				result.add(item);
			} while(cursor.moveToNext());
		}

		return result;
	}
}
