package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	private final static int DB_VERSION=1;
	protected Context context;
	
	public MySQLiteOpenHelper(Context context, String dbName) {
		super(context, dbName, null, DB_VERSION);
		this.context = context;
	}
	
	private void createTables(SQLiteDatabase db) {
		AssetManager am = context.getAssets();
		InputStream is;
		try {
			db.beginTransactionNonExclusive();
			is = am.open("sql/CreateTables.sql");
			Scanner sqlScanner = new Scanner(is, "UTF-8").useDelimiter(";");
			while (sqlScanner.hasNext()) {
				String sql = sqlScanner.next().trim();
				if (!sql.isEmpty() && !sql.startsWith("insert") && !sql.startsWith("udpate") && !sql.startsWith("delete"))
					db.execSQL(sql+";");
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void dropTables(SQLiteDatabase db) {
		AssetManager am = context.getAssets();
		InputStream is;
		try {
			db.beginTransactionNonExclusive();
//			if (BuildConfig.DEBUG) {
//				String test = ".tables;";
//				Cursor cur = db.rawQuery(test, null);
//				cur.moveToFirst();
//				int cc = cur.getCount();
//				cur.close();
//				if (cc == 0) {
//					return;
//				}
//			}
			is = am.open("sql/DropTables.sql");
			Scanner sqlScanner = new Scanner(is, "UTF-8").useDelimiter(";");
			while (sqlScanner.hasNext()) {
				String sql = sqlScanner.next();
				if (!sql.isEmpty() && !sql.startsWith("insert") && !sql.startsWith("udpate") && !sql.startsWith("delete"))
					db.execSQL(sql+";");
			} 
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

//	public void cleanTables() {
//		SQLiteDatabase db = this.getWritableDatabase();
//		AssetManager am = context.getAssets();
//		InputStream is;
//		try {
//			db.beginTransactionNonExclusive();
//			is = am.open("sql/CleanTables.sql");
//			Scanner sqlScanner = new Scanner(is, "UTF-8").useDelimiter(";");
//			while (sqlScanner.hasNext()) {
//				String sql = sqlScanner.next();
//				db.execSQL(sql+";");
//			}
//			db.setTransactionSuccessful();
//			db.endTransaction();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
	public void cleanTables() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.beginTransactionNonExclusive();
		db.delete("parametro", "nombre <> ?", new String[] {"DB_VERSION"});
		db.delete("imagen_origen", null, null);
		db.delete("algoritmo", null, null);
		db.delete("transformacion", null, null);
		db.delete("imagen_transformada", null, null);
		db.delete("estadistica", null, null);
		db.delete("source_keypoint", null, null);
		db.delete("transformed_keypoint", null, null);
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
//		if (BuildConfig.DEBUG)
//			dropTables(db);
		createTables(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		createTables(db);
	}

}
