package org.nb.mgm.client.api.impl;

import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.ws.ProjectNodeClient;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

public class ProjectNodeImpl implements IProjectNode {

	private Management management;
	private IProject project;
	private IProjectHome projectHome;
	private ProjectNodeDTO projectNodeDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param project
	 * @param projectHome
	 * @param projectNodeDTO
	 */
	public ProjectNodeImpl(Management management, IProject project, IProjectHome projectHome, ProjectNodeDTO projectNodeDTO) {
		this.management = management;
		this.project = project;
		this.projectHome = projectHome;
		this.projectNodeDTO = projectNodeDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		return this.management;
	}

	@Override
	public IProject getProject() {
		return this.project;
	}

	@Override
	public IProjectHome getProjectHome() {
		return this.projectHome;
	}

	// ------------------------------------------------------------------------------------------
	// Auto Update Attributes
	// ------------------------------------------------------------------------------------------
	@Override
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public boolean update() throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);
		checkProjectHome(this.projectHome);

		ProjectNodeClient projectNodeClient = this.management.getAdapter(ProjectNodeClient.class);
		checkClient(projectNodeClient);

		String projectId = this.project.getId();
		String projectHomeId = this.projectHome.getId();

		StatusDTO status = projectNodeClient.updateProjectNode(projectId, projectHomeId, this.projectNodeDTO);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.projectNodeDTO.getId();
	}

	@Override
	public String getName() {
		return this.projectNodeDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.projectNodeDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.projectNodeDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.projectNodeDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.projectNodeDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.projectNodeDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(ProjectNodeClient projectNodeClient) throws ClientException {
		if (projectNodeClient == null) {
			throw new ClientException(401, "ProjectNodeClient is not found.", null);
		}
	}

	protected void checkManagement(Management management) throws ClientException {
		if (management == null) {
			throw new ClientException(401, "management is null.", null);
		}
	}

	protected void checkProject(IProject project) throws ClientException {
		if (project == null) {
			throw new ClientException(401, "project is null.", null);
		}
	}

	protected void checkProjectHome(IProjectHome projectHome) throws ClientException {
		if (projectHome == null) {
			throw new ClientException(401, "projectHome is null.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ProjectNodeDTO.class.equals(adapter)) {
			return (T) this.projectNodeDTO;
		}
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ProjectNode(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
