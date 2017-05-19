package org.orbit.component.model.tier2.appstore;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.jdbc.SQLWhereOperator;

/**
 * DTO for App query
 *
 */
@XmlRootElement
public class AppQueryDTO {

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
	protected String description;

	@XmlElement
	protected String appId_oper;
	@XmlElement
	protected String namespace_oper;
	@XmlElement
	protected String categoryId_oper;
	@XmlElement
	protected String name_oper;
	@XmlElement
	protected String version_oper;
	@XmlElement
	protected String description_oper;

	public AppQueryDTO() {
	}

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
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
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	@XmlElement
	public String getAppId_oper() {
		return appId_oper;
	}

	public void setAppId_oper(String appId_oper) {
		this.appId_oper = SQLWhereOperator.isEqual(appId_oper) ? null : appId_oper;
	}

	@XmlElement
	public String getNamespace_oper() {
		return namespace_oper;
	}

	public void setNamespace_oper(String namespace_oper) {
		this.namespace_oper = SQLWhereOperator.isEqual(namespace_oper) ? null : namespace_oper;
	}

	@XmlElement
	public String getCategoryId_oper() {
		return categoryId_oper;
	}

	public void setCategoryId_oper(String categoryId_oper) {
		this.categoryId_oper = SQLWhereOperator.isEqual(categoryId_oper) ? null : categoryId_oper;
	}

	@XmlElement
	public String getName_oper() {
		return name_oper;
	}

	public void setName_oper(String name_oper) {
		this.name_oper = SQLWhereOperator.isEqual(name_oper) ? null : name_oper;
	}

	@XmlElement
	public String getVersion_oper() {
		return version_oper;
	}

	public void setVersion_oper(String version_oper) {
		this.version_oper = SQLWhereOperator.isEqual(version_oper) ? null : version_oper;
	}

	@XmlElement
	public String getDescription_oper() {
		return description_oper;
	}

	public void setDescription_oper(String description_oper) {
		this.description_oper = SQLWhereOperator.isEqual(description_oper) ? null : description_oper;
	}

}
