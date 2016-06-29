package org.nb.mgm.client.api.impl;

import java.util.List;
import java.util.Map;

import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.Project;
import org.nb.mgm.client.api.ProjectHomeConfig;
import org.nb.mgm.client.ws.HomeClient;
import org.nb.mgm.client.ws.ProjectHomeConfigClient;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.ProjectHomeConfigDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

public class ProjectHomeConfigImpl implements ProjectHomeConfig {

	private boolean autoUpdate = false;
	private Project project;
	private ProjectHomeConfigDTO homeConfigDTO;
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param homeConfigDTO
	 */
	public ProjectHomeConfigImpl(ProjectHomeConfigDTO homeConfigDTO) {
		this.homeConfigDTO = homeConfigDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		return this.project.getManagement();
	}

	@Override
	public Project getProject() {
		return this.project;
	}

	public void setProject(Project project) {
		this.project = project;
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
		Management management = this.project.getManagement();
		String projectId = this.project.getId();

		ProjectHomeConfigClient homeClient = management.getAdapter(ProjectHomeConfigClient.class);
//		checkClient(homeClient);
//
//		StatusDTO status = homeClient.updateHome(projectId, this.homeDTO);
//		return (status != null && status.success()) ? true : false;
		return false;
	}

	@Override
	public String getId() {
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void setName(String name) throws ClientException {

	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String description) throws ClientException {

	}

	@Override
	public Map<String, Object> getProperties() throws ClientException {
		return null;
	}

	@Override
	public boolean setProperty(String propName, Object propValue) throws ClientException {
		return false;
	}

	@Override
	public boolean setProperties(Map<String, Object> properties) throws ClientException {
		return false;
	}

	@Override
	public boolean removeProperty(String propertyName) throws ClientException {
		return false;
	}

	@Override
	public boolean removeProperties(List<String> propertyNames) throws ClientException {
		return false;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		// TODO Auto-generated method stub
		return null;
	}

}
