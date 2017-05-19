package org.orbit.component.model.tier2.appstore;

public class AppQueryRTO {

	protected String appId;
	protected String namespace;
	protected String categoryId;
	protected String name;
	protected String version;
	protected String description;

	protected String appId_oper;
	protected String namespace_oper;
	protected String categoryId_oper;
	protected String name_oper;
	protected String version_oper;
	protected String description_oper;

	public AppQueryRTO() {
	}

	public boolean isEmpty() {
		if (this.appId == null //
				&& this.namespace == null //
				&& this.categoryId == null //
				&& this.name == null //
				&& this.version == null //
				&& this.description == null //
		) {
			return true;
		}
		return false;
	}

	// ----------------------------------------------------------------------
	// Set/Get
	// ----------------------------------------------------------------------
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
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

	// ----------------------------------------------------------------------
	// Where operator
	// ----------------------------------------------------------------------
	public String getAppId_oper() {
		return appId_oper;
	}

	public void setAppId_oper(String appId_oper) {
		this.appId_oper = appId_oper;
	}

	public String getNamespace_oper() {
		return namespace_oper;
	}

	public void setNamespace_oper(String namespace_oper) {
		this.namespace_oper = namespace_oper;
	}

	public String getCategoryId_oper() {
		return categoryId_oper;
	}

	public void setCategoryId_oper(String categoryId_oper) {
		this.categoryId_oper = categoryId_oper;
	}

	public String getName_oper() {
		return name_oper;
	}

	public void setName_oper(String name_oper) {
		this.name_oper = name_oper;
	}

	public String getVersion_oper() {
		return version_oper;
	}

	public void setVersion_oper(String version_oper) {
		this.version_oper = version_oper;
	}

	public String getDescription_oper() {
		return description_oper;
	}

	public void setDescription_oper(String description_oper) {
		this.description_oper = description_oper;
	}

}
