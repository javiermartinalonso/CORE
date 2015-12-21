package es.satec.igeo.silcam.core.reports.manager.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import bussines.DataBean;
import bussines.DataBeanMaker;
import es.satec.igeo.silcam.core.reports.manager.ReportManager;
import es.satec.igeo.silcam.core.reports.model.DataSourcePlantilla;
import es.satec.igeo.silcam.core.reports.model.ExtensionFicheros;
import es.satec.igeo.silcam.core.reports.model.Informe;
import es.satec.igeo.silcam.core.reports.model.Plantilla;
import es.satec.igeo.silcam.core.reports.model.impl.DataSourcePlantillaJasper;
import es.satec.igeo.silcam.core.reports.model.impl.PlantillaJasper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;

public class ReportManagerJasperImpl implements ReportManager
{

	public static void main(String[] args)
	{

		ReportManagerJasperImpl reportManagerJasperImpl = new ReportManagerJasperImpl();
		
		DataBeanMaker dataBeanMaker = new DataBeanMaker();
		ArrayList<DataBean> dataBeanList = dataBeanMaker.getDataBeanList();

		
		DataSourcePlantilla dataSourcePlantilla = new DataSourcePlantillaJasper(dataBeanList);
		
		ClassLoader classLoader = ClassLoader.getSystemClassLoader();
		File file = new File(classLoader.getResource("reports/test_jasper.jrxml").getFile());

		Plantilla plantilla = null;
		Informe informe1 = new Informe("test_jasper1", ExtensionFicheros.PDF, "c:/reports/");
		Informe informe2 = new Informe("test_jasper2", ExtensionFicheros.PDF, "c:/reports/");

		try
		{
			plantilla = new PlantillaJasper(file);

			System.out.println("\nParámetros: " + plantilla.getParametros().toString());
		}
		catch (JRException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		reportManagerJasperImpl.generateReportFile(dataBeanList, plantilla, null, informe1);
	
		reportManagerJasperImpl.generateReportFile(dataSourcePlantilla, plantilla, null, informe2);

		
		
		
		
		
		
		Plantilla plantillaMinerales = null;
		
		try
		{
			File fileMinerales = new File(classLoader.getResource("reports/minerales.jrxml").getFile());
			
			plantillaMinerales = new PlantillaJasper(fileMinerales.getAbsoluteFile());
			
			System.out.println("\nParámetros: " + plantillaMinerales.getParametros().toString());
		}
		catch (JRException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Informe informeMinerales = new Informe("minerales", ExtensionFicheros.PDF, "c:/reports/");
		
		Map<String, Object> parametrosMinerales = new HashMap<String, Object>();
		parametrosMinerales.put("provincia", "Moxico");
		
		try
		{
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://silcam.igeo.satec.es:5432/silcam","igeo_informes", "igeo_informes");

			DataSourcePlantilla dataSourcePlantillaMinerales = new DataSourcePlantillaJasper(connection);
			
			reportManagerJasperImpl.generateReportFile(dataSourcePlantillaMinerales, plantillaMinerales, parametrosMinerales, informeMinerales);
			
			connection.close();
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void generateReportFile(Collection<?> collectionDataSource, Plantilla plantilla, Map<String, Object> parameters, Informe informe)
	{
		try
		{
			JRBeanCollectionDataSource beanColDataSource = new JRBeanCollectionDataSource(collectionDataSource);

			JasperDesign jasperDesign = ((PlantillaJasper) plantilla).getPlantillaJasperDesign();
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);

			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, beanColDataSource);

			generateReportFile(informe, jasperPrint);

		}
		catch (JRException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	@Override
	public void generateReportFile(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe)
	{
		try
		{
			JasperDesign jasperDesign = ((PlantillaJasper) plantilla).getPlantillaJasperDesign();
			JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
			JasperPrint jasperPrint = null;
			
			switch (dataSourcePlantilla.getTypeDatasource())
			{
				case JDBC:
				case JNDI_CONNECTION:
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSourcePlantilla.getDataSource());
				break;
				case JAVABEANS:
				default:
					jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, ((DataSourcePlantillaJasper) dataSourcePlantilla).getJRDataSource());
				break;
			}
			
			generateReportFile(informe, jasperPrint);

		}
		catch (JRException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	/**
	 * @param informe
	 * @param jasperPrint
	 * @throws JRException
	 */
	public static void generateReportFile(Informe informe, JasperPrint jasperPrint) throws JRException
	{
		switch (informe.getExtension())
		{
			case HTML:
				JasperExportManager.exportReportToHtmlFile(jasperPrint, informe.getFullPath());
			break;
			case RTF:

			break;
			case CSV:

			break;
			case TXT:

			break;
			case XML:
				JasperExportManager.exportReportToXmlFile(jasperPrint, informe.getFullPath(), true);
			break;
			case PDF:
				JasperExportManager.exportReportToPdfFile(jasperPrint, informe.getFullPath());
			break;
			case XLS:

			break;
			default:
			break;
		}
	}

}
