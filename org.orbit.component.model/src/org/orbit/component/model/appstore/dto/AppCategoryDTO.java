package org.orbit.component.model.appstore.dto;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Design time object for App category.
 *
 */
@XmlRootElement
public class AppCategoryDTO {

	@XmlElement
	protected String categoryId;
	@XmlElement
	protected String parentId;
	@XmlElement
	protected String name;
	@XmlElement
	protected String description;

	@XmlElement
	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	@XmlElement
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
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
