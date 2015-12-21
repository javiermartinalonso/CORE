package es.satec.igeo.silcam.core.reports.model.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import es.satec.igeo.silcam.core.reports.model.DataSourcePlantilla;
import es.satec.igeo.silcam.core.reports.model.TypeDatasource;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

public class DataSourcePlantillaJasper implements DataSourcePlantilla
{

	//	TIPO DE DATASOURCE QUE DEFINIMOS
	private TypeDatasource typeDatasource;

	//	DATASOURCE SE OBTIENE A TRAVES DE UNA CONEXION ESTABLECIDA EN EL ESPACIO DE NOMBRES
	//	private DataSource		jndiDatasource;
	//	COLECCION DE DATOS QUE ALIMENTA EL INFORME
	//	private Collection<?>	collectionDataSource;
	
	//	DATASOURCE SI ATACA DIRECTAMENTE A LA BBDD
		private Connection		connectionDataSource;


	//
	//
	/*
	 * INTERFAZ QUE GESTIONA EL ORIGEN DE DATOS DE UN JASPERREPORT MAS INFO
	 * http://jasperreports.sourceforge.net/api/net/sf/jasperreports/engine/
	 * JRDataSource.html All Known Subinterfaces: IndexedDataSource,
	 * JRRewindableDataSource, JsonData All Known Implementing Classes:
	 * AbstractPoiXlsDataSource, AbstractXlsDataSource, AbstractXmlDataSource,
	 * BookmarksFlatDataSource, ColumnValuesDataSource, DataSourceCollection,
	 * ExcelDataSource, JaxenXmlDataSource, JRAbstractBeanDataSource,
	 * JRAbstractTextDataSource, JRBeanArrayDataSource,
	 * JRBeanCollectionDataSource, JRCsvDataSource, JREmptyDataSource,
	 * JRHibernateAbstractDataSource, JRHibernateIterateDataSource,
	 * JRHibernateListDataSource, JRHibernateScrollDataSource, JRJpaDataSource,
	 * JRMapArrayDataSource, JRMapCollectionDataSource, JRMondrianDataSource,
	 * JROlapDataSource, JRResultSetDataSource, JRSortableDataSource,
	 * JRTableModelDataSource, JRXlsDataSource, JRXlsxDataSource,
	 * JRXmlDataSource, JsonDataCollection, JsonDataSource,
	 * ListOfArrayDataSource, Olap4jDataSource, RewindableDataSourceCollection,
	 * SortedDataSource, XalanXmlDataSource, XlsDataSource
	 */
	private JRDataSource jrDataSource;


	public DataSourcePlantillaJasper(Collection<?> collectionDataSource)
	{
		super();
		this.typeDatasource = TypeDatasource.JAVABEANS;
		this.jrDataSource = new JRBeanCollectionDataSource(collectionDataSource);
		this.connectionDataSource = null;

		//		TypeDatasource.EMPTY;
		//		TypeDatasource.HIBERNATE_CONNECTION;
		//		TypeDatasource.JAVABEANS;
		//		TypeDatasource.JDBC;
		//		TypeDatasource.JNDI_CONNECTION;
		//		TypeDatasource.JSON;
	}


	public DataSourcePlantillaJasper(Connection connectionDataSource)
	{
		super();
		this.typeDatasource = TypeDatasource.JDBC;
		this.connectionDataSource = connectionDataSource;
		this.jrDataSource = null;
	}


	public DataSourcePlantillaJasper(DataSource dataSource)
	{
		super();

		try
		{
			this.connectionDataSource = dataSource.getConnection();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.typeDatasource = TypeDatasource.JDBC;
		this.jrDataSource = null;
	}


	public DataSourcePlantillaJasper(String dataSourceContext)
	{
		super();
		this.typeDatasource = TypeDatasource.JNDI_CONNECTION;
		this.connectionDataSource = getJNDIConnection(dataSourceContext);
		this.jrDataSource = null;
	}


	public DataSourcePlantillaJasper()
	{
		super();
		this.typeDatasource = TypeDatasource.EMPTY;
		this.connectionDataSource = null;
		this.jrDataSource = new JREmptyDataSource();
	}


	/* (non-Javadoc)
	 * @see es.satec.igeo.silcam.core.reports.model.DataSourcePlantilla#getJRDataSource()
	 */
	public JRDataSource getJRDataSource()
	{
		return jrDataSource;
	}


	@Override
	public Connection getDataSource()
	{
		return connectionDataSource;
	}


	@Override
	public TypeDatasource getTypeDatasource()
	{
		return typeDatasource;
	}


	/** Uses JNDI and Datasource (preferred style). */
	private Connection getJNDIConnection(final String DATASOURCE_CONTEXT)
	{
		//		String DATASOURCE_CONTEXT = "java:comp/env/jdbc/blah";

		Connection result = null;
		try
		{
			Context initialContext = new InitialContext();
			if (initialContext == null)
			{
				//				log("JNDI problem. Cannot get InitialContext.");

			}

			DataSource datasource = (DataSource) initialContext.lookup(DATASOURCE_CONTEXT);
			if (datasource != null)
			{
				result = datasource.getConnection();
			}
			else
			{
				//				log("Failed to lookup datasource.");
			}
		}
		catch (NamingException ex)
		{
			//			log("Cannot get connection: " + ex);
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		catch (SQLException ex)
		{
			//			log("Cannot get connection: " + ex);
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return result;
	}
}
