package org.orbit.platform.sdk.extension.desc;

public class Parameter {

	protected String name;
	protected String description;
	protected boolean optional;

	public Parameter() {
	}

	/**
	 * 
	 * @param name
	 * @param description
	 */
	public Parameter(String name, String description) {
		this(name, description, false);
	}

	/**
	 * 
	 * @param name
	 * @param description
	 * @param optional
	 */
	public Parameter(String name, String description, boolean optional) {
		this.name = name;
		this.description = description;
		this.optional = optional;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isOptional() {
		return this.optional;
	}

	public void setOptional(boolean optional) {
		this.optional = optional;
	}

	public String getLabel() {
		String label = this.name != null ? this.name : "n/a";
		if (this.description != null) {
			label += " (" + this.description + ")";
		}
		if (this.optional) {
			label += " (optional)";
		}
		return label;
	}

	@Override
	public String toString() {
		return "Parameter [name=" + this.name + ", description=" + this.description + ", optional=" + this.optional + "]";
	}

}
