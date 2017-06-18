package org.orbit.component.api.tier2.appstore.request;

public class CreateAppRequest {

	protected String appId;
	protected String name;
	protected String version;
	protected String type;
	protected int priority = 1000;
	protected String manifest;
	protected String fileName;
	protected String description;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
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
