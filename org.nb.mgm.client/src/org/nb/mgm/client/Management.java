package org.nb.mgm.client;

import java.util.List;

import osgi.mgm.common.util.ClientException;
import osgi.mgm.common.util.IAdaptable;

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
	 * Get Machines by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	public List<Machine> getMachines(String filter) throws ClientException;

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
	 * @param description
	 * @param ipAddress
	 * @return
	 * @throws ClientException
	 */
	public Machine addMachine(String name, String description, String ipAddress) throws ClientException;

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	public boolean deleteMachine(String machineId) throws ClientException;

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
	 * Get MetaSectors by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	public List<MetaSector> getMetaSectors(String filter) throws ClientException;

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

}
