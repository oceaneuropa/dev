package org.orbit.component.api.tier2.appstore;

public class CreateAppRequest {

	protected String appId;
	protected String appVersion;
	protected String type;
	protected String name;
	protected String manifest;
	protected String fileName;
	protected String description;

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

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManifest() {
		return this.manifest;
	}

	public void setManifest(String manifest) {
		this.manifest = manifest;
	}

	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
