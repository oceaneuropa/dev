package org.orbit.infra.runtime.datacast.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.orbit.infra.runtime.datacast.service.ChannelMetadata;
import org.orbit.infra.runtime.datacast.service.ChannelStatus;
import org.origin.common.model.AccountConfig;

public class ChannelMetadataImpl implements ChannelMetadata {

	protected String dataCastId;
	protected String dataTubeId;
	protected String channelId;
	protected String name;
	protected String accessType;
	protected String accessCode;
	protected String ownerAccountId;
	protected List<AccountConfig> accountConfigs;
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
	 * @param accessType
	 * @param accessCode
	 * @param ownerAccountId
	 * @param accountConfigs
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public ChannelMetadataImpl(String dataCastId, String dataTubeId, String channelId, String name, String accessType, String accessCode, String ownerAccountId, List<AccountConfig> accountConfigs, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.dataCastId = dataCastId;
		this.dataTubeId = dataTubeId;
		this.channelId = channelId;
		this.name = name;
		this.accessType = accessType;
		this.accessCode = accessCode;
		this.ownerAccountId = ownerAccountId;
		this.accountConfigs = accountConfigs;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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
	public synchronized List<AccountConfig> getAccountConfigs() {
		if (this.accountConfigs == null) {
			this.accountConfigs = new ArrayList<AccountConfig>();
		}
		return this.accountConfigs;
	}

	@Override
	public synchronized void setAccountConfigs(List<AccountConfig> accountConfigs) {
		this.accountConfigs = accountConfigs;
	}

	@Override
	public synchronized void addAccountConfig(AccountConfig accountConfig) {
		if (accountConfig == null) {
			return;
		}
		if (this.accountConfigs == null) {
			this.accountConfigs = new ArrayList<AccountConfig>();
		}
		if (!this.accountConfigs.contains(accountConfig)) {
			this.accountConfigs.add(accountConfig);
		}
	}

	@Override
	public synchronized void removeAccountConfig(AccountConfig accountConfig) {
		if (accountConfig == null) {
			return;
		}
		if (this.accountConfigs == null) {
			this.accountConfigs = new ArrayList<AccountConfig>();
		}
		if (this.accountConfigs.contains(accountConfig)) {
			this.accountConfigs.remove(accountConfig);
		}
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
			if (this.status.getMode() != value) {
				this.status.setMode(value);
			}
		} else {
			this.status = new ChannelStatus(value);
		}
		return this.status;
	}

	@Override
	public void setStatus(ChannelStatus status) {
		this.status = status;
		updateProperties(this.status);
	}

	@Override
	public void appendStatus(ChannelStatus status) {
		if (status == null) {
			return;
		}
		if (this.status == null) {
			this.status = status;
		} else {
			this.status.setMode(this.status.getMode() | status.getMode());
		}
		updateProperties(this.status);
	}

	@Override
	public void clearStatus(ChannelStatus status) {
		if (this.status == null || status == null) {
			return;
		}
		this.status.setMode(this.status.getMode() & ~status.getMode());
		updateProperties(this.status);
	}

	protected void updateProperties(ChannelStatus status) {
		int value = (status != null) ? status.getMode() : 0;
		this.properties.put("status", new Integer(value));
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
