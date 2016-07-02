package org.nb.mgm.model.dto;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;

public class ProjectDTO {

	@XmlElement
	protected String id;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	// a list of ProjectHomeConfigs in the Project
	@XmlElement
	protected List<ProjectHomeDTO> homeConfigs = new ArrayList<ProjectHomeDTO>();

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
	public List<ProjectHomeDTO> getHomeConfigs() {
		return homeConfigs;
	}

	public void setHomeConfigs(List<ProjectHomeDTO> homeConfigs) {
		this.homeConfigs = homeConfigs;
	}

}
