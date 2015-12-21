package es.satec.igeo.silcam.core.reports.model;

import java.sql.Connection;

public interface DataSourcePlantilla
{
	public TypeDatasource getTypeDatasource();

	public Connection getDataSource();
}