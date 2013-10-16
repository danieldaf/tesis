package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;

public class ImagenOrigenDao extends DtoDao<ImagenOrigen> {
	
	public final String TABLA = "imagen_origen";
	
	public final String[] COLUMNAS = {"_id", "nombre", "uri_original", "uri"};
	
	public final String ORDER_BY = "_id ASC, nombre ASC, uri ASC";
	
	@Override
	protected ImagenOrigen build(Cursor cursor) {
		ImagenOrigen result = new ImagenOrigen(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(ImagenOrigen item) {
		ContentValues valores = new ContentValues();
		valores.put("nombre", item.getNombre());
		valores.put("uri", item.getUri());
		valores.put("uri_original", item.getOriginalUri());
		return valores;
	}

	@Override
	public ContentValues buildValues(ImagenOrigen item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("nombre", item.getNombre());
		valores.put("uri", item.getUri());
		valores.put("uri_original", item.getOriginalUri());
		return valores;
	}
}
