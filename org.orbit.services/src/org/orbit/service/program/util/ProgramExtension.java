package org.orbit.service.program.util;

import java.util.HashMap;
import java.util.Map;

import org.orbit.service.program.IProgramExtensionFilter;
import org.orbit.service.program.IProgramLauncher;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.adapter.IAdaptable;

/**
 * Program extension description.
 * 
 */
public class ProgramExtension implements IAdaptable {

	protected String typeId;
	protected String id;
	protected IProgramLauncher launcher;
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

	public IProgramLauncher getLauncher() {
		return this.launcher;
	}

	public void setLauncher(IProgramLauncher launcher) {
		this.launcher = launcher;
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

}
