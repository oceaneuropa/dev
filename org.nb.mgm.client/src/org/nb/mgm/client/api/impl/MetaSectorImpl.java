package org.nb.mgm.client.api.impl;

import java.util.List;
import java.util.Properties;

import org.nb.mgm.client.api.IMetaSector;
import org.nb.mgm.client.api.IMetaSpace;
import org.nb.mgm.client.api.Management;
import org.nb.mgm.client.ws.MetaSectorClient;
import org.nb.mgm.model.dto.MetaSectorDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class MetaSectorImpl implements IMetaSector {

	private Management management;
	private MetaSectorDTO metaSectorDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param metaSectorDTO
	 */
	public MetaSectorImpl(Management management, MetaSectorDTO metaSectorDTO) {
		this.management = management;
		this.metaSectorDTO = metaSectorDTO;
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
	public void update() throws ClientException {
		if (this.management != null) {
			MetaSectorClient metaSectorClient = this.management.getAdapter(MetaSectorClient.class);
			checkClient(metaSectorClient);

			metaSectorClient.updateMetaSector(this.metaSectorDTO);
		}
	}

	// ------------------------------------------------------------------------------------------
	// Attribute
	// ------------------------------------------------------------------------------------------
	@Override
	public String getId() {
		return this.metaSectorDTO.getId();
	}

	@Override
	public String getName() {
		return this.metaSectorDTO.getName();
	}

	@Override
	public void setName(String name) throws ClientException {
		String oldName = this.metaSectorDTO.getName();

		if ((oldName == null && name != null) || (oldName != null && !oldName.equals(name))) {
			this.metaSectorDTO.setName(name);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	@Override
	public String getDescription() {
		return this.metaSectorDTO.getDescription();
	}

	@Override
	public void setDescription(String description) throws ClientException {
		String oldDescription = this.metaSectorDTO.getDescription();

		if ((oldDescription == null && description != null) || (oldDescription != null && !oldDescription.equals(description))) {
			this.metaSectorDTO.setDescription(description);

			if (this.autoUpdate) {
				update();
			}
		}
	}

	// ------------------------------------------------------------------------------------------
	// MetaSpace
	// ------------------------------------------------------------------------------------------
	/**
	 * Get all MetaSpaces in a MetaSector.
	 * 
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<IMetaSpace> getMetaSpaces() throws ClientException {
		return getMetaSpaces(null);
	}

	/**
	 * Get MetaSpaces in a MetaSector by query parameters.
	 * 
	 * @param properties
	 *            supported keys are: "name", "filter".
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<IMetaSpace> getMetaSpaces(Properties properties) throws ClientException {
		return this.management.getMetaSpaces(getId(), properties);
	}

	/**
	 * Get MetaSpace by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public IMetaSpace getMetaSpace(String metaSpaceId) throws ClientException {
		return this.management.getMetaSpace(getId(), metaSpaceId);
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	@Override
	public IMetaSpace addMetaSpace(String name, String description) throws ClientException {
		return this.management.addMetaSpace(getId(), name, description);
	}

	/**
	 * Delete MetaSpace from a MetaSector by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public boolean deleteMetaSpace(String metaSpaceId) throws ClientException {
		return this.management.deleteMetaSpace(getId(), metaSpaceId);
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(MetaSectorClient metaSectorClient) throws ClientException {
		if (metaSectorClient == null) {
			throw new ClientException(401, "MetaSectorClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MetaSectorDTO.class.equals(adapter)) {
			return (T) this.metaSectorDTO;
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
		sb.append("MetaSector(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
