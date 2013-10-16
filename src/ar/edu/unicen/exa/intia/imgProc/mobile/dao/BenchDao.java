package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.Estadistica;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenTransformada;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.KeyPoint;
import ar.edu.unicen.exa.intia.imgProc.mobile.utils.DeviceInfoUtils;

public class BenchDao {
	
	protected SQLiteDatabase db;
	protected long contadorTransaccion = 0;
	
	@SuppressLint("SimpleDateFormat")
	protected SimpleDateFormat fechaEjecucionFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
	
	protected long timestampInicio;
	protected long timestampFin;
	
	protected AlgCombinacionDao algCombinacionDao = new AlgCombinacionDao();
	protected AlgTransformacionDao algTransformacionDao = new AlgTransformacionDao();
	protected ImagenOrigenDao imagenOrigenDao = new ImagenOrigenDao();
	protected ImagenTransformadaDao imagenTransformadaDao = new ImagenTransformadaDao();
	protected EstadisticaDao estadisticaDao = new EstadisticaDao();
	protected SourceKeyPointDao sourceKeyPointDao = new SourceKeyPointDao();
	protected TransformedKeyPointDao transformedKeyPointDao = new TransformedKeyPointDao();
	
	public BenchDao(SQLiteDatabase db) {
		this.db = db;
	}
	
	public String getDbFileName() {
		String result = null;
		if (db != null)
			result = db.getPath();
		return result;
	}
	
	public synchronized void iniciarTransaction() {
		if (!db.inTransaction()) {
			db.beginTransactionNonExclusive();
		}
		contadorTransaccion++;
	}
	
	public synchronized void commitTransaction() {
		contadorTransaccion--;
		if (db.inTransaction() && contadorTransaccion == 0) {
			db.setTransactionSuccessful();
			db.endTransaction();
		}
	}
	
	public synchronized void rollbackTransaction() {
		contadorTransaccion--;
		if (db.inTransaction() && contadorTransaccion == 0) {
			db.endTransaction();
		}
	}
	
	public void close() {
		db.close();
	}
	
	public boolean isOpen() {
		return db.isOpen();
	}
	
	public void asentarDeviceInfo() {
		String info = DeviceInfoUtils.getDeviceName();
		iniciarTransaction();

		ContentValues valores = new ContentValues();
		valores.put("nombre", "DEVICE_INFO");
		valores.put("valor", info);
		db.insert("parametro", null, valores);
		
		commitTransaction();
	}
	
	public String asentarEjecucion() {
		Date t = new GregorianCalendar().getTime();
		timestampInicio = t.getTime();
		return asentarEjecucion(fechaEjecucionFmt.format(t));
	}
	
	private String asentarEjecucion(String fechaEjecucion) {
		iniciarTransaction();

		ContentValues valores = new ContentValues();
		valores.put("nombre", "FECHA_EJECUCION");
		valores.put("valor", fechaEjecucion);
		db.insert("parametro", null, valores);
		
		commitTransaction();
		return fechaEjecucion;
	}
	
	public String asentarFinEjecucion() {
		Date t = new GregorianCalendar().getTime();
		timestampFin = t.getTime();
		iniciarTransaction();
		String fechaEjecucion = fechaEjecucionFmt.format(t);

		ContentValues valores = new ContentValues();
		valores.put("nombre", "FECHA_EJECUCION_FIN");
		valores.put("valor", fechaEjecucion);
		db.insert("parametro", null, valores);
		
		valores.clear();
		valores.put("nombre", "DURACION_EJECUCION_MS");
		valores.put("valor", timestampFin-timestampInicio);
		db.insert("parametro", null, valores);

		commitTransaction();
		return fechaEjecucion;
	}

	public AlgCombinacion recuperarAlgCombinacion(String nombre) {
		return algCombinacionDao.buildObjectFromCursor(db.query(algCombinacionDao.TABLA, algCombinacionDao.COLUMNAS, 
				"nombre=?", new String[] {nombre}, null, null, algCombinacionDao.ORDER_BY));
	}
	
