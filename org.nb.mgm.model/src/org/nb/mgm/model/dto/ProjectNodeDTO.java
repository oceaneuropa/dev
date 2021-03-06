package org.nb.mgm.model.dto;

import javax.xml.bind.annotation.XmlElement;

public class ProjectNodeDTO {

	// Attributes
	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	// Container ProjectDTO
	@XmlElement
	protected ProjectDTO projectDTO;

	// Container ProjectHomeDTO
	@XmlElement
	protected ProjectHomeDTO projectHomeDTO;

	@XmlElement
	public ProjectDTO getProject() {
		return projectDTO;
	}

	public void setProject(ProjectDTO projectDTO) {
		this.projectDTO = projectDTO;
	}

	@XmlElement
	public ProjectHomeDTO getProjectHome() {
		return projectHomeDTO;
	}

	public void setProjectHome(ProjectHomeDTO projectHomeDTO) {
		this.projectHomeDTO = projectHomeDTO;
	}

	@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
