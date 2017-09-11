package org.origin.core.resources;

public class WorkspaceDescription {

	public static final String DEFAULT_VERSION = "1.0";

	protected String version = DEFAULT_VERSION;
	protected String id;
	protected String name;
	protected String password;
	protected String description;

	public WorkspaceDescription() {
	}

	/**
	 * 
	 * @param version
	 * @param id
	 * @param name
	 * @param password
	 * @param description
	 */
	public WorkspaceDescription(String version, String id, String name, String password, String description) {
		this.version = version;
		this.id = id;
		this.name = name;
		this.password = password;
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "WorkspaceDescription [version=" + this.version + ", id=" + this.id + ", name=" + this.name + ", password=" + this.password + ", description=" + this.description + "]";
	}

}
