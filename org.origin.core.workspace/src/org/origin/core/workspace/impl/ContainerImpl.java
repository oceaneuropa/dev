package org.origin.core.workspace.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.origin.core.workspace.IContainer;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.IWorkspace;

public abstract class ContainerImpl extends ResourceImpl implements IContainer {

	public ContainerImpl() {
	}

	/**
	 * 
	 * @param file
	 */
	public ContainerImpl(File file) {
		super(file);
	}

	@Override
	public IResource[] members() throws IOException {
		if (this.file == null || !this.file.exists() || !this.file.isDirectory()) {
			return EMPTY_MEMBERS;
		}

		IWorkspace workspace = getWorkspace();
		checkWorkspace(workspace);

		List<IResource> resources = new ArrayList<IResource>();
		File[] files = this.file.listFiles();
		if (files != null) {
			for (File currFile : files) {
				IResource resource = workspace.createResource(this, currFile);
				if (resource != null) {
					resources.add(resource);
				}
			}
		}
		return resources.toArray(new IResource[resources.size()]);
	}

}
