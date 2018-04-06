package org.origin.common.resources.impl;

import java.io.IOException;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.resources.IFolder;
import org.origin.common.resources.IPath;
import org.origin.common.resources.IResource;
import org.origin.common.resources.IWorkspace;

public abstract class ResourceImpl implements IResource {

	protected IWorkspace workspace;
	protected IPath fullpath;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param workspace
	 * @param fullpath
	 */
	public ResourceImpl(IWorkspace workspace, IPath fullpath) {
		this.workspace = workspace;
		this.fullpath = fullpath;
	}

	@Override
	public IWorkspace getWorkspace() {
		return this.workspace;
	}

	@Override
	public IFolder getParent() throws IOException {
		return getWorkspace().getParent(getFullPath());
	}

	@Override
	public String getName() {
		return this.fullpath.getLastSegment();
	}

	@Override
	public IPath getFullPath() {
		return this.fullpath;
	}

	@Override
	public boolean exists() {
		return getWorkspace().underlyingResourceExists(getFullPath());
	}

	@Override
	public abstract boolean create() throws IOException;

	@Override
	public boolean delete() throws IOException {
		return getWorkspace().deleteUnderlyingResource(getFullPath());
	}

	@Override
	public void dispose() {

	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result == null) {
			if (IWorkspace.class.isAssignableFrom(adapter)) {
				result = (T) getWorkspace();
			}
		}
		if (result == null) {
			if (IPath.class.isAssignableFrom(adapter)) {
				result = (T) getFullPath();
			}
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}

// IPath fullPath = null;
// IResource parent = getParent();
// if (parent == null) {
// IRoot root = getRoot();
// if (root != null) {
// fullPath = new PathImpl(PathImpl.ROOT, this.name);
// } else {
// fullPath = new PathImpl(this.name);
// }
// } else {
// IPath parentFullPath = parent.getFullPath();
// if (parentFullPath != null) {
// fullPath = new PathImpl(parentFullPath, this.name);
// } else {
// fullPath = new PathImpl(this.name);
// }
// }

// IRoot root = getRoot();
// if (root != null) {
// return root.create(getFullPath());
// }