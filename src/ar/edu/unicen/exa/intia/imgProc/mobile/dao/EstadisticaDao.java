package ar.edu.unicen.exa.intia.imgProc.mobile.dao;

import android.content.ContentValues;
import android.database.Cursor;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgCombinacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.AlgTransformacion;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.Estadistica;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenOrigen;
import ar.edu.unicen.exa.intia.imgProc.mobile.dto.ImagenTransformada;

public class EstadisticaDao extends DtoDao<Estadistica> {
	
	public final String TABLA = "estadistica";
	
	public final String[] COLUMNAS = {"_id", "id_algoritmo", "id_transformacion", "id_imagen_origen", "id_imagen_transformada", 
		"total_keypoints", "argument_value", "percent_of_matches", "ratio_test_false_level", "mean_distance", "std_dev_distance",
		"correct_matches_percent", "homography_error", "consumed_time_ms", "consumed_time_ms_detector","consumed_time_ms_extractor",
		"consumed_time_ms_matcher","distancia_media_proyecciones", "desviacion_estandar_proyecciones",
		"distancia_minima_proyecciones", "distancia_maxima_proyecciones", "is_valid"};
	
	public final String ORDER_BY = "_id ASC, id_algoritmo ASC, id_transformacion ASC, id_imagen_origen ASC, id_imagen_transformada ASC, argument_value ASC";
	
	@Override
	protected Estadistica build(Cursor cursor) {
		AlgCombinacion algoritmo = new AlgCombinacion(cursor.getLong(1));
		AlgTransformacion transformacion = new AlgTransformacion(cursor.getLong(2));
		ImagenOrigen imagenOrigen = new ImagenOrigen(cursor.getLong(3));
		ImagenTransformada imagenTransformada = new ImagenTransformada(cursor.getLong(4));
		Estadistica result = new Estadistica(cursor.getLong(0), algoritmo, transformacion, imagenOrigen, imagenTransformada);

		result.setTotalKeypoints(cursor.getInt(5));
		result.setArgumentValue(cursor.getDouble(6));
		result.setPercentOfMatches(cursor.getDouble(7));
		result.setRatioTestFalseLevel(cursor.getDouble(8));
		result.setMeanDistance(cursor.getDouble(9));
		result.setStdDevDistance(cursor.getDouble(10));
		result.setCorrectMatchesPercent(cursor.getDouble(11));
		result.setHomographyError(cursor.getDouble(12));
		result.setConsumedTimeMs(cursor.getDouble(13));
		result.setConsumedTimeMsDetector(cursor.getDouble(14));
		result.setConsumedTimeMsExtractor(cursor.getDouble(15));
		result.setConsumedTimeMsMatcher(cursor.getDouble(16));
		result.setDistanciaMediaProyecciones(cursor.getDouble(17));
		result.setDesviacionEstandarProyecciones(cursor.getDouble(18));
		result.setDistanciaMinimaProyecciones(cursor.getDouble(19));
		result.setDistanciaMaximaProyecciones(cursor.getDouble(20));
		result.setIsValid(cursor.getInt(21)!=0);
		return result;
	}
	
	@Override
	public ContentValues buildValuesForInsert(Estadistica item) {
		ContentValues valores = new ContentValues();
		valores.put("id_algoritmo", item.getAlgoritmo()!=null?item.getAlgoritmo().getId():null);
		valores.put("id_transformacion", item.getTransformacion()!=null?item.getTransformacion().getId():null);
		valores.put("id_imagen_origen", item.getImagenOrigen()!=null?item.getImagenOrigen().getId():null);
		valores.put("id_imagen_transformada", item.getImagenTransformada()!=null?item.getImagenTransformada().getId():null);

		valores.put("total_keypoints", item.getTotalKeypoints());
		valores.put("argument_value", item.getArgumentValue());
		valores.put("percent_of_matches", item.getPercentOfMatches());
		valores.put("ratio_test_false_level", item.getRatioTestFalseLevel());
		valores.put("mean_distance", item.getMeanDistance());
		valores.put("std_dev_distance", item.getStdDevDistance());
		valores.put("correct_matches_percent", item.getCorrectMatchesPercent());
		valores.put("homography_error", item.getHomographyError());
		valores.put("consumed_time_ms", item.getConsumedTimeMs());
		valores.put("consumed_time_ms_detector", item.getConsumedTimeMsDetector());
		valores.put("consumed_time_ms_extractor", item.getConsumedTimeMsExtractor());
		valores.put("consumed_time_ms_matcher", item.getConsumedTimeMsMatcher());
		valores.put("distancia_media_proyecciones", item.getDistanciaMediaProyecciones());
		valores.put("desviacion_estandar_proyecciones", item.getDesviacionEstandarProyecciones());
		valores.put("distancia_minima_proyecciones", item.getDistanciaMinimaProyecciones());
		valores.put("distancia_maxima_proyecciones", item.getDistanciaMaximaProyecciones());
		valores.put("is_valid", item.getIsValid()?1:0);
		return valores;
	}

	@Override
	public ContentValues buildValues(Estadistica item) {
		ContentValues valores = new ContentValues();
		valores.put("_id", item.getId());
		valores.put("id_algoritmo", item.getAlgoritmo()!=null?item.getAlgoritmo().getId():null);
		valores.put("id_transformacion", item.getTransformacion()!=null?item.getTransformacion().getId():null);
		valores.put("id_imagen_origen", item.getImagenOrigen()!=null?item.getImagenOrigen().getId():null);
		valores.put("id_imagen_transformada", item.getImagenTransformada()!=null?item.getImagenTransformada().getId():null);

		valores.put("total_keypoints", item.getTotalKeypoints());
		valores.put("argument_value", item.getArgumentValue());
		valores.put("percent_of_matches", item.getPercentOfMatches());
		valores.put("ratio_test_false_level", item.getRatioTestFalseLevel());
		valores.put("mean_distance", item.getMeanDistance());
		valores.put("std_dev_distance", item.getStdDevDistance());
		valores.put("correct_matches_percent", item.getCorrectMatchesPercent());
		valores.put("homography_error", item.getHomographyError());
		valores.put("consumed_time_ms", item.getConsumedTimeMs());
		valores.put("consumed_time_ms_detector", item.getConsumedTimeMsDetector());
		valores.put("consumed_time_ms_extractor", item.getConsumedTimeMsExtractor());
		valores.put("consumed_time_ms_matcher", item.getConsumedTimeMsMatcher());
		valores.put("distancia_media_proyecciones", item.getDistanciaMediaProyecciones());
		valores.put("desviacion_estandar_proyecciones", item.getDesviacionEstandarProyecciones());
		valores.put("distancia_minima_proyecciones", item.getDistanciaMinimaProyecciones());
		valores.put("distancia_maxima_proyecciones", item.getDistanciaMaximaProyecciones());
		valores.put("is_valid", item.getIsValid()?1:0);
		return valores;
	}
}
