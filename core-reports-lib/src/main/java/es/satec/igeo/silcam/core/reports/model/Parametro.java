package es.satec.igeo.silcam.core.reports.model;

public class Parametro
{
	private String		name;
	private String		descripcion;
	private String		className;
	private Class<?>	classType;
	private boolean		required;
	//	private Object value;


	public Parametro(String name, String descripcion, String className, boolean required)
	{
		super();
		this.descripcion = descripcion;
		this.name = name;
		this.className = className;

		try
		{
			this.classType = Class.forName(className);
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		this.required = required;
	}


	public Parametro(String name, String descripcion, Class<?> classType, boolean required)
	{
		super();
		this.descripcion = descripcion;
		this.name = name;
		this.classType = classType;
		this.className = classType.getName();
		this.required = required;
	}


	public String getDescripcion()
	{
		return descripcion;
	}


	public void setDescripcion(String descripcion)
	{
		this.descripcion = descripcion;
	}


	public String getName()
	{
		return name;
	}


	public void setName(String name)
	{
		this.name = name;
	}


	public String getClassName()
	{
		return className;
	}


	public void setClassName(String className)
	{
		this.className = className;
	}


	public Class<?> getClassType()
	{
		return classType;
	}


	public void setClassType(Class<?> classType)
	{
		this.classType = classType;
	}


	public boolean isRequired()
	{
		return required;
	}


	public void setRequired(boolean required)
	{
		this.required = required;
	}


	//	public Object getValue()
	//	{
	//		return value;
	//	}
	//
	//
	//	public void setValue(Object value)
	//	{
	//		this.value = value;
	//	}

	public String toString()
	{
		return "{Nombre: '" + getName() + "'; Clase: '" + getClassType().getName() + "'; Descripcion: '" + getDescripcion() + "'}";
	}

}
