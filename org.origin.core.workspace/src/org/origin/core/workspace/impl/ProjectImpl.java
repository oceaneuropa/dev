package org.origin.core.workspace.impl;

import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IResource;

public class ProjectImpl extends ContainerImpl implements IProject {

	@Override
	public int getType() {
		return IResource.PROJECT;
	}

	@Override
	public IProject getProject() {
		return this;
	}

}
