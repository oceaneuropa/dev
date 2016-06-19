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
	 * Get all Machines.
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
	 * Get Machine information by Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws MgmException
	 */
	public Machine getMachine(String machineId) throws MgmException;

	/**
	 * Add a Machine to the cluster.
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
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws MgmException
	 */
	public void deleteMachine(String machineId) throws MgmException;

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

}
