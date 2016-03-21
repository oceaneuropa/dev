package org.nb.mgm.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.nb.mgm.client.Home;
import org.nb.mgm.client.Machine;
import org.nb.mgm.client.Management;
import org.nb.mgm.client.MgmFactory;

import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.client.HomeClient;
import osgi.mgm.ws.client.MachineClient;
import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MachineDTO;

public class MachineImpl implements Machine {

	private boolean autoUpdate = false;
	private Management management;
	private MachineDTO machineDTO;

	/**
	 * 
	 * @param machineDTO
	 */
	public MachineImpl(MachineDTO machineDTO) {
		this.machineDTO = machineDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		return management;
	}

	public void setManagement(Management management) {
		this.management = management;
	}

	// ------------------------------------------------------------------------------------------
	// Auto Update Attributes
	// ------------------------------------------------------------------------------------------
	@Override
	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	@Override
	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	@Override
	public void update() throws ClientException {
		if (this.management != null) {
			MachineClient machineClient = this.management.getAdapter(MachineClient.class);
			if (machineClient != null) {
				machineClient.updateMachine(this.machineDTO);
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.machineDTO.getId();
	}

	@Override
	public String getName() {
		return this.machineDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.machineDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.machineDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.machineDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.machineDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.machineDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getIpAddress() {
		return this.machineDTO.getIpAddress();
	}

	@Override
	public void setIpAddress(String ipAddress) throws ClientException {
		String oldIpAddress = this.machineDTO.getIpAddress();

		if ((oldIpAddress == null && ipAddress != null) || (oldIpAddress != null && !oldIpAddress.equals(ipAddress))) {
			this.machineDTO.setIpAddress(ipAddress);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// Home
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all Homes in a Machine.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Home> getHomes() throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		if (homeClient == null) {
			return null;
		}

		List<Home> homes = new ArrayList<Home>();
		List<HomeDTO> homeDTOs = homeClient.getHomes(this.getId());
		for (HomeDTO homeDTO : homeDTOs) {
			Home home = MgmFactory.createHome(this, homeDTO);
			homes.add(home);
		}
		return homes;
	}

	/**
	 * Get all Homes in a Machine by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Home> getHomes(String filter) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		if (homeClient == null) {
			return null;
		}

		List<Home> homes = new ArrayList<Home>();
		List<HomeDTO> homeDTOs = homeClient.getHomes(this.getId(), filter);
		for (HomeDTO homeDTO : homeDTOs) {
			Home home = MgmFactory.createHome(this, homeDTO);
			homes.add(home);
		}
		return homes;
	}

	/**
	 * Get Home by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public Home getHome(String homeId) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		if (homeClient == null) {
			return null;
		}

		Home home = null;
		HomeDTO homeDTO = homeClient.getHome(getId(), homeId);
		if (homeDTO != null) {
			home = MgmFactory.createHome(this, homeDTO);
		}
		return home;
	}

	/**
	 * Add a Home to a Machine.
	 * 
	 * @param name
	 * @param description
	 * @param url
	 * @throws ClientException
	 */
	public Home addHome(String name, String description, String url) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		if (homeClient == null) {
			return null;
		}

		Home home = null;

		HomeDTO newHomeRequest = new HomeDTO();
		newHomeRequest.setName(name);
		newHomeRequest.setDescription(description);
		newHomeRequest.setUrl(url);

		HomeDTO newHomeDTO = homeClient.addHome(getId(), newHomeRequest);
		if (newHomeDTO != null) {
			home = MgmFactory.createHome(this, newHomeDTO);
		}
		return home;
	}

	/**
	 * Delete Home from a Machine by home Id.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public void deleteHome(String homeId) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		if (homeClient == null) {
			return;
		}

		homeClient.deleteHome(getId(), homeId);
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MachineDTO.class.equals(adapter)) {
			return (T) this.machineDTO;
		}
		return null;
	}

}
