package org.orbit.spirit.model.userprograms;

import org.origin.common.resource.impl.RObjectImpl;

public class UserProgram extends RObjectImpl {

	protected String id;
	protected String version;

	public UserProgram() {
	}

	public UserProgram(String id, String version) {
		this.id = id;
		this.version = version;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
