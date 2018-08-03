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
	protected int id;
	@XmlElement
	protected String appId;
	@XmlElement
	protected String appVersion;
	@XmlElement
	protected String type;
	@XmlElement
	protected String name;
	@XmlElement
	protected String appManifest;
	@XmlElement
	protected String appFileName;
	@XmlElement
	protected long appFileLength;
	@XmlElement
	protected String description;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public AppManifestDTO() {
	}

	@XmlElement
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@XmlElement
	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@XmlElement
	public String getAppVersion() {
		return this.appVersion;
	}

	public void setAppVersion(String version) {
		this.appVersion = version;
	}

	@XmlElement
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getAppManifest() {
		return this.appManifest;
	}

	public void setAppManifest(String appManifest) {
		this.appManifest = appManifest;
	}

	@XmlElement
	public String getAppFileName() {
		return this.appFileName;
	}

	public void setAppFileName(String fileName) {
		this.appFileName = fileName;
	}

	@XmlElement
	public long getAppFileLength() {
		return this.appFileLength;
	}

	public void setAppFileLength(long appFileLength) {
		this.appFileLength = appFileLength;
	}

	@XmlElement
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@XmlElement
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
