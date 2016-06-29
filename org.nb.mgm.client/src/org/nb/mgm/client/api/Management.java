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
	 * Get all Machines.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<Machine> getMachines() throws ClientException;

	/**
	 * Get Machines by query properties.
	 * 
	 * @param properties
	 *            supported keys are: "name", "ipaddress", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<Machine> getMachines(Map<String, ?> properties) throws ClientException;

	/**
	 * Get Machine by machine Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public Machine getMachine(String machineId) throws ClientException;

	/**
	 * Add a Machine to the cluster.
	 * 
	 * @param name
	 * @param ipAddress
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public Machine addMachine(String name, String ipAddress, String description) throws ClientException;

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	public boolean deleteMachine(String machineId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	public List<Home> getHomes(String machineId) throws ClientException;

	/**
	 * Get Homes in a Machine by query parameters.
	 * 
	 * @param machineId
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	public List<Home> getHomes(String machineId, Properties properties) throws ClientException;

	/**
	 * Get Home by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public Home getHome(String homeId) throws ClientException;

	/**
	 * Get Home by machine Id and home Id.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public Home getHome(String machineId, String homeId) throws ClientException;

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param name
	 * @param url
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public Home addHome(String machineId, String name, String url, String description) throws ClientException;

	/**
	 * Delete a Home from a Machine by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteHome(String homeId) throws ClientException;

	/**
	 * Delete a Home from a Machine by machine Id and home Id.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteHome(String machineId, String homeId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSector> getMetaSectors() throws ClientException;

	/**
	 * Get MetaSectors by query parameters.
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSector> getMetaSectors(Properties properties) throws ClientException;

	/**
	 * Get MetaSector by metaSector Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public MetaSector getMetaSector(String metaSectorId) throws ClientException;

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public MetaSector addMetaSector(String name, String description) throws ClientException;

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws ClientException
	 */
	public boolean deleteMetaSector(String metaSectorId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws ClientException;

	/**
	 * Get MetaSpaces in a MetaSector by query parameters.
	 * 
	 * @param metaSectorId
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId, Properties properties) throws ClientException;

	/**
	 * Get MetaSpace by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSpace getMetaSpace(String metaSpaceId) throws ClientException;

	/**
	 * Get MetaSpace by metaSector Id and metaSpace Id.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public MetaSpace getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException;

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public MetaSpace addMetaSpace(String metaSectorId, String name, String description) throws ClientException;

	/**
	 * Delete a MetaSpace from a MetaSector by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteMetaSpace(String metaSpaceId) throws ClientException;

	/**
	 * Delete a MetaSpace from a MetaSector by metaSector Id and metaSpace Id.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Projects.
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<Project> getProjects() throws ClientException;

	/**
	 * Get Project by Id.
	 * 
	 * @param projectId
	 * @return
	 * @throws ClientException
	 */
	public Project getProject(String projectId) throws ClientException;

	/**
	 * Add a Project.
	 * 
	 * @param projectId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	public Project addProject(String projectId, String name, String description) throws ClientException;

	/**
	 * Delete a Project.
	 * 
	 * @param projectId
	 * @throws ClientException
	 */
	public boolean deleteProject(String projectId) throws ClientException;

	// ------------------------------------------------------------------------------------------
	// ProjectHomeConfig
	// ------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------
	// ProjectNodeConfig
	// ------------------------------------------------------------------------------------------

	// ------------------------------------------------------------------------------------------
	// ProjectSoftwareConfig
	// ------------------------------------------------------------------------------------------

}
