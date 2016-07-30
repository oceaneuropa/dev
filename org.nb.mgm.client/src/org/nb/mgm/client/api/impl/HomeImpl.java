package org.nb.mgm.client.api.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nb.mgm.client.api.IHome;
import org.nb.mgm.client.api.IMachine;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.HomeDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class HomeImpl implements IHome {

	private ManagementClient management;
	private IMachine machine;
	private HomeDTO homeDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param machine
	 * @param homeDTO
	 */
	public HomeImpl(ManagementClient management, IMachine machine, HomeDTO homeDTO) {
		this.management = management;
		this.machine = machine;
		this.homeDTO = homeDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public ManagementClient getManagement() {
		return this.management;
	}

	@Override
	public IMachine getMachine() {
		return machine;
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
		checkManagement(this.management);
		checkMachine(this.machine);

		String machineId = this.machine.getId();
		return this.management.updateHome(machineId, this.homeDTO);
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.homeDTO.getId();
	}

	@Override
	public String getName() {
		return this.homeDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.homeDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.homeDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.homeDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.homeDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.homeDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ---------------------------- --------------------------------------------------------------
	// Properties
	// ------------------------------------------------------------------------------------------
	@Override
	public Map<String, Object> getProperties() throws ClientException {
		checkManagement(this.management);
		checkMachine(this.machine);

		String machineId = this.machine.getId();
		String homeId = this.homeDTO.getId();
		return this.management.getHomeProperties(machineId, homeId, true);
	}

	@Override
	public boolean setProperty(String propName, Object propValue) throws ClientException {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(propName, propValue);
		return setProperties(properties);
	}

	@Override
	public boolean setProperties(Map<String, Object> properties) throws ClientException {
		checkManagement(this.management);
		checkMachine(this.machine);

		String machineId = this.machine.getId();
		String homeId = this.homeDTO.getId();
		return this.management.setHomeProperties(machineId, homeId, properties);
	}

	@Override
	public boolean removeProperty(String propertyName) throws ClientException {
		List<String> propertyNames = new ArrayList<String>();
		propertyNames.add(propertyName);
		return removeProperties(propertyNames);
	}

	@Override
	public boolean removeProperties(List<String> propertyNames) throws ClientException {
		checkManagement(this.management);
		checkMachine(this.machine);

		String machineId = this.machine.getId();
		String homeId = this.homeDTO.getId();
		return this.management.removeHomeProperties(machineId, homeId, propertyNames);
	}

	// ------------------------------------------------------------------------------------------
	// Check Management Client
	// ------------------------------------------------------------------------------------------
	/**
	 * @param management
	 * @throws ClientException
	 */
	protected void checkManagement(ManagementClient management) throws ClientException {
		if (management == null) {
			throw new ClientException(401, "management is null.", null);
		}
	}

	/**
	 * @param machine
	 * @throws ClientException
	 */
	protected void checkMachine(IMachine machine) throws ClientException {
		if (machine == null) {
			throw new ClientException(401, "machine is null.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (HomeDTO.class.equals(adapter)) {
			return (T) this.homeDTO;
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
		sb.append("Home(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
