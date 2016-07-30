package org.nb.mgm.client.api.impl;

import java.util.List;

import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.ProjectNodeDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class ProjectNodeImpl implements IProjectNode {

	private ManagementClient management;
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
	public ProjectNodeImpl(ManagementClient management, IProject project, IProjectHome projectHome, ProjectNodeDTO projectNodeDTO) {
		this.management = management;
		this.project = project;
		this.projectHome = projectHome;
		this.projectNodeDTO = projectNodeDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public ManagementClient getManagement() {
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

		String projectId = this.project.getId();
		String projectHomeId = this.projectHome.getId();
		return this.management.updateProjectNode(projectId, projectHomeId, this.projectNodeDTO);
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
	// Software
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ISoftware> getSoftware() throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);
		checkProjectHome(this.projectHome);

		String projectId = this.project.getId();
		String projectHomeId = this.projectHome.getId();
		String projectNodeId = this.getId();
		return this.management.getProjectNodeSoftware(projectId, projectHomeId, projectNodeId);
	}

	@Override
	public boolean installSoftware(String softwareId) throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);
		checkProjectHome(this.projectHome);

		String projectId = this.project.getId();
		String projectHomeId = this.projectHome.getId();
		String projectNodeId = this.getId();
		return this.management.installProjectNodeSoftware(projectId, projectHomeId, projectNodeId, softwareId);
	}

	@Override
	public boolean uninstallSoftware(String softwareId) throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);
		checkProjectHome(this.projectHome);

		String projectId = this.project.getId();
		String projectHomeId = this.projectHome.getId();
		String projectNodeId = this.getId();
		return this.management.uninstallProjectNodeSoftware(projectId, projectHomeId, projectNodeId, softwareId);
	}

	// ------------------------------------------------------------------------------------------
	// Check Management Client
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param management
	 * @throws ClientException
	 */
	protected void checkManagement(ManagementClient management) throws ClientException {
		if (management == null) {
			throw new ClientException(401, "management is null.", null);
		}
	}

	/**
	 * 
	 * @param project
	 * @throws ClientException
	 */
	protected void checkProject(IProject project) throws ClientException {
		if (project == null) {
			throw new ClientException(401, "project is null.", null);
		}
	}

	/**
	 * 
	 * @param projectHome
	 * @throws ClientException
	 */
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
