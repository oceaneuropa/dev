package org.nb.mgm.client.impl;

import java.util.ArrayList;
import java.util.List;

import org.nb.mgm.client.Management;
import org.nb.mgm.client.MetaSector;
import org.nb.mgm.client.MetaSpace;
import org.nb.mgm.client.MgmFactory;

import osgi.mgm.common.util.ClientException;
import osgi.mgm.ws.client.MetaSectorClient;
import osgi.mgm.ws.client.MetaSpaceClient;
import osgi.mgm.ws.dto.MetaSectorDTO;
import osgi.mgm.ws.dto.MetaSpaceDTO;
import osgi.mgm.ws.dto.StatusDTO;

public class MetaSectorImpl implements MetaSector {

	private boolean autoUpdate = false;
	private Management management;
	private MetaSectorDTO metaSectorDTO;

	public MetaSectorImpl(MetaSectorDTO metaSectorDTO) {
		this.metaSectorDTO = metaSectorDTO;
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
	public List<MetaSpace> getMetaSpaces() throws ClientException {
		MetaSpaceClient metaSpaceClient = this.management.getAdapter(MetaSpaceClient.class);
		if (metaSpaceClient == null) {
			return null;
		}

		List<MetaSpace> metaSpaces = new ArrayList<MetaSpace>();
		List<MetaSpaceDTO> metaSpaceDTOs = metaSpaceClient.getMetaSpaces(this.getId());
		for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
			MetaSpace metaSpace = MgmFactory.createMetaSpace(this, metaSpaceDTO);
			metaSpaces.add(metaSpace);
		}
		return metaSpaces;
	}

	/**
	 * Get all MetaSpaces in a MetaSector by filter.
	 * 
	 * @param filter
	 * @return
	 * @throws ClientException
	 */
	@Override
	public List<MetaSpace> getMetaSpaces(String filter) throws ClientException {
		MetaSpaceClient metaSpaceClient = this.management.getAdapter(MetaSpaceClient.class);
		checkClient(metaSpaceClient);

		List<MetaSpace> metaSpaces = new ArrayList<MetaSpace>();
		List<MetaSpaceDTO> metaSpaceDTOs = metaSpaceClient.getMetaSpaces(this.getId(), filter);
		for (MetaSpaceDTO metaSpaceDTO : metaSpaceDTOs) {
			MetaSpace metaSpace = MgmFactory.createMetaSpace(this, metaSpaceDTO);
			metaSpaces.add(metaSpace);
		}
		return metaSpaces;
	}

	/**
	 * Get MetaSpace by metaSpace Id.
	 * 
	 * @param metaSpaceId
	 * @return
	 * @throws ClientException
	 */
	@Override
	public MetaSpace getMetaSpace(String metaSpaceId) throws ClientException {
		MetaSpaceClient metaSpaceClient = this.management.getAdapter(MetaSpaceClient.class);
		checkClient(metaSpaceClient);

		MetaSpace metaSpace = null;
		MetaSpaceDTO metaSpaceDTO = metaSpaceClient.getMetaSpace(getId(), metaSpaceId);
		if (metaSpaceDTO != null) {
			metaSpace = MgmFactory.createMetaSpace(this, metaSpaceDTO);
		}
		return metaSpace;
	}

	/**
	 * Add a MetaSpace to a MetaSector.
	 * 
	 * @param name
	 * @param description
	 * @throws ClientException
	 */
	@Override
	public MetaSpace addMetaSpace(String name, String description) throws ClientException {
		MetaSpaceClient metaSpaceClient = this.management.getAdapter(MetaSpaceClient.class);
		checkClient(metaSpaceClient);

		MetaSpace metaSpace = null;

		MetaSpaceDTO newMetaSpaceRequest = new MetaSpaceDTO();
		newMetaSpaceRequest.setName(name);
		newMetaSpaceRequest.setDescription(description);

		MetaSpaceDTO newMetaSpaceDTO = metaSpaceClient.addMetaSpace(getId(), newMetaSpaceRequest);
		if (newMetaSpaceDTO != null) {
			metaSpace = MgmFactory.createMetaSpace(this, newMetaSpaceDTO);
		}
		return metaSpace;
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
		MetaSpaceClient metaSpaceClient = this.management.getAdapter(MetaSpaceClient.class);
		checkClient(metaSpaceClient);

		StatusDTO status = metaSpaceClient.deleteMetaSpace(getId(), metaSpaceId);
		if (status != null && status.isSuccess()) {
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------------------------
	// Check WS Client
	// ------------------------------------------------------------------------------------------
	protected void checkClient(MetaSectorClient metaSectorClient) throws ClientException {
		if (metaSectorClient == null) {
			throw new ClientException(401, "MetaSectorClient is not found.", null);
		}
	}

	protected void checkClient(MetaSpaceClient metaSpaceClient) throws ClientException {
		if (metaSpaceClient == null) {
			throw new ClientException(401, "MetaSpaceClient is not found.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (MetaSectorDTO.class.equals(adapter)) {
			return (T) this.metaSectorDTO;
		}
		return null;
	}

}
