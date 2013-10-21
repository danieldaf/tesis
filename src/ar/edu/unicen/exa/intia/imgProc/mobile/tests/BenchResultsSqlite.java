package ar.edu.unicen.exa.intia.imgProc.mobile.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.net.Uri;
import android.os.Environment;
import ar.edu.unicen.exa.intia.imgProc.mobile.dao.BenchDao;
import ar.edu.unicen.exa.intia.imgProc.mobile.dao.MySQLiteOpenHelper;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.Estadistica;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenTransformada;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.KeyPoint;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.exceptions.ExecutionEvaluationException;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.input.MatImagen;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.BenchResults;
import ar.edu.unicen.exa.intia.imgProc.mobile.tests.output.StatisticResults;

public class BenchResultsSqlite extends BenchResults {

	protected static Random random = new Random(System.currentTimeMillis());
	
	public final static String DEFAULT_DB_NAME = "database-comparacion.sqlite";

	protected MySQLiteOpenHelper sqliteHelper;
	protected BenchDao dao;
	protected String fechaEjecucion;
	
	public BenchResultsSqlite(MySQLiteOpenHelper sqliteHelper) {
		this.sqliteHelper = sqliteHelper;
		this.dao = new BenchDao(this.sqliteHelper.getWritableDatabase());
	}
		
	@Override
	public void iniciar() {
		if (!dao.isOpen())
			this.dao = new BenchDao(this.sqliteHelper.getWritableDatabase());
		sqliteHelper.cleanTables();
		dao.iniciarTransaction();
		dao.asentarDeviceInfo();
		fechaEjecucion = dao.asentarEjecucion();
		crearOutFolder();
	}
	
