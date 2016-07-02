package org.nb.mgm.client.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.ws.MachineClient;
import org.nb.mgm.model.dto.MachineDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.model.StatusDTO;

public class MachineImpl implements IMachine {

	private Management management;
	private MachineDTO machineDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param machineDTO
	 */
	public MachineImpl(Management management, MachineDTO machineDTO) {
		this.management = management;
		this.machineDTO = machineDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		return management;
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
	public boolean update() throws ClientException {
		MachineClient machineClient = this.management.getAdapter(MachineClient.class);
		checkClient(machineClient);

		StatusDTO status = machineClient.updateMachine(this.machineDTO);
		return (status != null && status.success()) ? true : false;
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
	 * Get all Homes of the Machine.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<IHome> getHomes() throws ClientException {
		return this.management.getHomes(getId());
	}

	/**
	 * Get Homes by query parameters of the Machine.
	 * 
	 * @param properties
	 *            supported keys are: "name", "url", "status", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<IHome> getHomes(Properties properties) throws ClientException {
		return this.management.getHomes(getId(), properties);
	}

	/**
	 * Get Home by homeId.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public IHome getHome(String homeId) throws ClientException {
		return this.management.getHome(getId(), homeId);
	}

	/**
	 * Add a Home to the Machine.
	 * 
	 * @param name
	 * @param url
	 * @param description
	 * @throws ClientException
	 */
	public IHome addHome(String name, String url, String description) throws ClientException {
		return this.management.addHome(getId(), name, url, description);
	}

	/**
	 * Delete Home from the Machine by homeId.
	 * 
	 * @param homeId
	 * @return
	 * @throws ClientException
	 */
	public boolean deleteHome(String homeId) throws ClientException {
		return this.management.deleteHome(getId(), homeId);
	}

	// ------------------------------------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------------------------
	@Override
	public Map<String, Object> getProperties() throws ClientException {
		String machineId = this.machineDTO.getId();

		MachineClient machineClient = this.management.getAdapter(MachineClient.class);
		checkClient(machineClient);

		Map<String, Object> properties = machineClient.getProperties(machineId, true);
		if (properties == null) {
			properties = new HashMap<String, Object>();
		}
		return properties;
	}

	@Override
	public boolean setProperty(String propName, Object propValue) throws ClientException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(propName, propValue);
		return setProperties(properties);
	}

	@Override
	public boolean setProperties(Map<String, Object> properties) throws ClientException {
		String machineId = this.machineDTO.getId();

		MachineClient machineClient = this.management.getAdapter(MachineClient.class);
		checkClient(machineClient);

		StatusDTO status = machineClient.setProperties(machineId, properties);
		return (status != null && status.success()) ? true : false;
	}

	@Override
	public boolean removeProperty(String propertyName) throws ClientException {
		List<String> propertyNames = new ArrayList<String>();
		propertyNames.add(propertyName);
		return removeProperties(propertyNames);
	}

	@Override
	public boolean removeProperties(List<String> propertyNames) throws ClientException {
		String machineId = this.machineDTO.getId();

		MachineClient machineClient = this.management.getAdapter(MachineClient.class);
		checkClient(machineClient);

		StatusDTO status = machineClient.removeProperties(machineId, propertyNames);
		return (status != null && status.success()) ? true : false;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(MachineClient machineClient) throws ClientException {
		if (machineClient == null) {
			throw new ClientException(401, "MachineClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MachineDTO.class.equals(adapter)) {
			return (T) this.machineDTO;
		}
		T result = this.adaptorSupport.getAdapter(adapter);
		if (result != null) {
			return result;
		}
		return null;
	}

	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		adaptorSupport.adapt(clazz, object);
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
