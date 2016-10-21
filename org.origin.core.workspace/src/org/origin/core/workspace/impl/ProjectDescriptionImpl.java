package org.origin.core.workspace.impl;

import org.origin.core.workspace.IProjectDescription;

public class ProjectDescriptionImpl extends ContainerDescriptionImpl implements IProjectDescription {

	@Override
	public IProjectDescription clone() {
		return (IProjectDescription) super.clone();
	}

}
