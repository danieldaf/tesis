package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacionEnum;

public class AlgTransformacionDao extends DtoDao<AlgTransformacion> {
	
	public final String TABLA = "transformacion";

	public final String[] COLUMNAS = {"_id", "nombre", "json_caracteristicas"};

	public final String ORDER_BY = "_id ASC, nombre ASC";
	
	@Override
	protected AlgTransformacion build(Cursor cursor) {
		AlgTransformacion result = new AlgTransformacion(cursor.getLong(0), 
				AlgTransformacionEnum.valueFromNombre(cursor.getString(1)), cursor.getString(2));
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(AlgTransformacion item) {
		ContentValues valores = new ContentValues();
		valores.put("nombre", item.getTipo().getNombre());
		valores.put("json_caracteristicas", item.getJsonCaracteristicas());
		return valores;
	}

	@Override
	public ContentValues buildValues(AlgTransformacion item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("nombre", item.getTipo().getNombre());
		valores.put("json_caracteristicas", item.getJsonCaracteristicas());
		return valores;
	}
}