package org.nb.mgm.client.api.impl;

import java.util.List;

import org.nb.mgm.client.api.IProject;
import org.nb.mgm.client.api.IProjectHome;
import org.nb.mgm.client.api.ISoftware;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.ProjectDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class ProjectImpl implements IProject {

	private boolean autoUpdate = false;
	private ManagementClient management;
	private ProjectDTO projectDTO;
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param management
	 * @param projectDTO
	 */
	public ProjectImpl(ManagementClient management, ProjectDTO projectDTO) {
		this.management = management;
		this.projectDTO = projectDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public ManagementClient getManagement() {
		return management;
	}

	public void setManagement(ManagementClient management) {
		this.management = management;
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
		return this.management.updateProject(this.projectDTO);
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.projectDTO.getId();
	}

	@Override
	public String getName() {
		return this.projectDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.projectDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.projectDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.projectDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.projectDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.projectDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	@Override
	public List<IProjectHome> getProjectHomes() throws ClientException {
		checkManagement(this.management);
		return this.management.getProjectHomes(getId());
	}

	@Override
	public IProjectHome getProjectHome(String projectHomeId) throws ClientException {
		checkManagement(this.management);
		return this.management.getProjectHome(getId(), projectHomeId);
	}

	@Override
	public IProjectHome addProjectHome(String name, String description) throws ClientException {
		checkManagement(this.management);
		return this.management.addProjectHome(getId(), name, description);
	}

	@Override
	public boolean deleteProjectHome(String projectHomeId) throws ClientException {
		checkManagement(this.management);
		return this.management.deleteProjectHome(getId(), projectHomeId);
	}

	// ------------------------------------------------------------------------------------------
	// ProjectSoftware
	// ------------------------------------------------------------------------------------------
	@Override
	public List<ISoftware> getSoftware() throws ClientException {
		checkManagement(this.management);
		return this.management.getProjectSoftwareList(getId());
	}

	@Override
	public ISoftware getSoftware(String softwareId) throws ClientException {
		checkManagement(this.management);
		return this.management.getProjectSoftware(getId(), softwareId);
	}

	@Override
	public ISoftware addSoftware(String type, String name, String version, String description) throws ClientException {
		checkManagement(this.management);
		return this.management.addProjectSoftware(getId(), type, name, version, description);
	}

	@Override
	public boolean deleteSoftware(String softwareId) throws ClientException {
		checkManagement(this.management);
		return this.management.deleteProjectSoftware(getId(), softwareId);
	}

	// ------------------------------------------------------------------------------------------
	// Check Management Client
	// ------------------------------------------------------------------------------------------
	/**
	 * @param management
	 * @throws ClientException
	 */
	protected void checkManagement(ManagementClient management) throws ClientException {
		if (management == null) {
			throw new ClientException(401, "management is null.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (ProjectDTO.class.equals(adapter)) {
			return (T) this.projectDTO;
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
		sb.append("Project(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
