package es.javier.martin.controller;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import es.satec.igeo.silcam.core.reports.manager.ReportManager;
import es.satec.igeo.silcam.core.reports.manager.impl.ReportManagerJasperImpl;
import es.satec.igeo.silcam.core.reports.model.DataSourcePlantilla;
import es.satec.igeo.silcam.core.reports.model.ExtensionInformes;
import es.satec.igeo.silcam.core.reports.model.Informe;
import es.satec.igeo.silcam.core.reports.model.Plantilla;
import es.satec.igeo.silcam.core.reports.model.impl.DataSourcePlantillaJasper;
import es.satec.igeo.silcam.core.reports.model.impl.PlantillaJasper;


@Controller
@RequestMapping("informes")
public class WebController
{
	@RequestMapping(value = "download/{plantillaName}.{plantillaExtension}", method = RequestMethod.GET)
	public @ResponseBody void downloadInforme(@PathVariable String plantillaName, @PathVariable String plantillaExtension, HttpServletRequest request, HttpServletResponse response)
	{
		ReportManager reportManagerJasperImpl = new ReportManagerJasperImpl();

		Plantilla plantilla = null;

		try
		{
			File filePlantilla = new File("C:/reports/reports/" + plantillaName + ".jrxml");
			plantilla = new PlantillaJasper(filePlantilla.getAbsoluteFile());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ExtensionInformes extension = ExtensionInformes.PDF;
		
		switch (plantillaExtension)
		{
			case "pdf":
				extension = ExtensionInformes.PDF;
			break;
			case "xls":
				extension = ExtensionInformes.XLS;
			break;
			default:
			break;
		}
		
		Informe informe = new Informe(plantillaName , extension, null);
		
		
		Map<String, Object> parametrosPlantilla = new HashMap<String, Object>();
		parametrosPlantilla.put("provincia", "Moxico");

		try
		{
			Class.forName("org.postgresql.Driver");
			Connection connection = null;
			connection = DriverManager.getConnection("jdbc:postgresql://silcam.igeo.satec.es:5432/silcam", "igeo_informes", "igeo_informes");

			DataSourcePlantilla dataSourcePlantilla = new DataSourcePlantillaJasper(connection);

			response.setHeader("Cache-Control", "no-cache");
			response.setHeader("Pragma", "no-cache");
			response.setDateHeader("Expires", 0);

			// set content attributes for the response
			response.setContentType("application/" + plantillaExtension);	
			response.setHeader("Content-disposition", "attachment; filename='" + informe.getFullName() + "';");

			final OutputStream outStream = response.getOutputStream();

			reportManagerJasperImpl.generateReportOutputStream(dataSourcePlantilla, plantilla, parametrosPlantilla, informe, outStream);
			connection.close();
			outStream.flush();
			outStream.close();
			response.flushBuffer();
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
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@RequestMapping(value = "{plantillaName}.{plantillaExtension}", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getFileInformeMinerales(@PathVariable String plantillaName, @PathVariable String plantillaExtension)
	{
		ReportManager reportManagerJasperImpl = new ReportManagerJasperImpl();

		Plantilla plantilla = null;

		try
		{
			File filePlantilla = new File("C:/reports/reports/" + plantillaName + ".jrxml");
			plantilla = new PlantillaJasper(filePlantilla.getAbsoluteFile());
		}
		catch (Exception e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ExtensionInformes extension = ExtensionInformes.PDF;
		
		switch (plantillaExtension)
		{
			case "pdf":
				extension = ExtensionInformes.PDF;
			break;
			case "xls":
				extension = ExtensionInformes.XLS;
			break;
			default:
			break;
		}
		
		Informe informe = new Informe(plantillaName , extension, null);
		
		Map<String, Object> parametrosPlantilla = new HashMap<String, Object>();
		parametrosPlantilla.put("provincia", "Moxico");

		Connection connection = null;
		
		try
		{
			Class.forName("org.postgresql.Driver");

			connection = DriverManager.getConnection("jdbc:postgresql://silcam.igeo.satec.es:5432/silcam", "igeo_informes", "igeo_informes");
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		
		DataSourcePlantilla dataSourcePlantillaMinerales = new DataSourcePlantillaJasper(connection);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/" + plantillaExtension));		
		
		String filename = informe.getFullName();
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

		byte[] pdfFile = reportManagerJasperImpl.generateReportByte(dataSourcePlantillaMinerales, plantilla, parametrosPlantilla, informe);

		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfFile, headers, HttpStatus.OK);

		return response;
	}

}