	@Override
	public void finalizar() {
		dao.asentarFinEjecucion();
		dao.commitTransaction();
		dao.close();
		String state = Environment.getExternalStorageState();
		if (config.isExportarSqlite() && config.getFolderPathOutResourcesFull() != null && Environment.MEDIA_MOUNTED.equals(state)) {
			String dbOutFile = DEFAULT_DB_NAME;
			String dbIntFile = dao.getDbFileName();
			
			byte buffer[] = new byte[256];
			FileOutputStream out = null;
			FileInputStream in = null;
			try {
				File fout = new File(Environment.getExternalStorageDirectory()+config.getFolderPathOutResourcesFull()+File.separator+dbOutFile);
				if (!fout.exists()) {
					fout.createNewFile();
				}
				out = new FileOutputStream(fout);
				in = new FileInputStream(dbIntFile);
				int cc = in.read(buffer);
				while (cc != -1) {
					out.write(buffer);
					cc = in.read(buffer);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null)
						in.close();
					if (out != null)
						out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	protected Uri persistirSourceImagen(MatImagen img, String path) {
		Uri uri = null;
		String state = Environment.getExternalStorageState();
		//BroadcastReceiver para monitorear el estado del sdcard
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File dirAlbum = new File(Environment.getExternalStorageDirectory() + path);
			if (!dirAlbum.exists()) {
				dirAlbum.mkdirs();
			}
			if (dirAlbum.exists() && dirAlbum.isDirectory() && dirAlbum.canWrite()) {
				Mat tmpMat = new Mat();
				Imgproc.cvtColor(img.getMatImagen(), tmpMat, Imgproc.COLOR_RGB2BGR);
				
				String imgFileName = img.getName();
				int pp = imgFileName.lastIndexOf('.');
				if (pp != -1) {
					imgFileName = imgFileName.substring(0, pp) + ".png";
				}
				File fileImg = new File(dirAlbum.getAbsolutePath() + "/"+ imgFileName);
				if (!fileImg.exists()) {
//					MatOfInt params = new MatOfInt(Highgui.IMWRITE_PNG_COMPRESSION, 9);
					Highgui.imwrite(fileImg.getAbsolutePath(), tmpMat);
				}
				uri = Uri.fromFile(fileImg);
				tmpMat.release();
			}
		}
		return uri;
	}

	protected Uri persistirTransformedImagen(MatImagen sourceImg, String strId, Mat transformedImage, String path) {
		Uri uri = null;
		String state = Environment.getExternalStorageState();
		//BroadcastReceiver para monitorear el estado del sdcard
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			File dirAlbum = new File(Environment.getExternalStorageDirectory() + path);
			if (!dirAlbum.exists()) {
				dirAlbum.mkdirs();
			}
			if (dirAlbum.exists() && dirAlbum.isDirectory() && dirAlbum.canWrite()) {
				Mat tmpMat = new Mat();
				Imgproc.cvtColor(transformedImage, tmpMat, Imgproc.COLOR_RGB2BGR);

				String strNro = strId; //Long.toHexString(random.nextLong());
				String names[] = sourceImg.getName().split("\\.");
//				String imgFileName = names[0]+"_"+strNro+"."+names[1];
				String imgFileName = names[0]+"_"+strNro+".png";
				File fileImg = new File(dirAlbum.getAbsolutePath() + "/"+ imgFileName);
				if (fileImg.exists()) {
					fileImg.delete();
				}
				Highgui.imwrite(fileImg.getAbsolutePath(), tmpMat);
				uri = Uri.fromFile(fileImg);
				tmpMat.release();
			}
		}
		return uri;
	}

	@Override
	public void addStatsResult(StatisticResults statResult) throws ExecutionEvaluationException {
		AlgCombinacion algCombinacion = null;
		if (statResult.getAlgorithm() != null) {
			algCombinacion = ConversorClasesAnalogas.armarAlgoritmoAnalogo(statResult.getAlgorithm());
			algCombinacion = dao.asentarAlgCombinacion(algCombinacion);
		}

		AlgTransformacion algTransformacion = null;
		if (statResult.getTransformation() != null) {
			algTransformacion = ConversorClasesAnalogas.armarAlgTransformacionAnalogo(statResult.getTransformation());
			algTransformacion = dao.asentarAlgTransformacion(algTransformacion);
		}
		
		ImagenOrigen imgOrigen = null;
		if (statResult.getSourceImage() != null) {
			MatImagen sourceImage = statResult.getSourceImage();
			imgOrigen = new ImagenOrigen(null, sourceImage.getName(), sourceImage.getUri().toString(), null);
			if (config.isSaveSourceImages() && config.getFolderPathOutResourcesFull() != null) {
				Uri uri = persistirSourceImagen(sourceImage, config.getFolderPathOutResourcesFull());
				if (uri != null) {
					imgOrigen.setUri(uri.toString());
				}
			}
			imgOrigen = dao.asentarImagenOrigen(imgOrigen);
		}
		
		ImagenTransformada imgTransformada = null;
		if (statResult.getTransformedImage() != null && imgOrigen != null && algTransformacion != null) {
			JSONObject json = new JSONObject();
			String jsonCaracteristicas = null;
			String strVal = null;
			try {
				json.put("valueTransformation", statResult.getValueTransformation());
				jsonCaracteristicas = json.toString();
				strVal = json.optString("valueTransformation");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Uri uri = null;
			if (config.isSaveTransformedImages() && config.getFolderPathOutResourcesFull() != null) {
				uri = persistirTransformedImagen(statResult.getSourceImage(), strVal, statResult.getTransformedImage(), config.getFolderPathOutResourcesFull());
			}
			imgTransformada = new ImagenTransformada(null, imgOrigen, algTransformacion, jsonCaracteristicas, uri!=null?uri.toString():null);
			imgTransformada = dao.asentarImagenTransformada(imgTransformada);
		}
		
		Estadistica estadistica = null;
		estadistica = ConversorClasesAnalogas.armarEstadisticaAnaloga(statResult.getFrameStatics());
		estadistica.setAlgoritmo(algCombinacion);
		estadistica.setTransformacion(algTransformacion);
		estadistica.setImagenOrigen(imgOrigen);
		estadistica.setImagenTransformada(imgTransformada);
		estadistica = dao.asentarEstadistica(estadistica);
		
		if (config.isSaveSourceKeypoints() && statResult.getSourceKeypoints() != null) {
			List<KeyPoint> sourceKeyPoints = ConversorClasesAnalogas.armarKeyPointAnalogo(statResult.getSourceKeypoints());
			dao.asentarSourceKeyPoints(estadistica, sourceKeyPoints);
		}
		
		if (config.isSaveTransformedKeypoints() && statResult.getTransformedKeypoints() != null) {
			List<KeyPoint> transformedKeyPoints = ConversorClasesAnalogas.armarKeyPointAnalogo(statResult.getTransformedKeypoints());
			dao.asentarTransformedKeyPoints(estadistica, transformedKeyPoints);
		}
	}

}
