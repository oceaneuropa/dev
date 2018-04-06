package org.origin.common.resources.impl;

import java.io.IOException;

import org.origin.common.resources.FolderDescription;
import org.origin.common.resources.IFile;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;
import org.origin.common.resources.impl.misc.FolderDescriptionPersistence;

public class FolderImpl extends ResourceImpl implements IFolder {

	protected FolderDescription desc;

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 */
	public FolderImpl(IWorkspace workspace, IPath fullpath) {
		super(workspace, fullpath);
	}

	@Override
	public IResource[] getMembers() {
		IResource[] members = getWorkspace().getMembers(getFullPath());
		if (members == null) {
			members = new IResource[] {};
		}
		return members;
	}

	@Override
	public IResource findMember(String name) {
		IResource member = null;
		if (name != null) {
			IResource[] members = getMembers();
			for (IResource currMember : members) {
				String currName = currMember.getName();
				if (name.equals(currName)) {
					member = currMember;
					break;
				}
			}
		}
		return member;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <RESOURCE extends IResource> RESOURCE findMember(String name, Class<RESOURCE> clazz) {
		RESOURCE resource = null;
		IResource member = findMember(name);
		if (member != null) {
			if (clazz.isAssignableFrom(member.getClass())) {
				resource = (RESOURCE) member;
			} else {
				// returns null when when resource exists but cannot be casted to the given resource class.
				// - no exception will be thrown, since it is more about the resource with expected name and class cannot be found.
			}
		}
		return resource;
	}

	@Override
	public boolean create() throws IOException {
		return getWorkspace().createUnderlyingFolder(this);
	}

	@Override
	public boolean exists(IPath path) {
		IPath fullpath = getFullPath().append(path);
		boolean exists = getWorkspace().underlyingResourceExists(fullpath);
		return exists;
	}

	@Override
	public IFile getFile(String name) throws IOException {
		return getFile(new PathImpl(name));
	}

	@Override
	public IFile getFile(IPath path) throws IOException {
		IPath fullpath = getFullPath().append(path);
		IFile file = getWorkspace().getFile(fullpath);
		return file;
	}

	@Override
	public IFolder getFolder(String name) throws IOException {
		return getFolder(new PathImpl(name));
	}

	@Override
	public IFolder getFolder(IPath path) throws IOException {
		IPath fullpath = getFullPath().append(path);
		IFolder folder = getWorkspace().getFolder(fullpath);
		return folder;
	}

	@Override
	public boolean create(FolderDescription desc) throws IOException {
		if (exists()) {
			return false;
		}
		boolean succeed = create();
		if (succeed) {
			setDescription(desc);
		}
		return succeed;
	}

	@Override
	public void setDescription(FolderDescription desc) throws IOException {
		this.desc = desc;
		FolderDescriptionPersistence.getInstance().save(this, desc);
	}

	@Override
	public FolderDescription getDescription() throws IOException {
		if (this.desc == null) {
			this.desc = FolderDescriptionPersistence.getInstance().load(this);
		}
		return this.desc;
	}

}
