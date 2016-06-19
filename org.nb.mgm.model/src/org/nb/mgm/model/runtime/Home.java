package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.model.ModelObject;

public class Home extends ModelObject {

	protected String url;
	protected Map<String, Object> properties = new HashMap<String, Object>();

	protected List<String> joinedMetaSectorIds = new ArrayList<String>();
	protected List<String> joinedMetaSpaceIds = new ArrayList<String>();

	public Home() {
		init();
	}

	/**
	 * 
	 * @param machine
	 */
	public Home(Machine machine) {
		super(machine);
		init();
	}

	public Machine getMachine() {
		if (this.getParent() instanceof Machine) {
			return (Machine) this.getParent();
		}
		return null;
	}

	protected void init() {
		properties.put("startTime", new Date());
		properties.put("name", "testName");
		properties.put("number", new Integer(10));
		properties.put("numberStr", "10");
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// ----------------------------------------------------------------------------------------------------------------
	// properties
	// ----------------------------------------------------------------------------------------------------------------
	public Map<String, Object> getProperties() {
		return this.properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties.putAll(properties);
	}

	public void setProperty(String propName, Object propValue) {
		this.properties.put(propName, propValue);
	}

	public void removeProperty(String propName) {
		this.properties.remove(propName);
	}

	public void removeProperties(List<String> propNames) {
		for (String propName : propNames) {
			this.properties.remove(propName);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// joinedMetaSectorIds
	// ----------------------------------------------------------------------------------------------------------------
	public List<String> getJoinedMetaSectorIds() {
		return this.joinedMetaSectorIds;
	}

	public void setJoinedMetaSectorIds(List<String> joinedMetaSectorIds) {
		this.joinedMetaSectorIds = joinedMetaSectorIds;
	}

	public void addJoinedMetaSectorId(String joinedMetaSectorId) {
		if (joinedMetaSectorId != null && !this.joinedMetaSectorIds.contains(joinedMetaSectorId)) {
			this.joinedMetaSectorIds.add(joinedMetaSectorId);
		}
	}

	public void removeJoinedMetaSectorId(String joinedMetaSectorId) {
		if (joinedMetaSectorId != null && this.joinedMetaSectorIds.contains(joinedMetaSectorId)) {
			this.joinedMetaSectorIds.remove(joinedMetaSectorId);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// joinedMetaSpaceIds
	// ----------------------------------------------------------------------------------------------------------------
	public List<String> getJoinedMetaSpaceIds() {
		return this.joinedMetaSpaceIds;
	}

	public void setJoinedMetaSpaceIds(List<String> joinedMetaSpaceIds) {
		this.joinedMetaSpaceIds = joinedMetaSpaceIds;
	}

	public void addJoinedMetaSpaceId(String joinedMetaSpaceId) {
		if (joinedMetaSpaceId != null && !this.joinedMetaSpaceIds.contains(joinedMetaSpaceId)) {
			this.joinedMetaSpaceIds.add(joinedMetaSpaceId);
		}
	}

	public void removeJoinedMetaSpaceId(String joinedMetaSpaceId) {
		if (joinedMetaSpaceId != null && this.joinedMetaSpaceIds.contains(joinedMetaSpaceId)) {
			this.joinedMetaSpaceIds.remove(joinedMetaSpaceId);
		}
	}

}
