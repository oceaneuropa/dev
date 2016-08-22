package org.origin.core.workspace.impl;

import org.origin.core.workspace.IResource;

public class FileImpl extends ResourceImpl {

	@Override
	public int getType() {
		return IResource.FILE;
	}

}