	public AlgCombinacion asentarAlgCombinacion(AlgCombinacion algoritmo) {
		AlgCombinacion result = null;
		AlgCombinacion resultSaved = recuperarAlgCombinacion(algoritmo.getNombre());
		if (resultSaved == null) {
			iniciarTransaction();
			long id = db.insert(algCombinacionDao.TABLA, null, algCombinacionDao.buildValuesForInsert(algoritmo));
			algoritmo.setId(id);
			commitTransaction();		
		} else {
			algoritmo.setId(resultSaved.getId());
		}
		result = algoritmo;
		return result;
	}
	
	public AlgTransformacion recuperarAlgTransformacion(String nombre) {
		return algTransformacionDao.buildObjectFromCursor(db.query(algTransformacionDao.TABLA, algTransformacionDao.COLUMNAS, 
				"nombre=?", new String[] {nombre}, null, null, algTransformacionDao.ORDER_BY));
	}
	
	public AlgTransformacion asentarAlgTransformacion(AlgTransformacion transformacion) {
		AlgTransformacion result = null;
		AlgTransformacion resultSaved = recuperarAlgTransformacion(transformacion.getTipo().getNombre());
		if (resultSaved == null) {
			iniciarTransaction();
			long id = db.insert(algTransformacionDao.TABLA, null, algTransformacionDao.buildValuesForInsert(transformacion));
			transformacion.setId(id);
			commitTransaction();
		} else {
			transformacion.setId(resultSaved.getId());
		}
		result = transformacion;
		return result;
	}
	
	public ImagenOrigen recuperarImagenOrigen(String nombre) {
		return imagenOrigenDao.buildObjectFromCursor(db.query(imagenOrigenDao.TABLA, imagenOrigenDao.COLUMNAS, 
				"nombre=?", new String[] {nombre}, null, null, imagenOrigenDao.ORDER_BY));
	}
	
	public ImagenOrigen asentarImagenOrigen(ImagenOrigen imagenOrigen) {
		ImagenOrigen result = null;
		ImagenOrigen resultSaved = recuperarImagenOrigen(imagenOrigen.getNombre());
		if (resultSaved == null) {
			iniciarTransaction();
			long id = db.insert(imagenOrigenDao.TABLA, null, imagenOrigenDao.buildValuesForInsert(imagenOrigen));
			imagenOrigen.setId(id);
			commitTransaction();
		} else {
			imagenOrigen.setId(resultSaved.getId());
		}
		result = imagenOrigen;
		return result;
	}
	
	public ImagenTransformada recuperarImagenTransformada(ImagenTransformada imagenTransformada) {
		return imagenTransformadaDao.buildObjectFromCursor(db.query(imagenTransformadaDao.TABLA, imagenTransformadaDao.COLUMNAS, 
				"id_imagen_origen=? AND id_transformacion=? AND json_caracteristicas=?", 
				new String[] {imagenTransformada.getImagenOrigen().getId().toString(),
					imagenTransformada.getTransformacion().getId().toString(),
					imagenTransformada.getJsonCaracteristicas()}, null, null, imagenTransformadaDao.ORDER_BY));
	}
	
	public ImagenTransformada asentarImagenTransformada(ImagenTransformada imagenTransformada) {
		ImagenTransformada result = null;
		ImagenTransformada resultSaved = recuperarImagenTransformada(imagenTransformada);
		if (resultSaved == null) {
			iniciarTransaction();
			long id = db.insert(imagenTransformadaDao.TABLA, null, imagenTransformadaDao.buildValuesForInsert(imagenTransformada));
			imagenTransformada.setId(id);
			commitTransaction();
		} else {
			imagenTransformada.setId(resultSaved.getId());
		}
		result = imagenTransformada;
		return result;
	}
	
	public Estadistica recuperarEstadisticaPorId(long idEstadistica) {
		return estadisticaDao.buildObjectFromCursor(db.query(estadisticaDao.TABLA, estadisticaDao.COLUMNAS,
				"_id=?",new String[] {Long.toString(idEstadistica)}, null, null, estadisticaDao.ORDER_BY));
	}

