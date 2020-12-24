package org.orbit.infra.model.subs.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SubsTypeDTO {

	@XmlElement
	protected Integer id;
	@XmlElement
	protected String type;
	@XmlElement
	protected String name;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public SubsTypeDTO() {
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
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
