package org.origin.core.workspace.impl;

import java.io.File;

import org.origin.common.adapter.AdaptorSupport;
import org.origin.core.workspace.IContainer;
import org.origin.core.workspace.IProject;
import org.origin.core.workspace.IResource;
import org.origin.core.workspace.IWorkspace;

public abstract class ResourceImpl implements IResource {

	protected static IProject[] EMPTY_PROJECTS = new IProject[0];
	protected static IResource[] EMPTY_MEMBERS = new IResource[0];

	protected File file;
	protected IWorkspace workspace;
	protected IContainer parent;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	public ResourceImpl() {
	}

	/**
	 * 
	 * @param file
	 */
	public ResourceImpl(File file) {
		this.file = file;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Life Cycle
	// --------------------------------------------------------------------------------------------------------------
	@Override
	public void delete() {
		if (exists()) {
			getFile().delete();
		}
	}

	// --------------------------------------------------------------------------------------------------------------
	// Containers and Contents
	// --------------------------------------------------------------------------------------------------------------
	@Override
	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public IWorkspace getWorkspace() {
		if (this.workspace != null) {
			return this.workspace;
		}
		IContainer parent = getParent();
		if (parent != null) {
			return parent.getWorkspace();
		}
		return null;
	}

	public void setWorkspace(IWorkspace workspace) {
		this.workspace = workspace;
	}

	protected void checkWorkspace(IWorkspace workspace) {
		if (workspace == null) {
			throw new RuntimeException("Workspace object is not available.");
		}
	}

	@Override
	public IProject getProject() {
		IProject project = null;
		if (this instanceof IProject) {
			return (IProject) this;
		} else {
			IContainer parent = getParent();
			if (parent != null) {
				project = parent.getProject();
			}
		}
		return project;
	}

	@Override
	public IContainer getParent() {
		return this.parent;
	}

	public void setParent(IContainer parent) {
		this.parent = parent;
	}

	@Override
	public String getName() {
		return this.file != null ? this.file.getName() : null;
	}

	@Override
	public boolean exists() {
		return this.file != null ? this.file.exists() : false;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Natures
	// --------------------------------------------------------------------------------------------------------------
	protected ResourceNatureHandler natureHandler;

	protected ResourceNatureHandler createNatureHandler() {
		return new ResourceNatureHandler();
	}

	public synchronized ResourceNatureHandler getNatureHandler() {
		if (this.natureHandler != null) {
			this.natureHandler = createNatureHandler();
		}
		return this.natureHandler;
	}

	public void setNatureHandler(ResourceNatureHandler natureHandler) {
		this.natureHandler = natureHandler;
	}

	// --------------------------------------------------------------------------------------------------------------
	// Adapter
	// --------------------------------------------------------------------------------------------------------------
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		if (IWorkspace.class.isAssignableFrom(adapter)) {
			IWorkspace workspace = getWorkspace();
			if (workspace != null) {
				return (T) workspace;
			}
		}
		if (File.class.isAssignableFrom(adapter)) {
			File file = getFile();
			if (file != null) {
				return (T) file;
			}
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

}
