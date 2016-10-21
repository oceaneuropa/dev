package org.origin.core.workspace.impl;

import org.origin.core.workspace.IWorkspaceDescription;

public class WorkspaceDescriptionImpl extends ContainerDescriptionImpl implements IWorkspaceDescription {

	@Override
	public IWorkspaceDescription clone() {
		return (IWorkspaceDescription) super.clone();
	}

}
