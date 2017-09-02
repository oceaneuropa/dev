package org.origin.core.resources.internal;

import java.io.IOException;

import org.origin.core.resources.IFile;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IRoot;

public class FolderImpl extends ResourceImpl implements IFolder {

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public FolderImpl(IRoot root, IPath fullpath) {
		super(root, fullpath);
	}

	@Override
	public IResource[] getMembers() {
		IResource[] members = getRoot().getMembers(getFullPath());
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
		if (member != null && clazz.isAssignableFrom(member.getClass())) {
			resource = (RESOURCE) member;
		}
		return resource;
	}

	@Override
	public boolean create() throws IOException {
		return getRoot().createUnderlyingFolder(getFullPath());
	}

	@Override
	public boolean exists(IPath path) {
		IPath fullpath = getFullPath().append(path);
		boolean exists = getRoot().underlyingResourceExists(fullpath);
		return exists;
	}

	@Override
	public IFile getFile(String name) throws IOException {
		return getFile(new PathImpl(name));
	}

	@Override
	public IFile getFile(IPath path) throws IOException {
		IPath fullpath = getFullPath().append(path);
		IFile file = getRoot().getFile(fullpath);
		return file;
	}

	@Override
	public IFolder getFolder(String name) throws IOException {
		return getFolder(new PathImpl(name));
	}

	@Override
	public IFolder getFolder(IPath path) throws IOException {
		IPath fullpath = getFullPath().append(path);
		IFolder folder = getRoot().getFolder(fullpath);
		return folder;
	}

}
