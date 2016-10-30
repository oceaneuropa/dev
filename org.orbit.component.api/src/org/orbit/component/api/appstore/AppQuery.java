package org.orbit.component.api.appstore;

import org.origin.common.jdbc.SQLWhereOperator;

public class AppQuery {

	protected String appId;
	protected String namespace;
	protected String categoryId;
	protected String name;
	protected String version;
	protected String description;

	protected String appId_oper = SQLWhereOperator.EQUAL;
	protected String namespace_oper = SQLWhereOperator.EQUAL;
	protected String categoryId_oper = SQLWhereOperator.EQUAL;
	protected String name_oper = SQLWhereOperator.LIKE;
	protected String version_oper = SQLWhereOperator.EQUAL;
	protected String description_oper = SQLWhereOperator.LIKE;

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
	public String getAppId() {
		return appId;
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

	public String getNamespace() {
		return namespace;
	}

	public void setNamespaceEqual(String namespace) {
		this.namespace = namespace;
		this.namespace_oper = SQLWhereOperator.EQUAL;
	}

	public void setNamespaceNotEqual(String namespace) {
		this.namespace = namespace;
		this.namespace_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setNamespaceLike(String namespace) {
		this.namespace = namespace;
		this.namespace_oper = SQLWhereOperator.LIKE;
	}

	public void setNamespaceIn(String namespace) {
		this.namespace = namespace;
		this.namespace_oper = SQLWhereOperator.IN;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryIdEqual(String categoryId) {
		this.categoryId = categoryId;
		this.categoryId_oper = SQLWhereOperator.EQUAL;
	}

	public void setCategoryIdNotEqual(String categoryId) {
		this.categoryId = categoryId;
		this.categoryId_oper = SQLWhereOperator.NOT_EQUAL;
	}

	public void setCategoryIdLike(String categoryId) {
		this.categoryId = categoryId;
		this.categoryId_oper = SQLWhereOperator.LIKE;
	}

	public void setCategoryIdIn(String categoryId) {
		this.categoryId = categoryId;
		this.categoryId_oper = SQLWhereOperator.IN;
	}

	public String getName() {
		return name;
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
		return version;
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

	public String getDescription() {
		return description;
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
		return appId_oper;
	}

	public String getNamespace_oper() {
		return namespace_oper;
	}

	public String getCategoryId_oper() {
		return categoryId_oper;
	}

	public String getName_oper() {
		return name_oper;
	}

	public String getVersion_oper() {
		return version_oper;
	}

	public String getDescription_oper() {
		return description_oper;
	}

}
