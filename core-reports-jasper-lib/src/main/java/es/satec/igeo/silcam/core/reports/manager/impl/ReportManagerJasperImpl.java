package es.satec.igeo.silcam.core.reports.manager.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
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
import es.satec.igeo.silcam.core.reports.model.ExtensionInformes;
import es.satec.igeo.silcam.core.reports.model.Informe;
import es.satec.igeo.silcam.core.reports.model.Plantilla;
import es.satec.igeo.silcam.core.reports.model.impl.DataSourcePlantillaJasper;
import es.satec.igeo.silcam.core.reports.model.impl.PlantillaJasper;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.WriterExporterOutput;

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
		Informe informePDF = new Informe("test_jasper1", ExtensionInformes.PDF, "c:/reports/");
		Informe informeHTML = new Informe("test_jasper2", ExtensionInformes.HTML, "c:/reports/");
		Informe informeRTF = new Informe("test_jasper2", ExtensionInformes.RTF, "c:/reports/");
		Informe informeWORD = new Informe("test_jasper2", ExtensionInformes.WORD, "c:/reports/");
		Informe informeCSV = new Informe("test_jasper2", ExtensionInformes.CSV, "c:/reports/");
		Informe informeXML = new Informe("test_jasper2", ExtensionInformes.XML, "c:/reports/");
		Informe informeXLS = new Informe("test_jasper2", ExtensionInformes.XLS, "c:/reports/");
		

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
		
		reportManagerJasperImpl.generateReportFile(dataBeanList, plantilla, null, informePDF);
		reportManagerJasperImpl.generateReportFile(dataSourcePlantilla, plantilla, null, informeHTML);
		reportManagerJasperImpl.generateReportFile(dataBeanList, plantilla, null, informeRTF);
		reportManagerJasperImpl.generateReportFile(dataBeanList, plantilla, null, informeWORD);
		reportManagerJasperImpl.generateReportFile(dataSourcePlantilla, plantilla, null, informeCSV);
		reportManagerJasperImpl.generateReportFile(dataBeanList, plantilla, null, informeXML);
		reportManagerJasperImpl.generateReportFile(dataSourcePlantilla, plantilla, null, informeXLS);
		
		
		
		
		
		
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
		
		Informe informeMinerales = new Informe("minerales", ExtensionInformes.PDF, "c:/reports/");
		
		Informe informeMineralesXLS = new Informe("minerales", ExtensionInformes.XLS, "c:/reports/");
		
		Map<String, Object> parametrosMinerales = new HashMap<String, Object>();
		parametrosMinerales.put("provincia", "Moxico");
		
		try
		{
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://silcam.igeo.satec.es:5432/silcam","igeo_informes", "igeo_informes");

			DataSourcePlantilla dataSourcePlantillaMinerales = new DataSourcePlantillaJasper(connection);
			
			reportManagerJasperImpl.generateReportFile(dataSourcePlantillaMinerales, plantillaMinerales, parametrosMinerales, informeMinerales);
			
			reportManagerJasperImpl.generateReportFile(dataSourcePlantillaMinerales, plantillaMinerales, parametrosMinerales, informeMineralesXLS);
			
			
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
	
	
	

	public void generateReportOutputStream(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe, OutputStream outputStream)
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
			
			generateReportOutputStream(informe, jasperPrint, outputStream);

		}
		catch (JRException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	public byte[] generateReportByte(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe)
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
			
			return generateReportByte(informe, jasperPrint);

		}
		catch (JRException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	/**
	 * @param informe
	 * @param jasperPrint
	 * @throws JRException
	 */
	public static void generateReportFile(Informe informe, JasperPrint jasperPrint) throws JRException
	{
		
		final String destFile = informe.getFullPath();
		 		
		switch (informe.getExtension())
		{
			case HTML:
				JasperExportManager.exportReportToHtmlFile(jasperPrint, destFile);
			break;
			case RTF:
			case WORD:
				JRRtfExporter exporterJRRtf = new JRRtfExporter();
				
				exporterJRRtf.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterJRRtf.setExporterOutput((WriterExporterOutput) new SimpleWriterExporterOutput(destFile));
				
				exporterJRRtf.exportReport();
			break;
			case CSV:
				JRCsvExporter exporterJRCsv = new JRCsvExporter();
				
				exporterJRCsv.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterJRCsv.setExporterOutput(new SimpleWriterExporterOutput(destFile));
				//exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "|");

				exporterJRCsv.exportReport();
			break;
			case XML:
				JasperExportManager.exportReportToXmlFile(jasperPrint, destFile, false);
			break;
			case PDF:
				JasperExportManager.exportReportToPdfFile(jasperPrint, destFile);
			break;
			case XLS:
				JRXlsExporter exporterJRXls = new JRXlsExporter();
				
				exporterJRXls.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterJRXls.setExporterOutput(new SimpleOutputStreamExporterOutput(destFile));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				
				configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
				configuration.setCollapseRowSpan(Boolean.TRUE);
				configuration.setRemoveEmptySpaceBetweenColumns(Boolean.FALSE);
				configuration.setWhitePageBackground(Boolean.FALSE);
				configuration.setIgnoreCellBackground(Boolean.TRUE);
				
				exporterJRXls.setConfiguration(configuration);
				
				exporterJRXls.exportReport();

			break;
		
			default:
			break;
		}
	}
	
	
	
	
	
	
	
	
	/**
	 * @param informe
	 * @param jasperPrint
	 * @throws JRException
	 */
	public static void generateReportOutputStream(Informe informe, JasperPrint jasperPrint, OutputStream outputStream) throws JRException
	{				 		
		switch (informe.getExtension())
		{
			case HTML:
				//	TODO Ni idea de como enfocarlo
			break;
			case RTF:
			case WORD:
				JRRtfExporter exporterJRRtf = new JRRtfExporter();
				
				exporterJRRtf.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterJRRtf.setExporterOutput((WriterExporterOutput) new SimpleOutputStreamExporterOutput(outputStream));
				exporterJRRtf.exportReport();
			break;
			case CSV:
				JRCsvExporter exporterJRCsv = new JRCsvExporter();
				
				exporterJRCsv.setExporterInput(new SimpleExporterInput(jasperPrint));

				exporterJRCsv.setExporterOutput((WriterExporterOutput) new SimpleOutputStreamExporterOutput(outputStream));
				//exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "|");

				exporterJRCsv.exportReport();
			break;
			case XML:
				JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
			break;
			case PDF:
				JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
			break;
			case XLS:
				JRXlsExporter exporterJRXls = new JRXlsExporter();
				
				exporterJRXls.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporterJRXls.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				
				configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
				configuration.setCollapseRowSpan(Boolean.TRUE);
				configuration.setRemoveEmptySpaceBetweenColumns(Boolean.FALSE);
				configuration.setWhitePageBackground(Boolean.FALSE);
				configuration.setIgnoreCellBackground(Boolean.TRUE);
				
				exporterJRXls.setConfiguration(configuration);
				
				exporterJRXls.exportReport();

			break;
		
			default:
			break;
		}
	}

	
	
	/**
	 * @param informe
	 * @param jasperPrint
	 * @throws JRException
	 */
	public static byte[] generateReportByte(Informe informe, JasperPrint jasperPrint) throws JRException
	{				
		
		byte[] result = null;
		
		ByteArrayOutputStream baos = null;
		
		
		switch (informe.getExtension())
		{
			case HTML:
				//	TODO Ni idea de como enfocarlo
			break;
			case RTF:
			case WORD:
				JRRtfExporter exporterJRRtf = new JRRtfExporter();
				 
				exporterJRRtf.setExporterInput(new SimpleExporterInput(jasperPrint));
				baos = new ByteArrayOutputStream();   
				exporterJRRtf.setExporterOutput((WriterExporterOutput) new SimpleOutputStreamExporterOutput(baos));
				exporterJRRtf.exportReport();
				
				result = baos.toByteArray();
				
//				 JRRtfExporter exporter = new JRRtfExporter();
//				   ByteArrayOutputStream baos = new ByteArrayOutputStream();    
//				   exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
//				   exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, baos);
//				   exporter.exportReport(); 
//				   return baos.toByteArray();
			break;
			case CSV:
				JRCsvExporter exporterJRCsv = new JRCsvExporter();
				 
				
				exporterJRCsv.setExporterInput(new SimpleExporterInput(jasperPrint));
				baos = new ByteArrayOutputStream();   
				exporterJRCsv.setExporterOutput((WriterExporterOutput) new SimpleOutputStreamExporterOutput(baos));
				//exporter.setParameter(JRCsvExporterParameter.FIELD_DELIMITER, "|");

				exporterJRCsv.exportReport();
				
			break;
			case XML:
//				result = JasperExportManager.exportReportTo
			break;
			case PDF:
				result = JasperExportManager.exportReportToPdf(jasperPrint);
			break;
			case XLS:
				JRXlsExporter exporterJRXls = new JRXlsExporter();
				
				exporterJRXls.setExporterInput(new SimpleExporterInput(jasperPrint));
				baos = new ByteArrayOutputStream();  
				exporterJRXls.setExporterOutput(new SimpleOutputStreamExporterOutput(baos));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				
				configuration.setRemoveEmptySpaceBetweenRows(Boolean.TRUE);
				configuration.setCollapseRowSpan(Boolean.TRUE);
				configuration.setRemoveEmptySpaceBetweenColumns(Boolean.FALSE);
				configuration.setWhitePageBackground(Boolean.FALSE);
				configuration.setIgnoreCellBackground(Boolean.TRUE);
				
				exporterJRXls.setConfiguration(configuration);
				
				exporterJRXls.exportReport();
				result = baos.toByteArray();
			break;
		
			default:
			break;
		}
		
		return result;
	}
}
