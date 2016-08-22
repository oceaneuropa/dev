package org.nb.mgm.client.api.impl;

import java.util.List;

import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.IProjectNode;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.ProjectHomeDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class ProjectHomeImpl implements IProjectHome {

	private ManagementClient management;
	private IProject project;
	private ProjectHomeDTO projectHomeDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param project
	 * @param projectHomeDTO
	 */
	public ProjectHomeImpl(ManagementClient management, IProject project, ProjectHomeDTO projectHomeDTO) {
		this.management = management;
		this.project = project;
		this.projectHomeDTO = projectHomeDTO;
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

		String projectId = this.project.getId();
		return this.management.updateProjectHome(projectId, this.projectHomeDTO);
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.projectHomeDTO.getId();
	}

	@Override
	public String getName() {
		return this.projectHomeDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.projectHomeDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.projectHomeDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.projectHomeDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.projectHomeDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.projectHomeDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// Deployment Home
	// ------------------------------------------------------------------------------------------
	@Override
	public IHome getDeploymentHome() throws ClientException {
		return this.management.getProjectDeploymentHome(getId());
	}

	@Override
	public boolean setDeploymentHome(String homeId) throws ClientException {
		return this.management.setProjectDeploymentHome(getId(), homeId);
	}

	@Override
	public boolean removeDeploymentHome(String homeId) throws ClientException {
		return this.management.removeProjectDeploymentHome(getId(), homeId);
	}

//	@Override
//	public boolean connect() throws ClientException {
//		IHome home = getDeploymentHome();
//		checkDeploymentHome(home);
//
//		this.management.connect(home);
//		return this.management.isConnected(home);
//	}
//
//	@Override
//	public boolean disconnect() throws ClientException {
//		IHome home = getDeploymentHome();
//		checkDeploymentHome(home);
//
//		this.management.disconnect(home);
//		return this.management.isConnected(home) ? false : true;
//	}
//
//	@Override
//	public boolean isConnected() throws ClientException {
//		IHome home = getDeploymentHome();
//		checkDeploymentHome(home);
//
//		return this.management.isConnected(home);
//	}
//
//	@Override
//	public boolean isHomeAgentActive() throws ClientException {
//		IHome home = getDeploymentHome();
//		checkDeploymentHome(home);
//
//		return this.management.isHomeAgentActive(home);
//	}

	/**
	 * 
	 * @param home
	 * @throws ClientException
	 */
	protected void checkDeploymentHome(IHome home) throws ClientException {
		if (home == null) {
			throw new ClientException(500, "ProjectHome is not configured with deployment home.");
		}
	}

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IProjectNode> getProjectNodes() throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);

		String projectId = this.project.getId();
		String projectHomeId = getId();
		return this.management.getProjectNodes(projectId, projectHomeId);
	}

	@Override
	public IProjectNode getProjectNode(String projectNodeId) throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);

		String projectId = this.project.getId();
		String projectHomeId = getId();
		return this.management.getProjectNode(projectId, projectHomeId, projectNodeId);
	}

	@Override
	public IProjectNode addProjectNode(String projectNodeId, String name, String description) throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);

		String projectId = this.project.getId();
		String projectHomeId = getId();
		return this.management.addProjectNode(projectId, projectHomeId, projectNodeId, name, description);
	}

	@Override
	public boolean deleteProjectNode(String projectNodeId) throws ClientException {
		checkManagement(this.management);
		checkProject(this.project);

		String projectId = this.project.getId();
		String projectHomeId = getId();
		return this.management.deleteProjectNode(projectId, projectHomeId, projectNodeId);
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

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ProjectHomeDTO.class.equals(adapter)) {
			return (T) this.projectHomeDTO;
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
		sb.append("ProjectHome(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
