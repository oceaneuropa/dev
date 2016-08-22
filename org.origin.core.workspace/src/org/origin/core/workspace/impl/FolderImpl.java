package org.origin.core.workspace.impl;

import org.origin.core.workspace.IFolder;
import org.origin.core.workspace.IResource;

public class FolderImpl extends ContainerImpl implements IFolder {

	@Override
	public int getType() {
		return IResource.FOLDER;
	}

}
