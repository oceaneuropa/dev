package org.orbit.platform.sdk.extension.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.platform.sdk.extension.IProgramExtensionFilter;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;

/**
 * Program extension description.
 * 
 */
public class ProgramExtension implements IAdaptable {

	protected String typeId;
	protected String id;
	protected String name;
	protected String description;
	// protected IProgramLauncher launcher;
	protected IProgramExtensionFilter filter;
	protected Map<Object, Object> properties = new HashMap<Object, Object>();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param typeId
	 * @param id
	 */
	public ProgramExtension(String typeId, String id) {
		this.typeId = typeId;
		this.id = id;
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

	public IProgramExtensionFilter getFilter() {
		return this.filter;
	}

	public void setFilter(IProgramExtensionFilter filter) {
		this.filter = filter;
	}

	public Map<Object, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<Object, Object> properties) {
		this.properties = properties;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
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
		ProgramExtension other = (ProgramExtension) obj;
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

// public IProgramLauncher getLauncher() {
// return this.launcher;
// }
//
// public void setLauncher(IProgramLauncher launcher) {
// this.launcher = launcher;
// }
