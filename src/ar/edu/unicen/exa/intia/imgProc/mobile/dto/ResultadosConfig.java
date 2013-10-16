package ar.edu.unicen.exa.intia.imgProc.mobile.dto;

import java.io.File;

public class ResultadosConfig {

	protected String folderPathOutResourcesBase = "/TesisFuentesRossi2013";
	protected String folderPathOutResources = "";

	protected boolean saveSourceImages = false;
	protected boolean saveTransformedImages = false;

	protected boolean saveSourceKeypoints = false;
	protected boolean saveTransformedKeypoints = false;

	protected boolean exportarSqlite = false;

	/**
	 * Setea los parametros por defecto para registrar unicamente los valores
	 * estaditicos
	 */
	public ResultadosConfig() {
	}

	/**
	 * Activa o desactiva todos los parametros de configuracion
	 * 
	 * @param activarTodo
	 *            boolean
	 */
	public ResultadosConfig(boolean activarTodo) {
		saveSourceImages = activarTodo;
		saveTransformedImages = activarTodo;

		saveSourceKeypoints = activarTodo;
		saveTransformedKeypoints = activarTodo;

		exportarSqlite = activarTodo;
	}

	public boolean isSaveSourceImages() {
		return saveSourceImages;
	}

	public void setSaveSourceImages(boolean saveSourceImages) {
		this.saveSourceImages = saveSourceImages;
	}

	public boolean isSaveTransformedImages() {
		return saveTransformedImages;
	}

	public void setSaveTransformedImages(boolean saveTransformedImages) {
		this.saveTransformedImages = saveTransformedImages;
	}

	public boolean isSaveSourceKeypoints() {
		return saveSourceKeypoints;
	}

	public void setSaveSourceKeypoints(boolean saveSourceKeypoints) {
		this.saveSourceKeypoints = saveSourceKeypoints;
	}

	public boolean isSaveTransformedKeypoints() {
		return saveTransformedKeypoints;
	}

	public void setSaveTransformedKeypoints(boolean saveTransformedKeypoints) {
		this.saveTransformedKeypoints = saveTransformedKeypoints;
	}

	public String getFolderPathOutResourcesFull() {
		return folderPathOutResourcesBase+File.separator+folderPathOutResources;
	}

	public String getFolderPathOutResources() {
		return folderPathOutResources;
	}

	public String getFolderPathOutResourcesBase() {
		return folderPathOutResourcesBase;
	}

	public void setFolderPathOutResources(String folderPathOutResources) {
		this.folderPathOutResources = folderPathOutResources;
	}

	public boolean isExportarSqlite() {
		return exportarSqlite;
	}

	public void setExportarSqlite(boolean exportarSqlite) {
		this.exportarSqlite = exportarSqlite;
	}

}
