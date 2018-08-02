package org.orbit.component.api.tier2.appstore;

public class UpdateAppRequest {

	protected int id;
	protected String appId;
	protected String appVersion;
	protected String type;
	protected String name;
	protected String manifest;
	protected String fileName;
	protected String description;

	public UpdateAppRequest() {
	}

	/**
	 * 
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param type
	 * @param name
	 * @param manifest
	 * @param fileName
	 * @param description
	 */
	public UpdateAppRequest(int id, String appId, String appVersion, String type, String name, String manifest, String fileName, String description) {
		this.id = id;
		this.appId = appId;
		this.appVersion = appVersion;
		this.type = type;
		this.name = name;
		this.manifest = manifest;
		this.fileName = fileName;
		this.description = description;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

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
