package org.nb.home.service.impl;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.nb.home.handler.ProjectHandler;
import org.nb.home.handler.WorkspaceHandler;
import org.nb.home.model.HomeConstants;
import org.nb.home.model.exception.HomeException;
import org.nb.home.model.runtime.Workspace;
import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.nb.home.service.HomeAgentService;
import org.origin.common.command.IEditingDomain;
import org.origin.common.util.PropertyUtil;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

public class HomeAgentServiceImpl implements HomeAgentService {

	protected Map<Object, Object> props = new Hashtable<Object, Object>();
	protected BundleContext bundleContext;
	protected ServiceRegistration<?> serviceReg;

	protected WorkspaceHandler workspaceHandler;
	protected ProjectHandler projectHandler;

	// read/write lock for data handlers
	protected ReadWriteLock workspaceRWLock = new ReentrantReadWriteLock();
	protected ReadWriteLock projectRWLock = new ReentrantReadWriteLock();

	protected IEditingDomain editingDomain;

	/**
	 * 
	 * @param bundleContext
	 */
	public HomeAgentServiceImpl(BundleContext bundleContext) {
		this.bundleContext = bundleContext;

		// load properties
		PropertyUtil.loadProperty(this.bundleContext, this.props, HomeConstants.AGENT_HOME_DIR);
		PropertyUtil.loadProperty(this.bundleContext, this.props, HomeConstants.PERSISTENCE_TYPE);
		PropertyUtil.loadProperty(this.bundleContext, this.props, HomeConstants.PERSISTENCE_AUTOSAVE);

		// Create the handlers
		this.workspaceHandler = new WorkspaceHandler(this, this.props);
		this.projectHandler = new ProjectHandler(this, this.props);
	}

	/**
	 * Start HomeAgentService
	 * 
	 */
	public void start() {
		this.editingDomain = IEditingDomain.getEditingDomain(HomeAgentService.class.getName());

		// Register HomeAgentService
		Hashtable<String, Object> props = new Hashtable<String, Object>();
		this.serviceReg = this.bundleContext.registerService(HomeAgentService.class, this, props);
	}

	/**
	 * Stop HomeAgentService
	 * 
	 */
	public void stop() {
		// Unregister HomeAgentService
		if (this.serviceReg != null) {
			this.serviceReg.unregister();
			this.serviceReg = null;
		}

		this.editingDomain = null;
		IEditingDomain.disposeEditingDomain(HomeAgentService.class.getName());
	}

	// ------------------------------------------------------------------------------------------
	// EditingDomain
	// ------------------------------------------------------------------------------------------
	public IEditingDomain getEditingDomain() {
		return this.editingDomain;
	}

	// ------------------------------------------------------------------------------------------
	// ping
	// ------------------------------------------------------------------------------------------
	@Override
	public int ping() throws HomeException {
		return 1;
	}

	// ------------------------------------------------------------------------------------------
	// Workspace
	// ------------------------------------------------------------------------------------------
	@Override
	public List<Workspace> getWorkspaces(String managementId) throws HomeException {
		this.workspaceRWLock.readLock().lock();
		try {
			return this.workspaceHandler.getWorkspaces(managementId);
		} finally {
			this.workspaceRWLock.readLock().unlock();
		}
	}

	@Override
	public Workspace getWorkspace(String managementId, String name) throws HomeException {
		this.workspaceRWLock.readLock().lock();
		try {
			return this.workspaceHandler.getWorkspace(managementId, name);
		} finally {
			this.workspaceRWLock.readLock().unlock();
		}
	}

	@Override
	public Workspace createWorkspace(WorkspaceConfig newWorkspaceRequest) throws HomeException {
		this.workspaceRWLock.writeLock().lock();
		try {
			return this.workspaceHandler.createWorkspace(newWorkspaceRequest);
		} finally {
			this.workspaceRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteWorkspace(String managementId, String name) throws HomeException {
		this.workspaceRWLock.writeLock().lock();
		try {
			return this.workspaceHandler.deleteWorkspace(managementId, name);
		} finally {
			this.workspaceRWLock.writeLock().unlock();
		}
	}

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	@Override
	public boolean projectExists(String projectId) throws HomeException {
		this.projectRWLock.readLock().lock();
		try {
			return this.projectHandler.projectExists(projectId);
		} finally {
			this.projectRWLock.readLock().unlock();
		}
	}

	@Override
	public boolean createProject(String projectId) throws HomeException {
		this.projectRWLock.writeLock().lock();
		try {
			return this.projectHandler.createProject(projectId);
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

	@Override
	public boolean deleteProject(String projectId) throws HomeException {
		this.projectRWLock.writeLock().lock();
		try {
			return this.projectHandler.deleteProject(projectId);
		} finally {
			this.projectRWLock.writeLock().unlock();
		}
	}

}
