package org.origin.core.resources.impl;

import java.io.IOException;

import org.origin.core.resources.IPath;
import org.origin.core.resources.IProject;
import org.origin.core.resources.IWorkspace;
import org.origin.core.resources.ProjectDescription;
import org.origin.core.resources.impl.misc.ProjectDescriptionPersistence;

public class ProjectImpl extends FolderImpl implements IProject {

	protected ProjectDescription desc;

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 */
	public ProjectImpl(IWorkspace workspace, IPath fullpath) {
		super(workspace, fullpath);
	}

	@Override
	public synchronized boolean create() throws IOException {
		String name = getName();
		return create(new ProjectDescription(name, name));
	}

	@Override
	public synchronized boolean create(ProjectDescription desc) throws IOException {
		if (exists()) {
			return false;
		}
		boolean succeed = super.create();
		if (succeed) {
			setDescription(desc);
		}
		return succeed;
	}

	@Override
	public synchronized void setDescription(ProjectDescription desc) throws IOException {
		this.desc = desc;
		ProjectDescriptionPersistence.getInstance().save(this, desc);
	}

	@Override
	public synchronized ProjectDescription getDescription() throws IOException {
		if (this.desc == null) {
			this.desc = ProjectDescriptionPersistence.getInstance().load(this);
		}
		return this.desc;
	}

}
