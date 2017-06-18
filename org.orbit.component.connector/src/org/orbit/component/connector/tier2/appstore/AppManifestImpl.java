package org.orbit.component.connector.tier2.appstore;

import java.util.Date;

import org.orbit.component.api.tier2.appstore.AppManifest;

public class AppManifestImpl implements AppManifest {

	protected String appId;
	protected String name;
	protected String version;
	protected String type;
	protected int priority;
	protected String manifestString;
	protected String fileName;
	protected String description;
	protected Date dateCreated;
	protected Date dateModified;

	public AppManifestImpl() {
	}

	@Override
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@Override
	public String getManifestString() {
		return manifestString;
	}

	public void setManifestString(String manifestString) {
		this.manifestString = manifestString;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public Date getDateModified() {
		return dateModified;
	}

	public void setDateModified(Date dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof AppManifest)) {
			return false;
		}
		AppManifest other = (AppManifest) obj;
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
		sb.append("AppManifest(");
		sb.append("appId=").append(this.appId);
		sb.append(", name=").append(this.name);
		sb.append(", version=").append(this.version);
		sb.append(", type=").append(this.type);
		sb.append(", priority=").append(this.priority);
		sb.append(", manifest=").append(this.manifestString);
		sb.append(", fileName=").append(this.fileName);
		sb.append(", description=").append(this.description);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");
		return sb.toString();
	}

}
