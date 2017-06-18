package org.orbit.component.model.tier2.appstore;

public class AppQueryRTO {

	protected String appId;
	protected String appVersion;
	protected String name;
	protected String type;
	protected String description;

	protected String appId_oper;
	protected String appVersion_oper;
	protected String name_oper;
	protected String type_oper;
	protected String description_oper;

	public AppQueryRTO() {
	}

	public boolean isEmpty() {
		if (this.appId == null //
				&& this.appVersion == null //
				&& this.name == null //
				&& this.type == null //
				&& this.description == null //
		) {
			return true;
		}
		return false;
	}

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	public String getAppId_oper() {
		return appId_oper;
	}

	public void setAppId_oper(String appId_oper) {
		this.appId_oper = appId_oper;
	}

	public String getAppVersion_oper() {
		return appVersion_oper;
	}

	public void setAppVersion_oper(String appVersion_oper) {
		this.appVersion_oper = appVersion_oper;
	}

	public String getName_oper() {
		return name_oper;
	}

	public void setName_oper(String name_oper) {
		this.name_oper = name_oper;
	}

	public String getType_oper() {
		return type_oper;
	}

	public void setType_oper(String type_oper) {
		this.type_oper = type_oper;
	}

	public String getDescription_oper() {
		return description_oper;
	}

	public void setDescription_oper(String description_oper) {
		this.description_oper = description_oper;
	}

}
