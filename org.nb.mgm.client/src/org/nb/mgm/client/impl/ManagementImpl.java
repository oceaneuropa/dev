package org.nb.mgm.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.nb.mgm.client.Machine;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MgmFactory;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.client.HomeClient;
import osgi.mgm.ws.client.MachineClient;
import osgi.mgm.ws.dto.MachineDTO;

public class ManagementImpl implements Management {

	private ClientConfiguration clientConfiguration;
	private MachineClient machineClient;
	private HomeClient homeClient;

	/**
	 * 
	 * @param url
	 * @param username
	 * @param password
	 */
	public ManagementImpl(String url, String username, String password) {
		this(url, "mgm", username, password);
	}

	/**
	 * 
	 * @param url
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public ManagementImpl(String url, String contextRoot, String username, String password) {
		this.clientConfiguration = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfiguration.setPassword(password);

		// web service client for machines
		this.machineClient = new MachineClient(this.clientConfiguration);
		// web service client for homes
		this.homeClient = new HomeClient(this.clientConfiguration);
	}

	// ------------------------------------------------------------------------------------------
	// Machine
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Machines.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Machine> getMachines() throws ClientException {
		List<Machine> machines = new ArrayList<Machine>();
		List<MachineDTO> machineDTOs = this.machineClient.getMachines();
		for (MachineDTO machineDTO : machineDTOs) {
			Machine machine = MgmFactory.createMachine(this, machineDTO);
			machines.add(machine);
		}
		return machines;
	}

	/**
	 * Get Machines by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Machine> getMachines(String filter) throws ClientException {
		List<Machine> machines = new ArrayList<Machine>();
		List<MachineDTO> machineDTOs = this.machineClient.getMachines(filter);
		for (MachineDTO machineDTO : machineDTOs) {
			Machine machine = MgmFactory.createMachine(this, machineDTO);
			machines.add(machine);
		}
		return machines;
	}

	/**
	 * Get Machine by machine Id.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public Machine getMachine(String machineId) throws ClientException {
		Machine machine = null;
		MachineDTO machineDTO = this.machineClient.getMachine(machineId);
		if (machineDTO != null) {
			machine = MgmFactory.createMachine(this, machineDTO);
		}
		return machine;
	}

	/**
	 * Add a Machine to the cluster.
	 * 
	 * @param name
	 * @param description
	 * @param ipAddress
	 * @return
	 * @throws ClientException
	 */
	@Override
	public Machine addMachine(String name, String description, String ipAddress) throws ClientException {
		Machine machine = null;

		MachineDTO newMachineRequest = new MachineDTO();
		newMachineRequest.setName(name);
		newMachineRequest.setDescription(description);
		newMachineRequest.setIpAddress(ipAddress);

		MachineDTO newMachineDTO = this.machineClient.addMachine(newMachineRequest);
		if (newMachineDTO != null) {
			machine = MgmFactory.createMachine(this, newMachineDTO);
		}
		return machine;
	}

	/**
	 * Delete a Machine from the cluster.
	 * 
	 * @param machineId
	 * @throws ClientException
	 */
	@Override
	public void deleteMachine(String machineId) throws ClientException {
		this.machineClient.deleteMachine(machineId);
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MachineClient.class.equals(adapter)) {
			return (T) this.machineClient;

		} else if (HomeClient.class.equals(adapter)) {
			return (T) this.homeClient;
		}
		return null;
	}

}