	public Estadistica recuperarEstadistica(Estadistica estadistica) {
		return estadisticaDao.buildObjectFromCursor(db.query(estadisticaDao.TABLA, estadisticaDao.COLUMNAS,
				"id_algoritmo=? AND id_transformacion=? AND id_imagen_origen=? AND id_imagen_transformada=?", 
				new String[] {estadistica.getAlgoritmo().getId().toString(), 
					estadistica.getTransformacion().getId().toString(),
					estadistica.getImagenOrigen().getId().toString(),
					estadistica.getImagenTransformada().getId().toString()}, null, null, estadisticaDao.ORDER_BY));
	}

	public Estadistica asentarEstadistica(Estadistica estadistica) {
		Estadistica result = null;
		if (recuperarEstadistica(estadistica) == null) {
			iniciarTransaction();
			long id = db.insert(estadisticaDao.TABLA, null, estadisticaDao.buildValuesForInsert(estadistica));
			estadistica.setId(id);
			commitTransaction();
		}
		result = estadistica;
		return result;
	}
	
	public void borrarSourceKeyPoints(long idEstadistica) {
		iniciarTransaction();
		db.delete(sourceKeyPointDao.TABLA, "id_estadistica=?", new String [] {Long.toString(idEstadistica)});
		commitTransaction();
	}
	
	public KeyPoint asentarSourceKeyPoint(KeyPoint keyPoint) {
		iniciarTransaction();
		long id = db.insert(sourceKeyPointDao.TABLA, null, sourceKeyPointDao.buildValuesForInsert(keyPoint));
		keyPoint.setId(id);
		commitTransaction();
		return keyPoint;
	}
	
	public List<KeyPoint> asentarSourceKeyPoints(Estadistica estadistica, List<KeyPoint> sourceKeyPoints) {
		if (sourceKeyPoints != null && !sourceKeyPoints.isEmpty()) {
			iniciarTransaction();
			ListIterator<KeyPoint> itKp = sourceKeyPoints.listIterator();
			while (itKp.hasNext()) {
				KeyPoint kp = itKp.next();
				kp.setEstadistica(estadistica);
				kp = asentarSourceKeyPoint(kp);
			}
			commitTransaction();
		}
		return sourceKeyPoints;
	}

	public List<KeyPoint> asentarSourceKeyPoints(long idEstadistica, List<KeyPoint> sourceKeyPoints) {
		List<KeyPoint> result = null;
		Estadistica estadistica = recuperarEstadisticaPorId(idEstadistica);
		if (estadistica != null) {
			iniciarTransaction();
			borrarSourceKeyPoints(idEstadistica);
			result = asentarSourceKeyPoints(estadistica, sourceKeyPoints);
			commitTransaction();
		}
		return result;
	}

	public void borrarTransformedKeyPoints(long  idEstadistica) {
		iniciarTransaction();
		db.delete(transformedKeyPointDao.TABLA, "id_estadistica=?", new String [] {Long.toString(idEstadistica)});
		commitTransaction();
	}
	
	public KeyPoint asentarTransformedKeyPoint(KeyPoint keyPoint) {
		iniciarTransaction();
		long id = db.insert(transformedKeyPointDao.TABLA, null, transformedKeyPointDao.buildValuesForInsert(keyPoint));
		keyPoint.setId(id);
		commitTransaction();
		return keyPoint;
	}
	
	public List<KeyPoint> asentarTransformedKeyPoints(Estadistica estadistica, List<KeyPoint> transformedKeyPoints) {
		if (transformedKeyPoints != null && !transformedKeyPoints.isEmpty()) {
			iniciarTransaction();
			ListIterator<KeyPoint> itKp = transformedKeyPoints.listIterator();
			while (itKp.hasNext()) {
				KeyPoint kp = itKp.next();
				kp.setEstadistica(estadistica);
				kp = asentarTransformedKeyPoint(kp);
			}
			commitTransaction();
		}
		return transformedKeyPoints;
	}

	public List<KeyPoint> asentarTransformedKeyPoints(long idEstadistica, List<KeyPoint> transformedKeyPoints) {
		List<KeyPoint> result = null;
		Estadistica estadistica = recuperarEstadisticaPorId(idEstadistica);
		if (estadistica != null) {
			iniciarTransaction();
			borrarTransformedKeyPoints(idEstadistica);
			result = asentarTransformedKeyPoints(estadistica, transformedKeyPoints);
			commitTransaction();
		}
		return result;
	}
}
