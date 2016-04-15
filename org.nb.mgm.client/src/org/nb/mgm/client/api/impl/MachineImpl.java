package org.nb.mgm.client.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MgmFactory;
import org.nb.mgm.client.util.ClientException;
import org.nb.mgm.client.ws.HomeClient;
import org.nb.mgm.client.ws.MachineClient;
import org.nb.mgm.ws.dto.HomeDTO;
import org.nb.mgm.ws.dto.MachineDTO;
import org.nb.mgm.ws.dto.StatusDTO;

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
			checkClient(machineClient);

			machineClient.updateMachine(this.machineDTO);
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
		return getHomes(null);
	}

	/**
	 * Get Homes in a Machine by query parameters.
	 * 
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<Home> getHomes(Properties properties) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		checkClient(homeClient);

		List<Home> homes = new ArrayList<Home>();
		List<HomeDTO> homeDTOs = homeClient.getHomes(this.getId(), properties);
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
		checkClient(homeClient);

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
	 * @param url
	 * @param description
	 * @throws ClientException
	 */
	public Home addHome(String name, String url, String description) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		checkClient(homeClient);

		Home home = null;

		HomeDTO newHomeRequest = new HomeDTO();
		newHomeRequest.setName(name);
		newHomeRequest.setUrl(url);
		newHomeRequest.setDescription(description);

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
	public boolean deleteHome(String homeId) throws ClientException {
		HomeClient homeClient = this.management.getAdapter(HomeClient.class);
		checkClient(homeClient);

		StatusDTO status = homeClient.deleteHome(getId(), homeId);
		if (status != null && "success".equalsIgnoreCase(status.getStatus())) {
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

	protected void checkClient(HomeClient homeClient) throws ClientException {
		if (homeClient == null) {
			throw new ClientException(401, "HomeClient is not found.", null);
		}
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Machine(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", ip=\"").append(getIpAddress()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
