package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;

public abstract class DtoDao<T> {

	protected abstract T build(Cursor cursor);
	
	public T buildObjectFromCursor(Cursor cursor) {
		T result = null;
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				result = build(cursor);
			}
		}
		return result;
	}
	
	public List<T> buildListFromCursor(Cursor cursor) {
		List<T> result = new ArrayList<T>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					result.add(build(cursor));
				} while (cursor.moveToNext());
			}
		}
		return result;
	}
	
	public abstract ContentValues buildValuesForInsert(T item);
	
	public abstract ContentValues buildValues(T item);

}
