package org.nb.mgm.client.api.impl;

import org.nb.mgm.client.api.Home;
import org.nb.mgm.client.api.Machine;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.ws.HomeClient;
import org.nb.mgm.model.dto.HomeDTO;
import org.origin.common.rest.client.ClientException;
import org.origin.common.util.AdaptorSupport;

public class HomeImpl implements Home {

	private boolean autoUpdate = false;
	private Machine machine;
	private HomeDTO homeDTO;
	private AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param homeDTO
	 */
	public HomeImpl(HomeDTO homeDTO) {
		this.homeDTO = homeDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		Management management = null;
		if (this.machine != null) {
			management = this.machine.getManagement();
		}
		return management;
	}

	@Override
	public Machine getMachine() {
		return machine;
	}

	public void setMachine(Machine machine) {
		this.machine = machine;
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
		if (this.machine != null) {
			Management management = this.machine.getManagement();
			String machineId = this.machine.getId();

			HomeClient homeClient = management.getAdapter(HomeClient.class);
			checkClient(homeClient);

			homeClient.updateHome(machineId, this.homeDTO);
		}
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
	public String getUrl() {
		return this.homeDTO.getUrl();
	}

	@Override
	public void setUrl(String url) throws ClientException {
		String oldUrl = this.homeDTO.getUrl();

		if ((oldUrl == null && url != null) || (oldUrl != null && !oldUrl.equals(url))) {
			this.homeDTO.setUrl(url);

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

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(HomeClient homeClient) throws ClientException {
		if (homeClient == null) {
			throw new ClientException(401, "HomeClient is not found.", null);
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
		sb.append(", url=\"").append(getUrl()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
