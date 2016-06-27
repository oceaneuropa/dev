package org.nb.mgm.service;

import java.util.List;
import java.util.Map;

import org.nb.mgm.exception.MgmException;
import org.nb.mgm.model.query.ArtifactQuery;
import org.nb.mgm.model.query.HomeQuery;
import org.nb.mgm.model.query.MachineQuery;
import org.nb.mgm.model.query.MetaSectorQuery;
import org.nb.mgm.model.query.MetaSpaceQuery;
import org.nb.mgm.model.runtime.Artifact;
import org.nb.mgm.model.runtime.ClusterRoot;
import org.nb.mgm.model.runtime.Home;
import org.nb.mgm.model.runtime.Machine;
import org.nb.mgm.model.runtime.MetaSector;
import org.nb.mgm.model.runtime.MetaSpace;
import org.nb.mgm.model.runtime.Project;
import org.nb.mgm.model.runtime.ProjectHomeConfig;
import org.nb.mgm.model.runtime.ProjectNodeConfig;

public interface ManagementService {

	// ------------------------------------------------------------------------------------------
	// Save
	// ------------------------------------------------------------------------------------------
	public boolean isAutoSave();

	public void setAutoSave(boolean autoSave);

	public void save();

	// ------------------------------------------------------------------------------------------
	// Root
	// ------------------------------------------------------------------------------------------
	public ClusterRoot getRoot();

	// ------------------------------------------------------------------------------------------
	// Namespace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Namespaces.
	 * 
	 * @return
	 * @throws MgmException
	 */
	// public List<Namespace> getNamespaces() throws MgmException;

	/**
	 * Get Namespace detailed information by namespace name.
	 * 
	 * @param namespace
	 * @return
	 * @throws MgmException
	 */
	// public Namespace getNamespace(String namespace) throws MgmException;

	/**
	 * Add a Namespace.
	 * 
	 * @param namespace
	 * @throws MgmException
	 */
	// public void addNamespace(Namespace namespace) throws MgmException;

	/**
	 * Update Namespace detailed information.
	 * 
	 * @param namespace
	 * @throws MgmException
	 */
	// public void updateNamespace(Namespace namespace) throws MgmException;

	/**
	 * Remove a Namespace by namespace name.
	 * 
	 * @param namespace
	 * @throws MgmException
	 */
	// public void deleteNamespace(String namespace) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Machines.
	 * 
	 * @return
	 * @throws MgmException
	 */
	public List<Machine> getMachines() throws MgmException;

	/**
	 * Get Machines by query.
	 * 
	 * @param query
	 * @return
	 * @throws MgmException
	 */
	public List<Machine> getMachines(MachineQuery query) throws MgmException;

	/**
	 * Get Machine detailed information by Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Machine getMachine(String machineId) throws MgmException;

	/**
	 * Add a Machine.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	public void addMachine(Machine machine) throws MgmException;

	/**
	 * Update Machine information.
	 * 
	 * @param machine
	 * @throws MgmException
	 */
	public void updateMachine(Machine machine) throws MgmException;

	/**
	 * Remove a Machine.
	 * 
	 * @param machineId
	 * @throws MgmException
	 */
	public void deleteMachine(String machineId) throws MgmException;

	/**
	 * Get Machine properties.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Map<String, Object> getMachineProperties(String machineId) throws MgmException;

	/**
	 * Set Machine properties.
	 * 
	 * @param machineId
	 * @param properties
	 * @throws MgmException
	 */
	public boolean setMachineProperties(String machineId, Map<String, Object> properties) throws MgmException;

	/**
	 * Remove Machine properties.
	 * 
	 * @param machineId
	 * @param propNames
	 * @throws MgmException
	 */
	public boolean removeMachineProperties(String machineId, List<String> propNames) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 */
	public List<Home> getHomes(String machineId) throws MgmException;

	/**
	 * Get Homes in a Machine by query.
	 * 
	 * @param machineId
	 * @param query
	 * @return
	 */
	public List<Home> getHomes(String machineId, HomeQuery query) throws MgmException;

	/**
	 * Get Home information by Id.
	 * 
	 * @param homeId
	 * @return
	 */
	public Home getHome(String homeId) throws MgmException;

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param home
	 * @throws MgmException
	 */
	public void addHome(String machineId, Home home) throws MgmException;

	/**
	 * Update Home information.
	 * 
	 * @param home
	 * @throws MgmException
	 */
	public void updateHome(Home home) throws MgmException;

	/**
	 * Delete a Home from a Machine.
	 * 
	 * @param homeId
	 * @throws MgmException
	 */
	public void deleteHome(String homeId) throws MgmException;

	/**
	 * Get Home properties.
	 * 
	 * @param homeId
	 * @return
	 * @throws MgmException
	 */
	public Map<String, Object> getHomeProperties(String homeId) throws MgmException;

	/**
	 * Set Home properties.
	 * 
	 * @param homeId
	 * @param properties
	 * @throws MgmException
	 */
	public boolean setHomeProperties(String homeId, Map<String, Object> properties) throws MgmException;

	/**
	 * Remove Home properties.
	 * 
	 * @param homeId
	 * @param propNames
	 * @throws MgmException
	 */
	public boolean removeHomeProperties(String homeId, List<String> propNames) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 */
	public List<MetaSector> getMetaSectors() throws MgmException;

	/**
	 * Get MetaSectors by query.
	 * 
	 * @param query
	 * @return
	 */
	public List<MetaSector> getMetaSectors(MetaSectorQuery query) throws MgmException;

