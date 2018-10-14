package org.orbit.infra.runtime.datacast.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.datacast.service.ChannelMetadata;

public class ChannelMetadataImpl implements ChannelMetadata {

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;
	protected List<String> accountIds;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public ChannelMetadataImpl() {
	}

	/**
	 * 
	 * @param dataCastId
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param accountIds
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ChannelMetadataImpl(String dataCastId, String dataTubeId, String channelId, String name, List<String> accountIds, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;
		this.accountIds = accountIds;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	public String getDataCastId() {
		return this.dataCastId;
	}

	public void setDataCastId(String dataCastId) {
		this.dataCastId = dataCastId;
	}

	public String getDataTubeId() {
		return this.dataTubeId;
	}

	public void setDataTubeId(String dataTubeId) {
		this.dataTubeId = dataTubeId;
	}

	public String getChannelId() {
		return this.channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public synchronized List<String> getAccountIds() {
		if (this.accountIds == null) {
			this.accountIds = new ArrayList<String>();
		}
		return this.accountIds;
	}

	public synchronized void setAccountIds(List<String> accountIds) {
		this.accountIds = accountIds;
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

	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
