package org.orbit.component.model.appstore.runtime;

public class AppManifestRTO {

	protected String appId;
	protected String namespace;
	protected String categoryId;
	protected String name;
	protected String version;
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
	 * @param appId
	 * @param namespace
	 * @param categoryId
	 * @param name
	 * @param version
	 * @param priority
	 * @param appManifest
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 */
	public AppManifestRTO(String appId, String namespace, String categoryId, String name, String version, int priority, String appManifest, String description, long dateCreated, long dateModified) {
		assert (appId != null) : "appId is null";
		assert (namespace != null) : "namespace is null";
		assert (categoryId != null) : "categoryId is null";
		assert (name != null) : "appName is null";
		assert (version != null) : "appVersion is null";

		this.appId = appId;
		this.namespace = namespace;
		this.categoryId = categoryId;
		this.name = name;
		this.version = version;
		this.priority = priority;
		this.appManifest = appManifest;
		this.description = description;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getName() {
		return name;
	}

	public void setName(String appName) {
		this.name = appName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String appVersion) {
		this.version = appVersion;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getAppManifest() {
		return appManifest;
	}

	public void setAppManifest(String appManifest) {
		this.appManifest = appManifest;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateModified() {
		return dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AppManifestRTO)) {
			return false;
		}
		AppManifestRTO other = (AppManifestRTO) obj;
		String otherAppId = other.getAppId();
		if (this.appId.equals(otherAppId)) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.appId.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AppManifestRTO(");
		sb.append("appId=").append(this.appId);
		sb.append(", namespace=").append(this.namespace);
		sb.append(", categoryId=").append(this.categoryId);
		sb.append(", name=").append(this.name);
		sb.append(", version=").append(this.version);
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
