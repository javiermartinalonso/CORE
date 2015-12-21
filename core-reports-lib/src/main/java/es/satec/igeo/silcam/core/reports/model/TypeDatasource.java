package es.satec.igeo.silcam.core.reports.model;

public enum TypeDatasource
{
	JNDI_CONNECTION ("objeto q representa la conexion con el origen de datos"), 
	JDBC ("Conexion directa a bbdd") , 
	JAVABEANS ("Coleccion de beans que representan la información"), 
	EMPTY ("Sin datos");
//	HIBERNATE_CONNECTION ("Conexion a través de objeto de hibernate"), 
//	JSON ("representacion JSON de la informacion");
	
	private String descripcion;
	
	private TypeDatasource(String descripcion)
	{
		this.descripcion = descripcion;
	}

	public String getDescripcion()
	{
		return descripcion;
	}
}
