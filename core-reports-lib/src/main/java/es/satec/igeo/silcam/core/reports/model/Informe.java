package es.satec.igeo.silcam.core.reports.model;

public class Informe {

	private static final String SEPARADOR_PUNTO = ".";
	private static final String SEPARADOR_DIRECTORIOS = "/";
	
	private String name;
	private ExtensionInformes extension;
	private String path;

	public Informe(String name, ExtensionInformes extension, String path) {
		super();
		this.name = name;
		this.extension = extension;
		this.path = path;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ExtensionInformes getExtension() {
		return extension;
	}

	public void setExtension(ExtensionInformes extension) {
		this.extension = extension;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getFullPath() {
		return this.getPath() + SEPARADOR_DIRECTORIOS + this.getName() + SEPARADOR_PUNTO + this.getExtension();
	}
	
	public String getFullName() {
		return this.getName() + SEPARADOR_PUNTO + this.getExtension();
	}
}
