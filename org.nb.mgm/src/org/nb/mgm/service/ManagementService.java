package org.nb.mgm.service;

import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.nb.mgm.model.exception.ManagementException;
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
import org.nb.mgm.model.runtime.ProjectHome;
import org.nb.mgm.model.runtime.ProjectNode;
import org.nb.mgm.model.runtime.Software;

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
	// Machine
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Machines.
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public List<Machine> getMachines() throws ManagementException;

	/**
	 * Get Machines.
	 * 
	 * @param query
	 * @return
	 * @throws ManagementException
	 */
	public List<Machine> getMachines(MachineQuery query) throws ManagementException;

	/**
	 * Get a Machine.
	 * 
	 * @param machineId
	 * @return
	 * @throws ManagementException
	 */
	public Machine getMachine(String machineId) throws ManagementException;

	/**
	 * Add a Machine.
	 * 
	 * @param newMachineRequest
	 * @return
	 * @throws ManagementException
	 */
	public Machine addMachine(Machine newMachineRequest) throws ManagementException;

	/**
	 * Update Machine.
	 * 
	 * @param machine
	 * @throws ManagementException
	 */
	public void updateMachine(Machine machine) throws ManagementException;

	/**
	 * Remove a Machine.
	 * 
	 * @param machineId
	 * @throws ManagementException
	 */
	public void deleteMachine(String machineId) throws ManagementException;

	/**
	 * Get Machine properties.
	 * 
	 * @param machineId
	 * @return
	 * @throws ManagementException
	 */
	public Map<String, Object> getMachineProperties(String machineId) throws ManagementException;

	/**
	 * Set Machine properties.
	 * 
	 * @param machineId
	 * @param properties
	 * @throws ManagementException
	 */
	public boolean setMachineProperties(String machineId, Map<String, Object> properties) throws ManagementException;

	/**
	 * Remove Machine properties.
	 * 
	 * @param machineId
	 * @param propNames
	 * @throws ManagementException
	 */
	public boolean removeMachineProperties(String machineId, List<String> propNames) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 */
	public List<Home> getHomes(String machineId) throws ManagementException;

	/**
	 * Get Homes in a Machine by query.
	 * 
	 * @param machineId
	 * @param query
	 * @return
	 */
	public List<Home> getHomes(String machineId, HomeQuery query) throws ManagementException;

	/**
	 * Get Home information by Id.
	 * 
	 * @param homeId
	 * @return
	 */
	public Home getHome(String homeId) throws ManagementException;

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param machineId
	 * @param newHomeRequest
	 * @return
	 * @throws ManagementException
	 */
	public Home addHome(String machineId, Home newHomeRequest) throws ManagementException;

	/**
	 * Update Home information.
	 * 
	 * @param home
	 * @throws ManagementException
	 */
	public void updateHome(Home home) throws ManagementException;

	/**
	 * Delete a Home from a Machine.
	 * 
	 * @param homeId
	 * @throws ManagementException
	 */
	public void deleteHome(String homeId) throws ManagementException;

	/**
	 * Get Home properties.
	 * 
	 * @param homeId
	 * @return
	 * @throws ManagementException
	 */
	public Map<String, Object> getHomeProperties(String homeId) throws ManagementException;

	/**
	 * Set Home properties.
	 * 
	 * @param homeId
	 * @param properties
	 * @throws ManagementException
	 */
	public boolean setHomeProperties(String homeId, Map<String, Object> properties) throws ManagementException;

	/**
	 * Remove Home properties.
	 * 
	 * @param homeId
	 * @param propNames
	 * @throws ManagementException
	 */
	public boolean removeHomeProperties(String homeId, List<String> propNames) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 */
	public List<MetaSector> getMetaSectors() throws ManagementException;

	/**
	 * Get MetaSectors by query.
	 * 
	 * @param query
	 * @return
	 */
	public List<MetaSector> getMetaSectors(MetaSectorQuery query) throws ManagementException;

	/**
	 * Get MetaSector information by Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ManagementException
	 */
	public MetaSector getMetaSector(String metaSectorId) throws ManagementException;

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param metaSector
	 * @throws ManagementException
	 */
	public void addMetaSector(MetaSector metaSector) throws ManagementException;

	/**
	 * Update MetaSector information.
	 * 
	 * @param metaSector
	 * @throws ManagementException
	 */
	public void updateMetaSector(MetaSector metaSector) throws ManagementException;

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws ManagementException
	 */
	public void deleteMetaSector(String metaSectorId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws ManagementException;

	/**
	 * Get MetaSpaces in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<MetaSpace> getMetaSpaces(String metaSectorId, MetaSpaceQuery query) throws ManagementException;

	/**
	 * Get MetaSpace information by Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 */
	public MetaSpace getMetaSpace(String metaSpaceId) throws ManagementException;

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpace
	 * @throws ManagementException
	 */
	public void addMetaSpace(String metaSectorId, MetaSpace metaSpace) throws ManagementException;

	/**
	 * Update MetaSpace information.
	 * 
	 * @param metaSpace
	 * @throws ManagementException
	 */
	public void updateMetaSpace(MetaSpace metaSpace) throws ManagementException;

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSpaceId
	 * @throws ManagementException
	 */
	public void deleteMetaSpace(String metaSpaceId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// Artifact
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Artifacts in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId) throws ManagementException;

	/**
	 * Get Artifacts in a MetaSector by query.
	 * 
	 * @param metaSectorId
	 * @param query
	 * @return
	 */
	public List<Artifact> getArtifacts(String metaSectorId, ArtifactQuery query) throws ManagementException;

	/**
	 * Get Artifact information by Id.
	 * 
	 * @param artifactId
	 * @return
	 */
	public Artifact getArtifact(String artifactId) throws ManagementException;

	/**
	 * Add a Artifact to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param artifact
	 * @throws ManagementException
	 */
	public void addArtifact(String metaSectorId, Artifact artifact) throws ManagementException;

	/**
	 * Update Artifact information.
	 * 
	 * @param artifact
	 * @throws ManagementException
	 */
	public void updateArtifact(Artifact artifact) throws ManagementException;

	/**
	 * Delete a Artifact from a MetaSector.
	 * 
	 * @param artifactId
	 * @throws ManagementException
	 */
	public void deleteArtifact(String artifactId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// Project
	// ------------------------------------------------------------------------------------------
	/**
	 * Get Projects.
	 * 
	 * @return
	 * @throws ManagementException
	 */
	public List<Project> getProjects() throws ManagementException;

	/**
	 * Get Project detailed information by Id.
	 * 
	 * @param projectId
	 * @return
	 * @throws ManagementException
	 */
	public Project getProject(String projectId) throws ManagementException;

	/**
	 * Add a Project.
	 * 
	 * @param newProjectRequest
	 * @return
	 * @throws ManagementException
	 */
	public Project addProject(Project newProjectRequest) throws ManagementException;

	/**
	 * Update Project information.
	 * 
	 * @param project
	 * @throws ManagementException
	 */
	public void updateProject(Project project) throws ManagementException;

	/**
	 * Remove a Project.
	 * 
	 * @param projectId
	 * @throws ManagementException
	 */
	public boolean deleteProject(String projectId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// ProjectHome
	// ------------------------------------------------------------------------------------------
	/**
	 * Get ProjectHomes from a Project.
	 * 
	 * @param projectId
	 * @return
	 */
	public List<ProjectHome> getProjectHomes(String projectId) throws ManagementException;

	/**
	 * Get ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 */
	public ProjectHome getProjectHome(String projectId, String projectHomeId) throws ManagementException;

	/**
	 * Add a ProjectHome to a Project.
	 * 
	 * @param projectId
	 * @param newProjectHomeRequest
	 * @return
	 * @throws ManagementException
	 */
	public ProjectHome addProjectHome(String projectId, ProjectHome newProjectHomeRequest) throws ManagementException;

	/**
	 * Update ProjectHome.
	 * 
	 * @param projectId
	 * @param updateProjectHomeRequest
	 * @throws ManagementException
	 */
	public void updateProjectHome(String projectId, ProjectHome updateProjectHomeRequest) throws ManagementException;

	/**
	 * Delete a ProjectHome from a Project.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @throws ManagementException
	 */
	public boolean deleteProjectHome(String projectId, String projectHomeId) throws ManagementException;

	/**
	 * Set ProjectHome's deployment Home.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param homeId
	 * @return
	 * @throws ManagementException
	 */
	public boolean setProjectDeploymentHome(String projectId, String projectHomeId, String homeId) throws ManagementException;

	/**
	 * Remove ProjectHome's deployment Home.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param homeId
	 * @return
	 * @throws ManagementException
	 */
	public boolean removeProjectDeploymentHome(String projectId, String projectHomeId, String homeId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// ProjectNode
	// ------------------------------------------------------------------------------------------
	/**
	 * Get ProjectNodes from a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @return
	 * @throws ManagementException
	 */
	public List<ProjectNode> getProjectNodes(String projectId, String projectHomeId) throws ManagementException;

	/**
	 * Get ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public ProjectNode getProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException;

	/**
	 * Add a ProjectNode to a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNode
	 * @return
	 * @throws ManagementException
	 */
	public ProjectNode addProjectNode(String projectId, String projectHomeId, ProjectNode projectNode) throws ManagementException;

	/**
	 * Update ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNode
	 * @throws ManagementException
	 */
	public void updateProjectNode(String projectId, String projectHomeId, ProjectNode projectNode) throws ManagementException;

	/**
	 * Delete a ProjectNode from a ProjectHome.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public boolean deleteProjectNode(String projectId, String projectHomeId, String projectNodeId) throws ManagementException;

	/**
	 * Get a list of Software installed on a ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @return
	 * @throws ManagementException
	 */
	public List<Software> getInstalledProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId) throws ManagementException;

	/**
	 * Install Software to ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public boolean installProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException;

	/**
	 * Uninstall Software from ProjectNode.
	 * 
	 * @param projectId
	 * @param projectHomeId
	 * @param projectNodeId
	 * @param softwareId
	 * @return
	 * @throws ManagementException
	 */
	public boolean uninstallProjectNodeSoftware(String projectId, String projectHomeId, String projectNodeId, String softwareId) throws ManagementException;

	// ------------------------------------------------------------------------------------------
	// Project Software
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all software in a Project.
	 * 
	 * @param projectId
	 * @return
	 */
	public List<Software> getProjectSoftware(String projectId) throws ManagementException;

	/**
	 * Get one piece of software.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 */
	public Software getProjectSoftware(String projectId, String softwareId) throws ManagementException;

	/**
	 * Get Software content.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @return
	 */
	public InputStream getProjectSoftwareContent(String projectId, String softwareId) throws ManagementException;

	/**
	 * Set Software content.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @param fileName
	 * @param length
	 * @param lastModified
	 * @param input
	 * @return
	 * @throws ManagementException
	 */
	public boolean setProjectSoftwareContent(String projectId, String softwareId, String fileName, long length, Date lastModified, InputStream input) throws ManagementException;

	/**
	 * Add a Software to a Project.
	 * 
	 * @param projectId
	 * @param newSoftwareRequest
	 * @return
	 * @throws ManagementException
	 */
	public Software addProjectSoftware(String projectId, Software newSoftwareRequest) throws ManagementException;

	/**
	 * Update Software information.
	 * 
	 * @param projectId
	 * @param software
	 * @throws ManagementException
	 */
	public void updateProjectSoftware(String projectId, Software software) throws ManagementException;

	/**
	 * Delete a piece of software from a Project.
	 * 
	 * @param projectId
	 * @param softwareId
	 * @throws ManagementException
	 */
	public boolean deleteProjectSoftware(String projectId, String softwareId) throws ManagementException;

}
