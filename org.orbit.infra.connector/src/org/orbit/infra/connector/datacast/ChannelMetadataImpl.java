package org.orbit.infra.connector.datacast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.ChannelStatus;
import org.orbit.infra.api.datacast.DataCastClient;

public class ChannelMetadataImpl implements ChannelMetadata {

	protected DataCastClient dataCastClient;

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;
	protected String accessType;
	protected String accessCode;
	protected String ownerAccountId;
	protected List<String> accountIds;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	/**
	 * 
	 * @param dataCastClient
	 */
	public ChannelMetadataImpl(DataCastClient dataCastClient) {
		this.dataCastClient = dataCastClient;
	}

	/**
	 * 
	 * @param dataCastId
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param accessType
	 * @param accessCode
	 * @param ownerAccountId
	 * @param accountIds
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ChannelMetadataImpl(String dataCastId, String dataTubeId, String channelId, String name, String accessType, String accessCode, String ownerAccountId, List<String> accountIds, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;
		this.accessType = accessType;
		this.accessCode = accessCode;
		this.ownerAccountId = ownerAccountId;
		this.accountIds = accountIds;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
	}

	@Override
	public DataCastClient getDataCastClient() {
		return this.dataCastClient;
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
	public String getAccessType() {
		return this.accessType;
	}

	@Override
	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	@Override
	public String getAccessCode() {
		return this.accessCode;
	}

	@Override
	public void setAccessCode(String accessCode) {
		this.accessCode = accessCode;
	}

	@Override
	public String getOwnerAccountId() {
		return this.ownerAccountId;
	}

	@Override
	public void setOwnerAccountId(String ownerAccountId) {
		this.ownerAccountId = ownerAccountId;
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

	protected ChannelStatus status = null;

	@Override
	public ChannelStatus getStatus() {
		int value = (int) this.properties.get("status");
		if (this.status != null) {
			if (this.status.value() != value) {
				this.status.setValue(value);
			}
		} else {
			this.status = new ChannelStatus(value);
		}
		return this.status;
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
