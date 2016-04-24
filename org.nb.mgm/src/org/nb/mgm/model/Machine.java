package org.nb.mgm.model;

import java.util.ArrayList;
import java.util.List;

import org.origin.common.rest.model.ModelObject;

public class Machine extends ModelObject {

	protected String ipAddress;

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

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
