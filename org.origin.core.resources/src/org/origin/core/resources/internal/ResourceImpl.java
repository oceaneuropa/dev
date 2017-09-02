package org.origin.core.resources.internal;

import java.io.IOException;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.core.resources.IFolder;
import org.origin.core.resources.IPath;
import org.origin.core.resources.IResource;
import org.origin.core.resources.IRoot;

public abstract class ResourceImpl implements IResource {

	protected IRoot root;
	protected IPath fullpath;
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param root
	 * @param fullpath
	 */
	public ResourceImpl(IRoot root, IPath fullpath) {
		this.root = root;
		this.fullpath = fullpath;
	}

	@Override
	public IRoot getRoot() {
		return this.root;
	}

	@Override
	public IFolder getParent() throws IOException {
		return getRoot().getParent(getFullPath());
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
		return getRoot().underlyingResourceExists(getFullPath());
	}

	@Override
	public abstract boolean create() throws IOException;

	@Override
	public boolean delete() {
		return getRoot().delete(getFullPath());
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
			if (IRoot.class.isAssignableFrom(adapter)) {
				result = (T) getRoot();
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