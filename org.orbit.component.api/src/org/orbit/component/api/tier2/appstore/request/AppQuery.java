package org.orbit.component.api.tier2.appstore.request;

import org.origin.common.jdbc.SQLWhereOperator;

public class AppQuery {

	protected String appId;
	protected String name;
	protected String version;
	protected String type;
	protected String description;

	protected String appId_oper = SQLWhereOperator.EQUAL;
	protected String name_oper = SQLWhereOperator.LIKE;
	protected String version_oper = SQLWhereOperator.EQUAL;
	protected String type_oper = SQLWhereOperator.EQUAL;
	protected String description_oper = SQLWhereOperator.LIKE;

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
	public String getAppId() {
		return this.appId;
	}

	public void setAppIdEqual(String appId) {
		this.appId = appId;
		this.appId_oper = SQLWhereOperator.EQUAL;
	}

	public void setAppIdNotEqual(String appId) {
		this.appId = appId;
		this.appId_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setAppIdLike(String appId) {
		this.appId = appId;
		this.appId_oper = SQLWhereOperator.LIKE;
	}

	public void setAppIdIn(String appId) {
		this.appId = appId;
		this.appId_oper = SQLWhereOperator.IN;
	}

	public String getName() {
		return this.name;
	}

	public void setNameEqual(String name) {
		this.name = name;
		this.name_oper = SQLWhereOperator.EQUAL;
	}

	public void setNameNotEqual(String name) {
		this.name = name;
		this.name_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setNameLike(String name) {
		this.name = name;
		this.name_oper = SQLWhereOperator.LIKE;
	}

	public void setNameIn(String name) {
		this.name = name;
		this.name_oper = SQLWhereOperator.IN;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersionEqual(String version) {
		this.version = version;
		this.version_oper = SQLWhereOperator.EQUAL;
	}

	public void setVersionNotEqual(String version) {
		this.version = version;
		this.version_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setVersionLike(String version) {
		this.version = version;
		this.version_oper = SQLWhereOperator.LIKE;
	}

	public void setVersionIn(String version) {
		this.version = version;
		this.version_oper = SQLWhereOperator.IN;
	}

	public String getType() {
		return this.type;
	}

	public void setTypeEqual(String type) {
		this.type = type;
		this.type_oper = SQLWhereOperator.EQUAL;
	}

	public void setTypeNotEqual(String type) {
		this.type = type;
		this.type_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setTypeLike(String type) {
		this.type = type;
		this.type_oper = SQLWhereOperator.LIKE;
	}

	public void setTypeIn(String type) {
		this.type = type;
		this.type_oper = SQLWhereOperator.IN;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescriptionEqual(String description) {
		this.description = description;
		this.description_oper = SQLWhereOperator.EQUAL;
	}

	public void setDescriptionNotEqual(String description) {
		this.description = description;
		this.description_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setDescriptionLike(String description) {
		this.description = description;
		this.description_oper = SQLWhereOperator.LIKE;
	}

	public void setDescriptionIn(String description) {
		this.description = description;
		this.description_oper = SQLWhereOperator.IN;
	}

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	public String getAppId_oper() {
		return this.appId_oper;
	}

	public String getName_oper() {
		return this.name_oper;
	}

	public String getVersion_oper() {
		return this.version_oper;
	}

	public String getType_oper() {
		return this.type_oper;
	}

	public String getDescription_oper() {
		return this.description_oper;
	}

}
