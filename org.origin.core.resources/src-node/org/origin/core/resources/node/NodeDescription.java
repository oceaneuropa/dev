package org.origin.core.resources.node;

public class NodeDescription {

	public static final String DEFAULT_VERSION = "1.0";

	protected String version = DEFAULT_VERSION;
	protected String id;
	protected String name;

	public NodeDescription() {
	}

	/**
	 * 
	 * @param id
	 */
	public NodeDescription(String id) {
		this(id, id);
	}

	/**
	 * 
	 * @param id
	 * @param name
	 */
	public NodeDescription(String id, String name) {
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
		return "NodeDescription [version=" + version + ", id=" + id + ", name=" + name + "]";
	}

}
