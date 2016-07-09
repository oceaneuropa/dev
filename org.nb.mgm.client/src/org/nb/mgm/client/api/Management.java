package org.nb.mgm.client.api;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.origin.common.adapter.IAdaptable;
import org.origin.common.rest.client.ClientException;

public interface Management extends IAdaptable {

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Machines.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IMachine> getMachines() throws ClientException;

	/**
	 * Get Machines.
	 * 
	 * @param properties
	 *            supported keys are: "name", "ipaddress", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<IMachine> getMachines(Map<String, ?> properties) throws ClientException;

	/**
	 * Get a Machine.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public IMachine getMachine(String machineId) throws ClientException;

	/**
	 * Add a Machine.
	 * 
	 * @param name
	 * @param ipAddress
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IMachine addMachine(String name, String ipAddress, String description) throws ClientException;

	/**
	 * Remove a Machine.
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	public boolean removeMachine(String machineId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public List<IHome> getHomes(String machineId) throws ClientException;

	/**
	 * Get Homes in a Machine.
	 * 
	 * @param machineId
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<IHome> getHomes(String machineId, Properties properties) throws ClientException;

	/**
	 * Get a Home.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public IHome getHome(String machineId, String homeId) throws ClientException;

	/**
	 * Get a Home.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public IHome getHome(String homeId) throws ClientException;

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IHome addHome(String machineId, String name, String description) throws ClientException;

	/**
	 * Remove a Home from a Machine.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean removeHome(String machineId, String homeId) throws ClientException;

	/**
	 * Remove a Home from a Machine.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean removeHome(String homeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	/**
	 * Get MetaSectors.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSector> getMetaSectors() throws ClientException;

	/**
	 * Get MetaSectors.
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSector> getMetaSectors(Properties properties) throws ClientException;

	/**
	 * Get a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public IMetaSector getMetaSector(String metaSectorId) throws ClientException;

	/**
	 * Add a MetaSector.
	 * 
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IMetaSector addMetaSector(String name, String description) throws ClientException;

	/**
	 * Delete MetaSector.
	 * 
	 * @param metaSectorId
	 * @throws ClientException
	 */
	public boolean deleteMetaSector(String metaSectorId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSpace> getMetaSpaces(String metaSectorId) throws ClientException;

	/**
	 * Get MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IMetaSpace> getMetaSpaces(String metaSectorId, Properties properties) throws ClientException;

	/**
	 * Get a MetaSpace.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public IMetaSpace getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException;

	/**
	 * Get a MetaSpace.
	 * 
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public IMetaSpace getMetaSpace(String metaSpaceId) throws ClientException;

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IMetaSpace addMetaSpace(String metaSectorId, String name, String description) throws ClientException;

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException;

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteMetaSpace(String metaSpaceId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Projects.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<IProject> getProjects() throws ClientException;

	/**
	 * Get a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws ClientException
	 */
	public IProject getProject(String projectId) throws ClientException;

	/**
	 * Add a Project.
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IProject addProject(String projectId, String name, String description) throws ClientException;

	/**
	 * Delete a Project.
	 * 
	 * @param projectId
	 * @throws ClientException
	 */
	public boolean deleteProject(String projectId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	/**
	 * Get ProjectHomes in a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws ClientException
	 */
	public List<IProjectHome> getProjectHomes(String projectId) throws ClientException;

	/**
	 * Get a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectHome getProjectHome(String projectId, String projectHomeId) throws ClientException;

	/**
	 * Get a ProjectHome.
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectHome getProjectHome(String projectHomeId) throws ClientException;

	/**
	 * Add a ProjectHome to a Project.
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IProjectHome addProjectHome(String projectId, String name, String description) throws ClientException;

	/**
	 * Delete a ProjectHome from a Project.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectHome(String projectId, String projectHomeId) throws ClientException;

	/**
	 * Delete a ProjectHome from a Project.
	 * 
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectHome(String projectHomeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	/**
	 * Get ProjectNodes in a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 * @throws ClientException
	 */
	public List<IProjectNode> getProjectNodes(String projectId, String projectHomeId) throws ClientException;

	/**
	 * Get a ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException;

	/**
	 * Get a ProjectNode.
	 * 
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public IProjectNode getProjectNode(String projectNodeId) throws ClientException;

	/**
	 * Add a ProjectNode to a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public IProjectNode addProjectNode(String projectId, String projectHomeId, String projectNodeId, String name, String description) throws ClientException;

	/**
	 * Delete a ProjectNode from a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ClientException;

	/**
	 * Delete a ProjectNode from a ProjectHome.
	 * 
	 * @param projectNodeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectNode(String projectNodeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectSoftware
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Software in a Project.
	 * 
	 * @param projectId
	 * @return
	 * @throws ClientException
	 */
	public List<ISoftware> getProjectSoftwareList(String projectId) throws ClientException;

	/**
	 * Get Software.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	public ISoftware getProjectSoftware(String projectId, String softwareId) throws ClientException;

	/**
	 * Get Software.
	 * 
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	public ISoftware getProjectSoftware(String softwareId) throws ClientException;

	/**
	 * Add Software to a Project.
	 * 
	 * @param projectId
	 * @param type
	 * @param name
	 * @param version
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public ISoftware addProjectSoftware(String projectId, String type, String name, String version, String description) throws ClientException;

	/**
	 * Delete Software from a Project.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectSoftware(String projectId, String softwareId) throws ClientException;

	/**
	 * Delete Software from a Project.
	 * 
	 * @param softewareId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteProjectSoftware(String softewareId) throws ClientException;

}
