package org.nb.mgm.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.nb.mgm.client.Home;
import org.nb.mgm.client.Machine;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MetaSector;
import org.nb.mgm.client.MetaSpace;
import org.nb.mgm.client.MgmFactory;

import osgi.mgm.common.util.ClientConfiguration;
import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.client.HomeClient;
import osgi.mgm.ws.client.MachineClient;
import osgi.mgm.ws.client.MetaSectorClient;
import osgi.mgm.ws.client.MetaSpaceClient;
import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MachineDTO;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.MetaSpaceDTO;
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
	 * @param contextRoot
	 * @param username
	 * @param password
	 */
	public ManagementImpl(String url, String contextRoot, String username, String password) {
		this.clientConfiguration = ClientConfiguration.get(url, contextRoot, username);
		this.clientConfiguration.setPassword(password);

		// Web service client for machine
		this.machineClient = new MachineClient(this.clientConfiguration);
		// Web service client for home
		this.homeClient = new HomeClient(this.clientConfiguration);
		// Web service client for metaSector
		this.metaSectorClient = new MetaSectorClient(this.clientConfiguration);
		// Web service client for metaSpace
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
		return getMachines(null);
	}

	/**
	 * Get Machines by query properties.
	 * 
	 * @param properties
	 *            supported keys are: "name", "ipaddress", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Machine> getMachines(Properties properties) throws ClientException {
		checkClient(this.machineClient);

		List<Machine> machines = new ArrayList<Machine>();

		List<MachineDTO> machineDTOs = this.machineClient.getMachines(properties);
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
		checkClient(this.machineClient);

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
	 * @param ipAddress
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	@Override
	public Machine addMachine(String name, String ipAddress, String description) throws ClientException {
		checkClient(this.machineClient);

		Machine machine = null;

		MachineDTO newMachineRequest = new MachineDTO();
		newMachineRequest.setName(name);
		newMachineRequest.setIpAddress(ipAddress);
		newMachineRequest.setDescription(description);

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
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @param machineId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Home> getHomes(String machineId) throws ClientException {
		return getHomes(machineId, null);
	}

	/**
	 * Get Homes in a Machine by query parameters.
	 * 
	 * @param machineId
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Home> getHomes(String machineId, Properties properties) throws ClientException {
		checkClient(this.homeClient);

		List<Home> homes = new ArrayList<Home>();

		Machine machine = getMachine(machineId);
		if (machine != null) {
			List<HomeDTO> homeDTOs = this.homeClient.getHomes(machineId, properties);
			for (HomeDTO homeDTO : homeDTOs) {
				Home home = MgmFactory.createHome(machine, homeDTO);
				homes.add(home);
			}
		}
		return homes;
	}

	/**
	 * Get Home by machine Id and home Id.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public Home getHome(String machineId, String homeId) throws ClientException {
		checkClient(this.homeClient);

		Home home = null;

		Machine machine = getMachine(machineId);
		if (machine != null) {
			HomeDTO homeDTO = this.homeClient.getHome(machineId, homeId);
			if (homeDTO != null) {
				home = MgmFactory.createHome(machine, homeDTO);
			}
		}
		return home;
	}

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
	@Override
	public Home addHome(String machineId, String name, String url, String description) throws ClientException {
		checkClient(this.homeClient);

		Home home = null;

		Machine machine = getMachine(machineId);
		if (machine != null) {
			HomeDTO newHomeRequest = new HomeDTO();
			newHomeRequest.setName(name);
			newHomeRequest.setUrl(url);
			newHomeRequest.setDescription(description);

			HomeDTO newHomeDTO = this.homeClient.addHome(machineId, newHomeRequest);
			if (newHomeDTO != null) {
				home = MgmFactory.createHome(machine, newHomeDTO);
			}
		}
		return home;
	}

	/**
	 * Delete a Home from a Machine by home Id.
	 * 
	 * @param machineId
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public boolean deleteHome(String machineId, String homeId) throws ClientException {
		checkClient(this.homeClient);

		StatusDTO status = this.homeClient.deleteHome(machineId, homeId);
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
		return getMetaSectors(null);
	}

	/**
	 * Get MetaSectors by query properties.
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<MetaSector> getMetaSectors(Properties properties) throws ClientException {
		checkClient(this.metaSectorClient);

		List<MetaSector> metaSectors = new ArrayList<MetaSector>();

		List<MetaSectorDTO> machineDTOs = this.metaSectorClient.getMetaSectors(properties);
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
		checkClient(this.metaSectorClient);

		MetaSector metaSector = null;

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
		checkClient(this.metaSectorClient);

		MetaSector metaSector = null;

		MetaSectorDTO newMetaSectorRequest = new MetaSectorDTO();
		newMetaSectorRequest.setName(name);
		newMetaSectorRequest.setDescription(description);

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
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @param metaSectorId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId) throws ClientException {
		return getMetaSpaces(metaSectorId, null);
	}

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
	@Override
	public List<MetaSpace> getMetaSpaces(String metaSectorId, Properties properties) throws ClientException {
		checkClient(this.metaSpaceClient);

		List<MetaSpace> metaSpaces = new ArrayList<MetaSpace>();

		MetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			List<MetaSpaceDTO> metaSpaceDTOs = this.metaSpaceClient.getMetaSpaces(metaSectorId, properties);
			for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
				MetaSpace metaSpace = MgmFactory.createMetaSpace(metaSector, metaSpaceDTO);
				metaSpaces.add(metaSpace);
			}
		}
		return metaSpaces;
	}

	/**
	 * Get MetaSpace by metaSector Id and metaSpace Id.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public MetaSpace getMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);

		MetaSpace metaSpace = null;

		MetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			MetaSpaceDTO metaSpaceDTO = this.metaSpaceClient.getMetaSpace(metaSectorId, metaSpaceId);
			if (metaSpaceDTO != null) {
				metaSpace = MgmFactory.createMetaSpace(metaSector, metaSpaceDTO);
			}
		}
		return metaSpace;
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param name
	 * @param description
	 * @return
	 * @throws ClientException
	 */
	@Override
	public MetaSpace addMetaSpace(String metaSectorId, String name, String description) throws ClientException {
		checkClient(this.metaSpaceClient);

		MetaSpace metaSpace = null;

		MetaSector metaSector = getMetaSector(metaSectorId);
		if (metaSector != null) {
			MetaSpaceDTO newMetaSpaceRequest = new MetaSpaceDTO();
			newMetaSpaceRequest.setName(name);
			newMetaSpaceRequest.setDescription(description);

			MetaSpaceDTO newMetaSpaceDTO = this.metaSpaceClient.addMetaSpace(metaSectorId, newMetaSpaceRequest);
			if (newMetaSpaceDTO != null) {
				metaSpace = MgmFactory.createMetaSpace(metaSector, newMetaSpaceDTO);
			}
		}
		return metaSpace;
	}

	/**
	 * Delete a MetaSpace from a MetaSector.
	 * 
	 * @param metaSectorId
	 * @param metaSpaceId
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public boolean deleteMetaSpace(String metaSectorId, String metaSpaceId) throws ClientException {
		checkClient(this.metaSpaceClient);

		StatusDTO status = this.metaSpaceClient.deleteMetaSpace(metaSectorId, metaSpaceId);
		if (status != null && status.isSuccess()) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	/**
	 * 
	 * @param machineClient
	 * @throws ClientException
	 */
	protected void checkClient(MachineClient machineClient) throws ClientException {
		if (machineClient == null) {
			throw new ClientException(401, "MachineClient is null.", null);
		}
	}

	/**
	 * 
	 * @param homeClient
	 * @throws ClientException
	 */
	protected void checkClient(HomeClient homeClient) throws ClientException {
		if (homeClient == null) {
			throw new ClientException(401, "HomeClient is null.", null);
		}
	}

	/**
	 * 
	 * @param metaSectorClient
	 * @throws ClientException
	 */
	protected void checkClient(MetaSectorClient metaSectorClient) throws ClientException {
		if (metaSectorClient == null) {
			throw new ClientException(401, "MetaSectorClient is null.", null);
		}
	}

	/**
	 * 
	 * @param metaSpaceClient
	 * @throws ClientException
	 */
	protected void checkClient(MetaSpaceClient metaSpaceClient) throws ClientException {
		if (metaSpaceClient == null) {
			throw new ClientException(401, "MetaSpaceClient is null.", null);
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
