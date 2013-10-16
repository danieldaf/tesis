package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenTransformada;

public class ImagenTransformadaDao extends DtoDao<ImagenTransformada> {
	
	public final String TABLA = "imagen_transformada";

	public final String[] COLUMNAS = {"_id", "id_imagen_origen", "id_transformacion", "json_caracteristicas", "uri"};
	
	public final String ORDER_BY = "_id ASC, id_imagen_origen ASC, id_transformacion ASC, json_caracteristicas ASC, uri ASC";
	
	@Override
	protected ImagenTransformada build(Cursor cursor) {
		ImagenOrigen imgOrigen = new ImagenOrigen(cursor.getLong(1));
		AlgTransformacion transformacion = new AlgTransformacion(cursor.getLong(2));
		ImagenTransformada result = new ImagenTransformada(cursor.getLong(0), imgOrigen, transformacion, cursor.getString(3), cursor.getString(4));
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(ImagenTransformada item) {
		ContentValues valores = new ContentValues();
		valores.put("id_imagen_origen", item.getImagenOrigen()!=null?item.getImagenOrigen().getId():null);
		valores.put("id_transformacion", item.getTransformacion()!=null?item.getTransformacion().getId():null);
		valores.put("json_caracteristicas", item.getJsonCaracteristicas());
		valores.put("uri", item.getUri());
		return valores;
	}

	@Override
	public ContentValues buildValues(ImagenTransformada item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("id_imagen_origen", item.getImagenOrigen()!=null?item.getImagenOrigen().getId():null);
		valores.put("id_transformacion", item.getTransformacion()!=null?item.getTransformacion().getId():null);
		valores.put("json_caracteristicas", item.getJsonCaracteristicas());
		valores.put("uri", item.getUri());
		return valores;
	}
}
