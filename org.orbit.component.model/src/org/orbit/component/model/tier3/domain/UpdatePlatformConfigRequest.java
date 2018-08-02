package org.orbit.component.model.tier3.domain;

import java.util.ArrayList;
import java.util.List;

public class UpdatePlatformConfigRequest {

	protected String platformId;
	protected String name;
	protected String home;
	protected String hostURL;
	protected String contextRoot;
	protected List<String> fieldsToUpdate = new ArrayList<String>();

	public String getPlatformId() {
		return this.platformId;
	}

	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
		this.fieldsToUpdate.add("name");
	}

	public String getHome() {
		return this.home;
	}

	public void setHome(String home) {
		this.home = home;
		this.fieldsToUpdate.add("home");
	}

	public String getHostURL() {
		return this.hostURL;
	}

	public void setHostURL(String hostURL) {
		this.hostURL = hostURL;
		this.fieldsToUpdate.add("hostUrl");
	}

	public String getContextRoot() {
		return this.contextRoot;
	}

	public void setContextRoot(String contextRoot) {
		this.contextRoot = contextRoot;
		this.fieldsToUpdate.add("contextRoot");
	}

	public List<String> getFieldsToUpdate() {
		return this.fieldsToUpdate;
	}

	public void setFieldsToUpdate(List<String> fieldsToUpdate) {
		this.fieldsToUpdate = fieldsToUpdate;
	}

}
