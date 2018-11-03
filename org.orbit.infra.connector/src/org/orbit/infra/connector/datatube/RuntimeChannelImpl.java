package org.orbit.infra.connector.datatube;

import org.orbit.infra.api.datatube.DataTubeClient;
import org.orbit.infra.api.datatube.RuntimeChannel;

public class RuntimeChannelImpl implements RuntimeChannel {

	protected DataTubeClient dataTubeClient;

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param dataTubeClient
	 */
	public RuntimeChannelImpl(DataTubeClient dataTubeClient) {
		this.dataTubeClient = dataTubeClient;
	}

	/**
	 * 
	 * @param dataCastId
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param dateCreated
	 * @param dateModified
	 */
	public RuntimeChannelImpl(String dataCastId, String dataTubeId, String channelId, String name, long dateCreated, long dateModified) {
		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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

}
