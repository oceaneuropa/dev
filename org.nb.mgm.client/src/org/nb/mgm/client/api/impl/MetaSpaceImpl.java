package org.nb.mgm.client.api.impl;

import org.nb.mgm.client.api.IMetaSector;
import org.nb.mgm.client.api.IMetaSpace;
import org.nb.mgm.client.api.ManagementClient;
import org.nb.mgm.model.dto.HomeDTO;
import org.nb.mgm.model.dto.MetaSpaceDTO;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.rest.client.ClientException;

public class MetaSpaceImpl implements IMetaSpace {

	private ManagementClient management;
	private IMetaSector metaSector;
	private MetaSpaceDTO metaSpaceDTO;

	private AdaptorSupport adaptorSupport = new AdaptorSupport();
	private boolean autoUpdate = false;

	/**
	 * 
	 * @param management
	 * @param metaSector
	 * @param metaSpaceDTO
	 */
	public MetaSpaceImpl(ManagementClient management, IMetaSector metaSector, MetaSpaceDTO metaSpaceDTO) {
		this.management = management;
		this.metaSector = metaSector;
		this.metaSpaceDTO = metaSpaceDTO;
	}

	// ------------------------------------------------------------------------------------------
	// Parent
	// ------------------------------------------------------------------------------------------
	@Override
	public ManagementClient getManagement() {
		return this.management;
	}

	@Override
	public IMetaSector getMetaSector() {
		return metaSector;
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
		checkMetaSector(this.metaSector);

		String metaSectorId = this.metaSector.getId();
		return this.management.updateMetaSpace(metaSectorId, this.metaSpaceDTO);
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
	 * @param metaSector
	 * @throws ClientException
	 */
	protected void checkMetaSector(IMetaSector metaSector) throws ClientException {
		if (metaSector == null) {
			throw new ClientException(401, "metaSector is null.", null);
		}
	}

	/** implement IAdaptable interface */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if (HomeDTO.class.equals(adapter)) {
			return (T) this.metaSpaceDTO;
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
		sb.append("MetaSpace(");
		sb.append("id=\"").append(getId()).append("\"");
		sb.append(", name=\"").append(getName()).append("\"");
		sb.append(", description=\"").append(getDescription()).append("\"");
		sb.append(")");
		return sb.toString();
	}

}
