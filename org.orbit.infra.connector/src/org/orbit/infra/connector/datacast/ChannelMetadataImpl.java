package org.orbit.infra.connector.datacast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.orbit.infra.api.datacast.ChannelStatus;
import org.orbit.infra.api.datacast.DataCastClient;
import org.origin.common.adapter.AdaptorSupport;
import org.origin.common.model.AccountConfig;

public class ChannelMetadataImpl implements ChannelMetadata {

	protected DataCastClient dataCastClient;

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;
	protected ChannelStatus channelStatus;
	protected String accessType;
	protected String accessCode;
	protected String ownerAccountId;
	protected List<AccountConfig> accountConfigs;
	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	protected AdaptorSupport adaptorSupport = new AdaptorSupport();

	/**
	 * 
	 * @param dataCastClient
	 */
	public ChannelMetadataImpl(DataCastClient dataCastClient) {
		this.dataCastClient = dataCastClient;
	}

	/**
	 * 
	 * @param dataCastClient
	 * @param dataCastId
	 * @param dataTubeId
	 * @param channelId
	 * @param name
	 * @param status
	 * @param accessType
	 * @param accessCode
	 * @param ownerAccountId
	 * @param accountConfigs
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ChannelMetadataImpl(DataCastClient dataCastClient, String dataCastId, String dataTubeId, String channelId, String name, ChannelStatus status, String accessType, String accessCode, String ownerAccountId, List<AccountConfig> accountConfigs, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dataCastClient = dataCastClient;

		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;
		this.channelStatus = status;
		this.accessType = accessType;
		this.accessCode = accessCode;
		this.ownerAccountId = ownerAccountId;
		this.accountConfigs = accountConfigs;
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
	public ChannelStatus getStatus() {
		int value = 0;
		Object obj = getProperties().get("channel_status");
		if (obj instanceof Integer) {
			value = (int) obj;
		} else if (obj != null) {
			try {
				value = Integer.parseInt(obj.toString());
			} catch (Exception e) {
			}
		}
		if (this.channelStatus != null) {
			if (this.channelStatus.getMode() != value) {
				this.channelStatus.setMode(value);
			}
		} else {
			this.channelStatus = new ChannelStatus(value);
		}
		return this.channelStatus;
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
	public synchronized List<AccountConfig> getAccountConfigs() {
		if (this.accountConfigs == null) {
			this.accountConfigs = new ArrayList<AccountConfig>();
		}
		return this.accountConfigs;
	}

	public synchronized void setAccountConfigs(List<AccountConfig> accountConfigs) {
		this.accountConfigs = accountConfigs;
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

// protected List<String> accountIds;
// @Override
// public synchronized List<String> getAccountIds() {
// if (this.accountIds == null) {
// this.accountIds = new ArrayList<String>();
// }
// return this.accountIds;
// }
// public synchronized void setAccountIds(List<String> accountIds) {
// this.accountIds = accountIds;
// }
