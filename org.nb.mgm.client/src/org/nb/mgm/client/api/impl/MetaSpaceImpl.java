package org.nb.mgm.client.api.impl;

import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.api.MetaSector;
import org.nb.mgm.client.api.MetaSpace;
import org.nb.mgm.client.util.ClientException;
import org.nb.mgm.client.ws.MetaSpaceClient;

import osgi.mgm.ws.dto.HomeDTO;
import osgi.mgm.ws.dto.MetaSpaceDTO;

public class MetaSpaceImpl implements MetaSpace {

	private boolean autoUpdate = false;
	private MetaSector metaSector;
	private MetaSpaceDTO metaSpaceDTO;

	/**
	 * 
	 * @param metaSpaceDTO
	 */
	public MetaSpaceImpl(MetaSpaceDTO metaSpaceDTO) {
		this.metaSpaceDTO = metaSpaceDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public Management getManagement() {
		Management management = null;
		if (this.metaSector != null) {
			management = this.metaSector.getManagement();
		}
		return management;
	}

	@Override
	public MetaSector getMetaSector() {
		return metaSector;
	}

	public void setMetaSector(MetaSector metaSector) {
		this.metaSector = metaSector;
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
		if (this.metaSector != null) {
			Management management = this.metaSector.getManagement();
			String metaSectorId = this.metaSector.getId();

			MetaSpaceClient metaSpaceClient = management.getAdapter(MetaSpaceClient.class);
			checkClient(metaSpaceClient);

			metaSpaceClient.updateMetaSpace(metaSectorId, this.metaSpaceDTO);
		}
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.metaSpaceDTO.getId();
	}

	@Override
	public String getName() {
		return this.metaSpaceDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.metaSpaceDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.metaSpaceDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.metaSpaceDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.metaSpaceDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.metaSpaceDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(MetaSpaceClient metaSpaceClient) throws ClientException {
		if (metaSpaceClient == null) {
			throw new ClientException(401, "MetaSpaceClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (HomeDTO.class.equals(adapter)) {
			return (T) this.metaSpaceDTO;
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("MetaSpace(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
