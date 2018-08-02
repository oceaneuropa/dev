package org.orbit.component.runtime.model.appstore;

public class AppManifest {

	protected int id;
	protected String appId;
	protected String appVersion;
	protected String type;
	protected String name;
	protected String appManifest;
	protected String appFileName;
	protected String description;
	protected long dateCreated;
	protected long dateModified;

	public AppManifest() {
	}

	/**
	 * 
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param name
	 * @param type
	 * @param appManifest
	 * @param appFileName
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 */
	public AppManifest(int id, String appId, String appVersion, String name, String type, String appManifest, String appFileName, String description, long dateCreated, long dateModified) {
		assert (appId != null) : "appId is null";
		assert (appVersion != null) : "appVersion is null";

		this.id = id;
		this.appId = appId;
		this.appVersion = appVersion;
		this.type = type;
		this.name = name;
		this.appManifest = appManifest;
		this.appFileName = appFileName;
		this.description = description;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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

	public void setName(String appName) {
		this.name = appName;
	}

	public String getAppManifest() {
		return this.appManifest;
	}

	public void setAppManifest(String appManifest) {
		this.appManifest = appManifest;
	}

	public String getAppFileName() {
		return this.appFileName;
	}

	public void setAppFileName(String fileName) {
		this.appFileName = fileName;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.appId == null) ? 0 : this.appId.hashCode());
		result = prime * result + ((this.appVersion == null) ? 0 : this.appVersion.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AppManifest other = (AppManifest) obj;
		if (this.appId == null) {
			if (other.appId != null)
				return false;
		} else if (!this.appId.equals(other.appId))
			return false;
		if (this.appVersion == null) {
			if (other.appVersion != null)
				return false;
		} else if (!this.appVersion.equals(other.appVersion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AppManifest(");
		sb.append("id=").append(this.id);
		sb.append("appId=").append(this.appId);
		sb.append("appVersion=").append(this.appVersion);
		sb.append(", type=").append(this.type);
		sb.append(", name=").append(this.name);
		sb.append(", appManifest=").append(this.appManifest);
		sb.append(", appFileName=").append(this.appFileName);
		sb.append(", description=").append(this.description);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");
		return sb.toString();
	}

}
