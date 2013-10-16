package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.Estadistica;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.KeyPoint;

public class SourceKeyPointDao extends DtoDao<KeyPoint> {
	
	public final String TABLA = "source_keypoint";
	
	public final String[] COLUMNAS = {"_id", "id_estadistica", "x", "y", "size", "angle", "response", "octave", "class_id"};
	
	public final String ORDER_BY = "_id ASC, id_estadistica ASC";
	
	@Override
	protected KeyPoint build(Cursor cursor) {
		Estadistica estadistica = new Estadistica(cursor.getLong(1));
		KeyPoint result = new KeyPoint(cursor.getLong(0), estadistica, 
				cursor.getDouble(2), cursor.getDouble(3), cursor.getDouble(4),
				cursor.getDouble(5), cursor.getDouble(6), cursor.getInt(7), cursor.getInt(8));
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(KeyPoint item) {
		ContentValues valores = new ContentValues();
		valores.put("id_estadistica", item.getEstadistica()!=null?item.getEstadistica().getId():null);
		valores.put("x", item.getX());
		valores.put("y", item.getY());
		valores.put("size", item.getSize());
		valores.put("angle", item.getAngle());
		valores.put("response", item.getResponse());
		valores.put("octave", item.getOctave());
		valores.put("class_id", item.getClassId());
		return valores;
	}

	@Override
	public ContentValues buildValues(KeyPoint item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("id_estadistica", item.getEstadistica()!=null?item.getEstadistica().getId():null);
		valores.put("x", item.getX());
		valores.put("y", item.getY());
		valores.put("size", item.getSize());
		valores.put("angle", item.getAngle());
		valores.put("response", item.getResponse());
		valores.put("octave", item.getOctave());
		valores.put("class_id", item.getClassId());
		return valores;
	}
}
