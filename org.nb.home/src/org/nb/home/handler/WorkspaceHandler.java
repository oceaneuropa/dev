package org.nb.home.handler;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.nb.home.model.exception.HomeException;
import org.nb.home.model.runtime.Workspace;
import org.nb.home.model.runtime.config.WorkspaceConfig;
import org.nb.home.model.util.HomeSetupUtil;
import org.nb.home.service.HomeAgentService;

public class WorkspaceHandler {

	protected HomeAgentService service;
	protected Map<Object, Object> props;

	/**
	 * 
	 * @param service
	 * @param props
	 */
	public WorkspaceHandler(HomeAgentService service, Map<Object, Object> props) {
		this.service = service;
		this.props = props;
	}

	/**
	 * 
	 * @return
	 */
	public File getWorkspacesFolder() throws HomeException {
		Path homePath = HomeSetupUtil.getHomePath(this.props);
		Path workspacesPath = HomeSetupUtil.getWorkspacesPath(homePath);
		File workspacesFolder = workspacesPath.toFile();
		if (workspacesFolder.exists() && !workspacesFolder.isDirectory()) {
			throw new HomeException("500", "Workspaces file exists, but is not a directory.");
		}
		return workspacesFolder;
	}

	/**
	 * 
	 * @param managementId
	 * @return
	 * @throws HomeException
	 */
	public List<Workspace> getWorkspaces(String managementId) throws HomeException {
		File workspacesFolder = getWorkspacesFolder();

		// FileCacheUtil.getRootElement(file, clazz);

		return null;
	}

	/**
	 * 
	 * @param managementId
	 * @param name
	 * @return
	 * @throws HomeException
	 */
	public Workspace getWorkspace(String managementId, String name) throws HomeException {
		return null;
	}

	/**
	 * 
	 * @param newWorkspaceRequest
	 * @return
	 * @throws HomeException
	 */
	public Workspace createWorkspace(WorkspaceConfig newWorkspaceRequest) throws HomeException {
		return null;
	}

	/**
	 * 
	 * @param managementId
	 * @param name
	 * @throws HomeException
	 */
	public boolean deleteWorkspace(String managementId, String name) throws HomeException {
		return false;
	}

}
