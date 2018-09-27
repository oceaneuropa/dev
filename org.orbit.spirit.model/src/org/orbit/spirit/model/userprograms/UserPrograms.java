package org.orbit.spirit.model.userprograms;

import java.util.List;

import org.origin.common.resource.RList;
import org.origin.common.resource.impl.RObjectImpl;

public class UserPrograms extends RObjectImpl {

	protected List<UserProgram> userPrograms;

	public UserPrograms() {
	}

	public synchronized List<UserProgram> getChildren() {
		if (this.userPrograms == null) {
			this.userPrograms = new RList<UserProgram>(this, true);
		}
		return this.userPrograms;
	}

}
