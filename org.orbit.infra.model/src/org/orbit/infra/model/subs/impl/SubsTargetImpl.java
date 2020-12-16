package org.orbit.infra.model.subs.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.subs.SubsTarget;
import org.origin.common.util.DateUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTargetImpl implements SubsTarget {

	protected Integer id;
	protected String name;
	protected String type;
	protected String typeId;

	protected String serverId;
	protected String serverURL;
	protected Date serverHeartbeatTime;

	protected Map<String, String> properties = new LinkedHashMap<String, String>();
	protected Date createdTime;
	protected Date modifiedTime;

	public SubsTargetImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param name
	 * @param type
	 * @param typeId
	 * @param createdTime
	 * @param modifiedTime
	 */
	public SubsTargetImpl(Integer id, String name, String type, String typeId, Date createdTime, Date modifiedTime) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.typeId = typeId;
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getType() {
		return this.type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String getTypeId() {
		return this.typeId;
	}

	@Override
	public void setTypeId(String typeId) {
		this.typeId = typeId;
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
		String serverHeartbeatTimeStr = (this.serverHeartbeatTime != null) ? DateUtil.toString(this.serverHeartbeatTime, DateUtil.getJdbcDateFormat()) : null;
		String createdTimeStr = (this.createdTime != null) ? DateUtil.toString(this.createdTime, DateUtil.getJdbcDateFormat()) : null;
		String modifiedTimeStr = (this.modifiedTime != null) ? DateUtil.toString(this.modifiedTime, DateUtil.getJdbcDateFormat()) : null;

		return "SubsTargetImpl [id=" + id + ", name=" + name + ", type=" + type + ", typeId=" + typeId + ", serverId=" + serverId + ", serverURL=" + serverURL + ", serverHeartbeatTime=" + serverHeartbeatTimeStr + ", properties=" + properties + ", createdTime=" + createdTimeStr + ", modifiedTime=" + modifiedTimeStr + "]";
	}

}
