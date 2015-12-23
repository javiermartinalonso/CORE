package es.satec.igeo.silcam.core.reports.manager;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

import es.satec.igeo.silcam.core.reports.model.DataSourcePlantilla;
import es.satec.igeo.silcam.core.reports.model.Informe;
import es.satec.igeo.silcam.core.reports.model.Plantilla;

public interface ReportManager
{

	void generateReportFile(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe);


	void generateReportFile(Collection<?> collectionDataSource, Plantilla plantilla, Map<String, Object> parameters, Informe informe);


	byte[] generateReportByte(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe);


	void generateReportOutputStream(DataSourcePlantilla dataSourcePlantilla, Plantilla plantilla, Map<String, Object> parameters, Informe informe, OutputStream outputStream);

}