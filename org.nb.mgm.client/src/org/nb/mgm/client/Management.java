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
	public void deleteMachine(String machineId) throws ClientException;

}
