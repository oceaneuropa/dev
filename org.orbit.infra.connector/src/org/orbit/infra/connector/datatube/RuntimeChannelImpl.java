package org.orbit.infra.connector.datatube;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.RuntimeChannel;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.model.DateRecordSupport;
import org.origin.common.model.TransientPropertySupport;

public class RuntimeChannelImpl implements RuntimeChannel {

	protected DataTubeClient dataTubeClient;

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;

	protected DateRecordSupport<Long> dateRecordSupport = new DateRecordSupport<Long>();
	protected TransientPropertySupport transientPropertySupport = new TransientPropertySupport();
	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param dataTubeClient
	 */
	public RuntimeChannelImpl(DataTubeClient dataTubeClient) {
		this.dataTubeClient = dataTubeClient;
	}

	/**
	 * 
	 * @param dataTubeClient
	 * @param dataCastId
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param dateCreated
	 * @param dateModified
	 */
	public RuntimeChannelImpl(DataTubeClient dataTubeClient, String dataCastId, String dataTubeId, String channelId, String name, long dateCreated, long dateModified) {
		this.dataTubeClient = dataTubeClient;

		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;

		setDateCreated(dateCreated);
		setDateModified(dateModified);
	}

	@Override
	public DataTubeClient getDataTubeClient() {
		return this.dataTubeClient;
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
	public String getChannelId() {
		return this.channelId;
	}

	@Override
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	/** DateRecordAware */
	@Override
	public Long getDateCreated() {
		return this.dateRecordSupport.getDateCreated();
	}

	@Override
	public void setDateCreated(Long dateCreated) {
		this.dateRecordSupport.setDateCreated(dateCreated);
	}

	@Override
	public Long getDateModified() {
		return this.dateRecordSupport.getDateModified();
	}

	@Override
	public void setDateModified(Long dateModified) {
		this.dateRecordSupport.setDateModified(dateModified);
	}

	/** TransientPropertyAware */
	@Override
	public <T> T getTransientProperty(String key) {
		return this.transientPropertySupport.getTransientProperty(key);
	}

	@Override
	public <T> T setTransientProperty(String key, T value) {
		return this.transientPropertySupport.setTransientProperty(key, value);
	}

	@Override
	public <T> T removeTransientProperty(String key) {
		return this.transientPropertySupport.removeTransientProperty(key);
	}

	/** IAdaptable */
	@Override
	public <T> void adapt(Class<T> clazz, T object) {
		this.adaptorSupport.adapt(clazz, object);
	}

	@Override
	public <T> void adapt(Class<T>[] classes, T object) {
		this.adaptorSupport.adapt(classes, object);
	}

	@Override
	public <T> T getAdapter(Class<T> adapter) {
		return this.adaptorSupport.getAdapter(adapter);
	}

}

// long getDateCreated();
// void setDateCreated(long dateCreated);
// long getDateModified();
// void setDateModified(long dateModified);
// protected long dateCreated;
// protected long dateModified;
// return this.dateCreated;
// this.dateCreated = dateCreated;
// return this.dateModified;
// this.dateModified = dateModified;
