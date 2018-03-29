package org.origin.common.extensions;

import java.util.HashMap;
import java.util.Map;

import org.origin.common.extensions.util.InterfacesAware;
import org.origin.common.extensions.util.InterfacesSupport;

/**
 * Program extension description.
 * 
 */
public class Extension implements InterfacesAware {

	protected String typeId;
	protected String id;
	protected String name;
	protected String description;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected InterfacesSupport interfacesSupport = new InterfacesSupport();

	/**
	 * 
	 * @param typeId
	 * @param id
	 */
	public Extension(String typeId, String id) {
		this.typeId = typeId;
		this.id = id;
	}

	/**
	 * 
	 * @param typeId
	 * @param id
	 * @param name
	 */
	public Extension(String typeId, String id, String name) {
		this.typeId = typeId;
		this.id = id;
		this.name = name;
	}

	/**
	 * 
	 * @param typeId
	 * @param id
	 * @param name
	 * @param description
	 */
	public Extension(String typeId, String id, String name, String description) {
		this.typeId = typeId;
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public String getTypeId() {
		return this.typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<Object, Object> properties) {
		this.properties = properties;
	}

	@Override
	public Object[] getInterfaces() {
		return this.interfacesSupport.getInterfaces();
	}

	@Override
	public <T> T getInterface(Class<T> clazz) {
		return this.interfacesSupport.getInterface(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Class<?> clazz) {
		return this.interfacesSupport.getInterfaceDescription(clazz);
	}

	@Override
	public InterfaceDescription getInterfaceDescription(Object object) {
		return this.interfacesSupport.getInterfaceDescription(object);
	}

	@Override
	public <T> void addInterface(Class<?> clazz, T interfaceInstance) {
		this.interfacesSupport.addInterface(clazz, interfaceInstance);
	}

	@Override
	public void addInterface(Class<?> clazz, String interfaceClassName) {
		this.interfacesSupport.addInterface(clazz, interfaceClassName);
	}

	@Override
	public void addInterface(Class<?> clazz, Class<?> interfaceImplClass) {
		this.interfacesSupport.addInterface(clazz, interfaceImplClass);
	}

	@Override
	public void addInterface(InterfaceDescription description) {
		this.interfacesSupport.addInterface(description);
	}

	@Override
	public void removeInterface(Class<?>... classes) {
		this.interfacesSupport.removeInterface(classes);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((typeId == null) ? 0 : typeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Extension other = (Extension) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (typeId == null) {
			if (other.typeId != null)
				return false;
		} else if (!typeId.equals(other.typeId))
			return false;
		return true;
	}

}
