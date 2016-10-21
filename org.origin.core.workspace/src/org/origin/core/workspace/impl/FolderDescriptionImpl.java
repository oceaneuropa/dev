package org.origin.core.workspace.impl;

import org.origin.core.workspace.IFolderDescription;

public class FolderDescriptionImpl extends ResourceDescriptionImpl implements IFolderDescription {

	@Override
	public IFolderDescription clone() {
		return (IFolderDescription) super.clone();
	}

}
