package org.origin.core.workspace.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.resource.RObjectImpl;
import org.origin.core.workspace.IResourceDescription;

public abstract class ResourceDescriptionImpl extends RObjectImpl implements IResourceDescription {

	protected String name;
	protected List<String> natureIds = new ArrayList<String>();
	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public String[] getNatureIds() {
		return this.natureIds.toArray(new String[this.natureIds.size()]);
	}

	@Override
	public void setNatureIds(String[] natureIds) {
		this.natureIds.clear();
		for (String natureId : natureIds) {
			addNatureId(natureId);
		}
	}

	/**
	 * 
	 * @param natureId
	 * @return
	 */
	@Override
	public boolean addNatureId(String natureId) {
		if (natureId != null && !this.natureIds.contains(natureId)) {
			return this.natureIds.add(natureId);
		}
		return false;
	}

	/**
	 * 
	 * @param natureId
	 * @return
	 */
	@Override
	public boolean removeNatureId(String natureId) {
		if (natureId != null && this.natureIds.contains(natureId)) {
			return this.natureIds.remove(natureId);
		}
		return false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	@Override
	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public void setProperty(String key, Object value) {
		this.properties.put(key, value);
	}

	@Override
	public void removeProperty(String key) {
		this.properties.remove(key);
	}

	@Override
	public Object clone() {
		ResourceDescriptionImpl clone = (ResourceDescriptionImpl) super.clone();
		clone.setName(name);
		clone.setNatureIds(getNatureIds());
		clone.setProperties(properties);
		return clone;
	}

}
