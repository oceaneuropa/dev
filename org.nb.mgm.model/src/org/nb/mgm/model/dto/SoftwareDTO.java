package org.nb.mgm.model.dto;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DTO for Software
 *
 */
@XmlRootElement
public class SoftwareDTO {

	// Container Project
	// The value is set for Software contained by Project only
	@XmlElement
	protected ProjectDTO project;

	@XmlElement
	protected String id;
	@XmlElement
	protected String type;
	@XmlElement
	protected String name;
	@XmlElement
	protected String version;
	@XmlElement
	protected String description;
	@XmlElement
	protected long length;
	@XmlElement
	protected Date lastModified;
	@XmlElement
	protected String md5;

	@XmlElement
	protected String localPath;

	@XmlElement
	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public String getMd5() {
		return md5;
	}

	public void setMd5(String md5) {
		this.md5 = md5;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

}
