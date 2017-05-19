package org.orbit.component.model.tier2.appstore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for App manifest
 *
 */
@XmlRootElement
public class AppManifestDTO {

	@XmlElement
	protected String appId;
	@XmlElement
	protected String namespace;
	@XmlElement
	protected String categoryId;
	@XmlElement
	protected String name;
	@XmlElement
	protected String version;
	@XmlElement
	protected int priority;
	@XmlElement
	protected String appManifest;
	@XmlElement
	protected String fileName;
	@XmlElement
	protected String description;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public AppManifestDTO() {

	}

	@XmlElement
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@XmlElement
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	@XmlElement
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement
	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	@XmlElement
	public String getAppManifest() {
		return appManifest;
	}

	public void setAppManifest(String appManifest) {
		this.appManifest = appManifest;
	}

	@XmlElement
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public long getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