	/**
	 * Get MetaSector information by Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws MgmException
	 */
	public MetaSector getMetaSector(String metaSectorId) throws MgmException;

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	public void addMetaSector(MetaSector metaSector) throws MgmException;

	/**
	 * Update MetaSector information.
	 * 
	 * @param metaSector
	 * @throws MgmException
	 */
	public void updateMetaSector(MetaSector metaSector) throws MgmException;

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws MgmException
	 */
	public void deleteMetaSector(String metaSectorId) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws MgmException;

	/**
	 * Get MetaSpaces in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId, MetaSpaceQuery query) throws MgmException;

	/**
	 * Get MetaSpace information by Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 */
	public MetaSpace getMetaSpace(String metaSpaceId) throws MgmException;

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 * @throws MgmException
	 */
	public void addMetaSpace(String metaSectorId, MetaSpace metaSpace) throws MgmException;

	/**
	 * Update MetaSpace information.
	 * 
	 * @param metaSpace
	 * @throws MgmException
	 */
	public void updateMetaSpace(MetaSpace metaSpace) throws MgmException;

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSpaceId
	 * @throws MgmException
	 */
	public void deleteMetaSpace(String metaSpaceId) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// Artifact
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Artifacts in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId) throws MgmException;

	/**
	 * Get Artifacts in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId, ArtifactQuery query) throws MgmException;

	/**
	 * Get Artifact information by Id.
	 * 
	 * @param artifactId
	 * @return
	 */
	public Artifact getArtifact(String artifactId) throws MgmException;

	/**
	 * Add a Artifact to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param artifact
	 * @throws MgmException
	 */
	public void addArtifact(String metaSectorId, Artifact artifact) throws MgmException;

	/**
	 * Update Artifact information.
	 * 
	 * @param artifact
	 * @throws MgmException
	 */
	public void updateArtifact(Artifact artifact) throws MgmException;

	/**
	 * Delete a Artifact from a MetaSector.
	 * 
	 * @param artifactId
	 * @throws MgmException
	 */
	public void deleteArtifact(String artifactId) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Projects.
	 * 
	 * @return
	 * @throws MgmException
	 */
	public List<Project> getProjects() throws MgmException;

	/**
	 * Get Project detailed information by Id.
	 * 
	 * @param projectId
	 * @return
	 * @throws MgmException
	 */
	public Project getProject(String projectId) throws MgmException;

	/**
	 * Add a Project.
	 * 
	 * @param project
	 * @throws MgmException
	 */
	public void addProject(Project project) throws MgmException;

	/**
	 * Update Project information.
	 * 
	 * @param project
	 * @throws MgmException
	 */
	public void updateProject(Project project) throws MgmException;

	/**
	 * Remove a Project.
	 * 
	 * @param projectId
	 * @throws MgmException
	 */
	public boolean deleteProject(String projectId) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// ProjectHomeConfig
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all ProjectHomeConfigs in a Project.
	 * 
	 * @param projectId
	 * @return
	 */
	public List<ProjectHomeConfig> getProjectHomeConfigs(String projectId) throws MgmException;

	/**
	 * Get ProjectHomeConfig information by Id.
	 * 
	 * @param homeConfigId
	 * @return
	 */
	public ProjectHomeConfig getProjectHomeConfig(String homeConfigId) throws MgmException;

	/**
	 * Add a ProjectHomeConfig to a Project.
	 * 
	 * @param projectId
	 * @param homeConfig
	 * @throws MgmException
	 */
	public void addProjectHomeConfig(String projectId, ProjectHomeConfig homeConfig) throws MgmException;

	/**
	 * Update ProjectHomeConfig information.
	 * 
	 * @param homeConfig
	 * @throws MgmException
	 */
	public void updateProjectHomeConfig(ProjectHomeConfig homeConfig) throws MgmException;

	/**
	 * Delete a ProjectHomeConfig from a Project.
	 * 
	 * @param homeConfigId
	 * @throws MgmException
	 */
	public boolean deleteProjectHomeConfig(String homeConfigId) throws MgmException;

	// ------------------------------------------------------------------------------------------
	// ProjectNodeConfig
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all ProjectNodeConfigs in a Project.
	 * 
	 * @param homeConfigId
	 * @return
	 */
	public List<ProjectNodeConfig> getProjectNodeConfigs(String homeConfigId) throws MgmException;

	/**
	 * Get ProjectNodeConfig information by Id.
	 * 
	 * @param nodeConfigId
	 * @return
	 */
	public ProjectNodeConfig getProjectNodeConfig(String nodeConfigId) throws MgmException;

	/**
	 * Add a ProjectNodeConfig to a Project.
	 * 
	 * @param homeConfigId
	 * @param nodeConfig
	 * @throws MgmException
	 */
	public void addProjectNodeConfig(String homeConfigId, ProjectNodeConfig nodeConfig) throws MgmException;

	/**
	 * Update ProjectNodeConfig information.
	 * 
	 * @param nodeConfig
	 * @throws MgmException
	 */
	public void updateProjectNodeConfig(ProjectNodeConfig nodeConfig) throws MgmException;

	/**
	 * Delete a ProjectNodeConfig from a ProjectHomeConfig.
	 * 
	 * @param nodeConfigId
	 * @throws MgmException
	 */
	public boolean deleteProjectNodeConfig(String nodeConfigId) throws MgmException;

}
