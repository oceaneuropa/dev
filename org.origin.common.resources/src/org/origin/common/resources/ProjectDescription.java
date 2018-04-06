package org.origin.common.resources;

public class ProjectDescription extends FolderDescription {

	public static final String DEFAULT_VERSION = "1.0";

	protected String version = DEFAULT_VERSION;
	protected String id;
	protected String name;

	public ProjectDescription() {
	}

	/**
	 * 
	 * @param id
	 */
	public ProjectDescription(String id) {
		this(DEFAULT_VERSION, id, id);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public ProjectDescription(String id, String name) {
		this(DEFAULT_VERSION, id, name);
	}

	/**
	 * 
	 * @param version
	 * @param id
	 * @param name
	 */
	public ProjectDescription(String version, String id, String name) {
		this.version = version;
		this.id = id;
		this.name = name;
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

	@Override
	public String toString() {
		return "ProjectDescription [version=" + version + ", id=" + id + ", name=" + name + "]";
	}

}
