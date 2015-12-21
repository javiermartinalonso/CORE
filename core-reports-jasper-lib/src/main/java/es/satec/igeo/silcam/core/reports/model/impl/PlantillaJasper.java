package es.satec.igeo.silcam.core.reports.model.impl;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import es.satec.igeo.silcam.core.reports.model.Parametro;
import es.satec.igeo.silcam.core.reports.model.Plantilla;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

/**
 * Objeto que representa una plantilla de disenio de Jasper en disco.
 * 
 * @author javier.martin 18 de dic. de 2015
 */
public class PlantillaJasper implements Plantilla
{

	// Origen de datos de la plantilla
	// private JRDataSource dataSource;
	// Disenio de la plantilla
	private JasperDesign plantillaJasperDesign;

	// Descripcion de la plantilla
	private String descripcion;


	public PlantillaJasper(JasperDesign plantillaJasperDesign)
	{
		super();
		this.plantillaJasperDesign = plantillaJasperDesign;
	}


	public PlantillaJasper(String path) throws JRException
	{
		super();
		this.plantillaJasperDesign = JRXmlLoader.load(path);
	}


	public PlantillaJasper(File file) throws JRException
	{
		super();
		this.plantillaJasperDesign = JRXmlLoader.load(file);
	}


	public PlantillaJasper(InputStream inputStream) throws JRException
	{
		super();
		this.plantillaJasperDesign = JRXmlLoader.load(inputStream);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see es.satec.igeo.silcam.core.reports.model.Plantilla#getName()
	 */
	@Override
	public String getName()
	{
		return this.plantillaJasperDesign.getName();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see es.satec.igeo.silcam.core.reports.model.Plantilla#getDescripcion()
	 */
	@Override
	public String getDescripcion()
	{
		return descripcion;
	}


	public JasperDesign getPlantillaJasperDesign()
	{
		return plantillaJasperDesign;
	}

	// public void setDataSource(JRDataSource dataSource) {
	// this.dataSource = dataSource;
	// }


	/*
	 * (non-Javadoc)
	 * 
	 * @see es.satec.igeo.silcam.core.reports.model.Plantilla#getParametros()
	 */
	@Override
	public List<Parametro> getParametros()
	{

		List<Parametro> parametros = null;

		for (JRParameter parameter : plantillaJasperDesign.getParametersMap().values())
		{
			if (parametros == null)
			{
				parametros = new LinkedList<Parametro>();
			}

			parametros.add(new Parametro(parameter.getName(), parameter.getDescription(), parameter.getValueClassName(), parameter.isForPrompting()));
		}

		return parametros;
	}


	@Override
	public Parametro getParameter(String parameterName)
	{
		Parametro parametro = null;
		for (JRParameter param : plantillaJasperDesign.getParameters())
		{
			if (param.getName().equals(parameterName))
			{
				JRParameter jrParameter = param;
				parametro = new Parametro(jrParameter.getName(), jrParameter.getDescription(), jrParameter.getValueClass(), jrParameter.isForPrompting());
				break;
			}
		}
		return parametro;
	}
}