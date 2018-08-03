package org.orbit.component.connector.tier2.appstore;

import java.util.Date;

import org.orbit.component.api.tier2.appstore.AppManifest;

public class AppManifestImpl implements AppManifest {

	protected int id;
	protected String appId;
	protected String appVersion;
	protected String type;
	protected String name;
	protected String manifest;
	protected String fileName;
	protected long fileLength;
	protected String description;
	protected Date dateCreated;
	protected Date dateModified;

	public AppManifestImpl() {
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
	 * @param fileLength
	 * @param description
	 * @param dateCreated
	 * @param dateModified
	 */
	public AppManifestImpl(int id, String appId, String appVersion, String type, String name, String manifest, String fileName, long fileLength, String description, Date dateCreated, Date dateModified) {
		this.id = id;
		this.appId = appId;
		this.appVersion = appVersion;
		this.type = type;
		this.name = name;
		this.manifest = manifest;
		this.fileName = fileName;
		this.fileLength = fileLength;
		this.description = description;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@Override
	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	@Override
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getManifest() {
		return this.manifest;
	}

	public void setManifest(String manifestString) {
		this.manifest = manifestString;
	}

	@Override
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public long getFileLength() {
		return this.fileLength;
	}

	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public Date getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public Date getDateModified() {
		return this.dateModified;
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
		sb.append(", appVersion=").append(this.appVersion);
		sb.append(", type=").append(this.type);
		sb.append(", name=").append(this.name);
		sb.append(", manifest=").append(this.manifest);
		sb.append(", fileName=").append(this.fileName);
		sb.append(", description=").append(this.description);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");
		return sb.toString();
	}

}
