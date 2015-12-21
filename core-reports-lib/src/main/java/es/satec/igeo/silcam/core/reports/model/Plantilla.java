package es.satec.igeo.silcam.core.reports.model;

import java.util.List;

public interface Plantilla {

	String getName();
	
	String getDescripcion();

	List<Parametro> getParametros();
	
	Parametro getParameter(String parameterName);

}