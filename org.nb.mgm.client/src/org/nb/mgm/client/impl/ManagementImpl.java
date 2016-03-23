package org.nb.mgm.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.nb.mgm.client.Machine;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MetaSector;
import org.nb.mgm.client.MgmFactory;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.client.HomeClient;
import osgi.mgm.ws.client.MachineClient;
import osgi.mgm.ws.client.MetaSectorClient;
import osgi.mgm.ws.client.MetaSpaceClient;
import osgi.mgm.ws.dto.MachineDTO;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.StatusDTO;

public class ManagementImpl implements Management {

	private ClientConfiguration clientConfiguration;
	private MachineClient machineClient;
	private HomeClient homeClient;
	private MetaSectorClient metaSectorClient;
	private MetaSpaceClient metaSpaceClient;

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

		// WS client for machine
		this.machineClient = new MachineClient(this.clientConfiguration);
		// WS client for home
		this.homeClient = new HomeClient(this.clientConfiguration);
		// WS client for metaSector
		this.metaSectorClient = new MetaSectorClient(this.clientConfiguration);
		// WS client for metaSpace
		this.metaSpaceClient = new MetaSpaceClient(this.clientConfiguration);
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

		checkClient(this.machineClient);
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

		checkClient(this.machineClient);
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

		checkClient(this.machineClient);
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

		checkClient(this.machineClient);
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
	public boolean deleteMachine(String machineId) throws ClientException {
		checkClient(this.machineClient);
		StatusDTO status = this.machineClient.deleteMachine(machineId);
		if (status != null && status.isSuccess()) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------
	// MetaSector
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSectors.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<MetaSector> getMetaSectors() throws ClientException {
		List<MetaSector> metaSectors = new ArrayList<MetaSector>();

		checkClient(this.metaSectorClient);
		List<MetaSectorDTO> machineDTOs = this.metaSectorClient.getMetaSectors();

		for (MetaSectorDTO machineDTO : machineDTOs) {
			MetaSector metaSector = MgmFactory.createMetaSector(this, machineDTO);
			metaSectors.add(metaSector);
		}
		return metaSectors;
	}

	/**
	 * Get MetaSectors by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<MetaSector> getMetaSectors(String filter) throws ClientException {
		List<MetaSector> metaSectors = new ArrayList<MetaSector>();

		checkClient(this.metaSectorClient);
		List<MetaSectorDTO> machineDTOs = this.metaSectorClient.getMetaSectors(filter);

		for (MetaSectorDTO machineDTO : machineDTOs) {
			MetaSector metaSector = MgmFactory.createMetaSector(this, machineDTO);
			metaSectors.add(metaSector);
		}
		return metaSectors;
	}

	/**
	 * Get MetaSector by metaSector Id.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public MetaSector getMetaSector(String metaSectorId) throws ClientException {
		MetaSector metaSector = null;

		checkClient(this.metaSectorClient);
		MetaSectorDTO metaSectorDTO = this.metaSectorClient.getMetaSector(metaSectorId);

		if (metaSectorDTO != null) {
			metaSector = MgmFactory.createMetaSector(this, metaSectorDTO);
		}
		return metaSector;
	}

	/**
	 * Add a MetaSector to the cluster.
	 * 
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	@Override
	public MetaSector addMetaSector(String name, String description) throws ClientException {
		MetaSector metaSector = null;

		MetaSectorDTO newMetaSectorRequest = new MetaSectorDTO();
		newMetaSectorRequest.setName(name);
		newMetaSectorRequest.setDescription(description);

		checkClient(this.metaSectorClient);
		MetaSectorDTO newMetaSectorDTO = this.metaSectorClient.addMetaSector(newMetaSectorRequest);

		if (newMetaSectorDTO != null) {
			metaSector = MgmFactory.createMetaSector(this, newMetaSectorDTO);
		}
		return metaSector;
	}

	/**
	 * Delete a MetaSector from the cluster.
	 * 
	 * @param metaSectorId
	 * @throws ClientException
	 */
	@Override
	public boolean deleteMetaSector(String metaSectorId) throws ClientException {
		checkClient(this.metaSectorClient);
		StatusDTO status = this.metaSectorClient.deleteMetaSector(metaSectorId);
		if (status != null && status.isSuccess()) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(MachineClient machineClient) throws ClientException {
		if (machineClient == null) {
			throw new ClientException(401, "MachineClient is not found.", null);
		}
	}

	protected void checkClient(MetaSectorClient metaSectorClient) throws ClientException {
		if (metaSectorClient == null) {
			throw new ClientException(401, "MetaSectorClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MachineClient.class.equals(adapter)) {
			return (T) this.machineClient;

		} else if (HomeClient.class.equals(adapter)) {
			return (T) this.homeClient;

		} else if (MetaSectorClient.class.equals(adapter)) {
			return (T) this.metaSectorClient;

		} else if (MetaSpaceClient.class.equals(adapter)) {
			return (T) this.metaSpaceClient;
		}
		return null;
	}

}
