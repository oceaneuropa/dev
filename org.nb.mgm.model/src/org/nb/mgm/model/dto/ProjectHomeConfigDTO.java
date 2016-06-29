package org.nb.mgm.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ProjectHomeConfigDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	// Container Project
	@XmlElement
	protected ProjectDTO project;

	// The Home that the ProjectHomeConfig is configured with. Could be null if the ProjectHomeConfig is not configured with any Home.
	@XmlElement
	protected HomeDTO home;

	// a list of ProjectNodeConfigs in the ProjectHomeConfig
	@XmlElement
	protected List<ProjectNodeConfigDTO> nodeConfigs = new ArrayList<ProjectNodeConfigDTO>();

	@XmlElement
	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	@XmlElement
	public HomeDTO getHome() {
		return home;
	}

	public void setHome(HomeDTO home) {
		this.home = home;
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

	@XmlElement
	public List<ProjectNodeConfigDTO> getNodeConfigs() {
		return nodeConfigs;
	}

	public void setNodeConfigs(List<ProjectNodeConfigDTO> nodeConfigs) {
		this.nodeConfigs = nodeConfigs;
	}

}
