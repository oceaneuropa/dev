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
	protected String type;
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
	protected String type_oper;
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
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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
	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@XmlElement
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	@XmlElement
	public String getAppId_oper() {
		return this.appId_oper;
	}

	public void setAppId_oper(String appId_oper) {
		this.appId_oper = SQLWhereOperator.isEqual(appId_oper) ? null : appId_oper;
	}

	@XmlElement
	public String getType_oper() {
		return this.type_oper;
	}

	public void setType_oper(String type_oper) {
		this.type_oper = SQLWhereOperator.isEqual(type_oper) ? null : type_oper;
	}

	@XmlElement
	public String getName_oper() {
		return this.name_oper;
	}

	public void setName_oper(String name_oper) {
		this.name_oper = SQLWhereOperator.isEqual(name_oper) ? null : name_oper;
	}

	@XmlElement
	public String getVersion_oper() {
		return this.version_oper;
	}

	public void setVersion_oper(String version_oper) {
		this.version_oper = SQLWhereOperator.isEqual(version_oper) ? null : version_oper;
	}

	@XmlElement
	public String getDescription_oper() {
		return this.description_oper;
	}

	public void setDescription_oper(String description_oper) {
		this.description_oper = SQLWhereOperator.isEqual(description_oper) ? null : description_oper;
	}

}
