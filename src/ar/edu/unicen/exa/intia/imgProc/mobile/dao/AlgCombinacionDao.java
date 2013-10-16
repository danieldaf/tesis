package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgDeteccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgExtraccion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgMatching;

public class AlgCombinacionDao extends DtoDao<AlgCombinacion>{
	
	public final String TABLA = "algoritmo";
	
	public final String[] COLUMNAS = {"_id", "nombre", "nombre_detector", "nombre_extractor", "nombre_matcher"};
	
	public final String ORDER_BY = "_id ASC, nombre ASC, nombre_detector ASC, nombre_extractor ASC, nombre_matcher ASC";
	
	@Override
	protected AlgCombinacion build(Cursor cursor) {
		AlgDeteccion detector = AlgDeteccion.fromNombre(cursor.getString(2));
		AlgExtraccion extractor = AlgExtraccion.fromNombre(cursor.getString(3));
		AlgMatching matcher = AlgMatching.fromNombre(cursor.getString(4));
		AlgCombinacion result = new AlgCombinacion(cursor.getLong(0), cursor.getString(1), detector, extractor, matcher);
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(AlgCombinacion item) {
		ContentValues valores = new ContentValues();
		valores.put("nombre", item.getNombre());
		valores.put("nombre_detector", item.getDetector()!=null?item.getDetector().getNombre():null);
		valores.put("nombre_extractor", item.getExtractor()!=null?item.getExtractor().getNombre():null);
		valores.put("nombre_matcher", item.getMatcher()!=null?item.getMatcher().getNombre():null);
		return valores;
	}

	@Override
	public ContentValues buildValues(AlgCombinacion item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("nombre", item.getNombre());
		valores.put("nombre_detector", item.getDetector()!=null?item.getDetector().getNombre():null);
		valores.put("nombre_extractor", item.getExtractor()!=null?item.getExtractor().getNombre():null);
		valores.put("nombre_matcher", item.getMatcher()!=null?item.getMatcher().getNombre():null);
		return valores;
	}
}
