package org.orbit.infra.runtime.datacast.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.orbit.infra.runtime.datacast.service.DataTubeConfig;
import org.orbit.infra.runtime.util.ModelConverter;

public class DataTubeConfigImpl implements DataTubeConfig {

	protected String id;
	protected String dataCastId;
	protected String dataTubeId;
	protected String name;
	protected boolean enabled;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public DataTubeConfigImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param dataCastId
	 * @param dataTubeId
	 * @param name
	 * @param enabled
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public DataTubeConfigImpl(String id, String dataCastId, String dataTubeId, String name, boolean enabled, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.id = id;
		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.name = name;
		this.enabled = enabled;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public String getId() {
		return this.id;
	}

	@Override
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String getDataCastId() {
		return this.dataCastId;
	}

	@Override
	public void setDataCastId(String dataCastId) {
		this.dataCastId = dataCastId;
	}

	@Override
	public String getDataTubeId() {
		return this.dataTubeId;
	}

	@Override
	public void setDataTubeId(String dataTubeId) {
		this.dataTubeId = dataTubeId;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new HashMap<String, Object>();
		}
		return this.properties;
	}

	public synchronized void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	@Override
	public long getDateCreated() {
		return this.dateCreated;
	}

	@Override
	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@Override
	public long getDateModified() {
		return this.dateModified;
	}

	@Override
	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String propertiesString = ModelConverter.DataCast.toPropertiesString(this.properties);

		sb.append("DataTubeConfigImpl (");
		sb.append("id='").append(this.id).append("'");
		sb.append(", dataCastId='").append(this.dataCastId).append("'");
		sb.append(", dataTubeId='").append(this.dataTubeId).append("'");
		sb.append(", name='").append(this.name).append("'");
		sb.append(", enabled=").append(this.enabled);
		sb.append(", properties=").append(propertiesString);
		sb.append(", dateCreated=").append(this.dateCreated);
		sb.append(", dateModified=").append(this.dateModified);
		sb.append(")");

		return sb.toString();
	}

}
