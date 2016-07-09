package org.nb.mgm.model.runtime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.model.ModelObject;

public class Machine extends ModelObject {

	protected String ipAddress;
	protected Map<String, Object> properties = new HashMap<String, Object>();

	protected List<Home> homes = new ArrayList<Home>();

	public Machine() {
	}

	/**
	 * 
	 * @param parent
	 */
	public Machine(Object parent) {
		super(parent);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// attributes
	// ----------------------------------------------------------------------------------------------------------------
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	public void removeProperties(List<String> propNames) {
		for (String propName : propNames) {
			this.properties.remove(propName);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Home
	// ----------------------------------------------------------------------------------------------------------------
	public List<Home> getHomes() {
		return this.homes;
	}

	public void addHome(Home home) {
		if (home != null && !this.homes.contains(home)) {
			home.setParent(this);
			this.homes.add(home);
		}
	}

	public void deleteHome(Home home) {
		if (home != null && this.homes.contains(home)) {
			this.homes.remove(home);
			home.setParent(null);
		}
	}

}
