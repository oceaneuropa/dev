package org.orbit.component.model.tier2.appstore;

public class AppManifestRTO {

	protected int id;
	protected String appId;
	protected String appVersion;
	protected String name;
	protected String type;
	protected int priority;
	protected String appManifest;
	protected String fileName;
	protected String description;
	protected long dateCreated;
	protected long dateModified;

	public AppManifestRTO() {
	}

	/**
	 * 
	 * @param id
	 * @param appId
	 * @param appVersion
	 * @param name
	 * @param type
	 * @param priority
	 * @param appManifest
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 */
	public AppManifestRTO(int id, String appId, String appVersion, String name, String type, int priority, String appManifest, String description, long dateCreated, long dateModified) {
		assert (appId != null) : "appId is null";
		assert (appVersion != null) : "appVersion is null";
		assert (name != null) : "appName is null";
		assert (type != null) : "type is null";

		this.id = id;
		this.appId = appId;
		this.appVersion = appVersion;
		this.name = name;
		this.type = type;
		this.priority = priority;
		this.appManifest = appManifest;
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

	public String getName() {
		return this.name;
	}

	public void setName(String appName) {
		this.name = appName;
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

	public String getAppManifest() {
		return this.appManifest;
	}

	public void setAppManifest(String appManifest) {
		this.appManifest = appManifest;
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
		result = prime * result + ((appId == null) ? 0 : appId.hashCode());
		result = prime * result + ((appVersion == null) ? 0 : appVersion.hashCode());
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
		AppManifestRTO other = (AppManifestRTO) obj;
		if (appId == null) {
			if (other.appId != null)
				return false;
		} else if (!appId.equals(other.appId))
			return false;
		if (appVersion == null) {
			if (other.appVersion != null)
				return false;
		} else if (!appVersion.equals(other.appVersion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AppManifestRTO(");
		sb.append("id=").append(this.id);
		sb.append("appId=").append(this.appId);
		sb.append("appVersion=").append(this.appVersion);
		sb.append(", name=").append(this.name);
		sb.append(", type=").append(this.type);
		sb.append(", priority=").append(this.priority);
		sb.append(", appManifest=").append(this.appManifest);
		sb.append(", fileName=").append(this.fileName);
		sb.append(", description=").append(this.description);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");
		return sb.toString();
	}

}
