package org.orbit.infra.model.subs.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.origin.common.util.DateUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsMappingImpl implements SubsMapping {

	protected Integer id;
	protected Integer sourceId;
	protected Integer targetId;

	protected String clientId;
	protected String clientURL;
	protected Date clientHeartbeatTime;

	protected String serverId;
	protected String serverURL;
	protected Date serverHeartbeatTime;

	protected Map<String, String> properties = new LinkedHashMap<String, String>();

	protected Date createdTime;
	protected Date modifiedTime;

	public SubsMappingImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param sourceId
	 * @param targetId
	 * @param clientId
	 * @param clientUrl
	 * @param createdTime
	 * @param modifiedTime
	 */
	public SubsMappingImpl(Integer id, Integer sourceId, Integer targetId, String clientId, String clientUrl, Date createdTime, Date modifiedTime) {
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;
		this.clientId = clientId;
		this.clientURL = clientUrl;
		this.createdTime = createdTime;
		this.modifiedTime = modifiedTime;
	}

	@Override
	public Integer getId() {
		return this.id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public Integer getSourceId() {
		return this.sourceId;
	}

	@Override
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	@Override
	public Integer getTargetId() {
		return this.targetId;
	}

	@Override
	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	@Override
	public String getClientId() {
		return this.clientId;
	}

	@Override
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public String getClientURL() {
		return this.clientURL;
	}

	@Override
	public void setClientURL(String clientURL) {
		this.clientURL = clientURL;
	}

	@Override
	public Date getClientHeartbeatTime() {
		return this.clientHeartbeatTime;
	}

	@Override
	public void setClientHeartbeatTime(Date heartbeatTime) {
		this.clientHeartbeatTime = heartbeatTime;
	}

	@Override
	public String getServerId() {
		return this.serverId;
	}

	@Override
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@Override
	public String getServerURL() {
		return this.serverURL;
	}

	@Override
	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	@Override
	public Date getServerHeartbeatTime() {
		return this.serverHeartbeatTime;
	}

	@Override
	public void setServerHeartbeatTime(Date serverHeartbeatTime) {
		this.serverHeartbeatTime = serverHeartbeatTime;
	}

	@Override
	public Map<String, String> getProperties() {
		return this.properties;
	}

	@Override
	public Date getCreatedTime() {
		return this.createdTime;
	}

	@Override
	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	@Override
	public Date getModifiedTime() {
		return this.modifiedTime;
	}

	@Override
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	@Override
	public String toString() {
		String clientHeartbeatTimeStr = (this.clientHeartbeatTime != null) ? DateUtil.toString(this.clientHeartbeatTime, DateUtil.getJdbcDateFormat()) : null;
		String serverHeartbeatTimeStr = (this.serverHeartbeatTime != null) ? DateUtil.toString(this.serverHeartbeatTime, DateUtil.getJdbcDateFormat()) : null;
		String createdTimeStr = (this.createdTime != null) ? DateUtil.toString(this.createdTime, DateUtil.getJdbcDateFormat()) : null;
		String modifiedTimeStr = (this.modifiedTime != null) ? DateUtil.toString(this.modifiedTime, DateUtil.getJdbcDateFormat()) : null;

		return "SubsMappingImpl [id=" + id + ", sourceId=" + sourceId + ", targetId=" + targetId + ", clientId=" + clientId + ", clientURL=" + clientURL + ", clientHeartbeatTime=" + clientHeartbeatTimeStr + ", serverId=" + serverId + ", serverURL=" + serverURL + ", serverHeartbeatTime=" + serverHeartbeatTimeStr + ", properties=" + properties + ", createdTime=" + createdTimeStr + ", modifiedTime=" + modifiedTimeStr + "]";
	}

}
