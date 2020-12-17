package org.orbit.infra.model.subs.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.subs.SubsTarget;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.DateUtil;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTargetImpl implements SubsTarget {

	protected Integer id;
	protected String type;
	protected String typeId;
	protected String name;

	protected String serverId;
	protected String serverURL;
	protected long serverHeartbeatTime;

	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected long dateCreated;
	protected long dateModified;

	public SubsTargetImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param type
	 * @param typeId
	 * @param name
	 * @param serverId
	 * @param serverURL
	 * @param serverHeartbeatTime
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public SubsTargetImpl(Integer id, String type, String typeId, String name, String serverId, String serverURL, long serverHeartbeatTime, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.id = id;
		this.type = type;
		this.typeId = typeId;
		this.name = name;
		this.serverId = serverId;
		this.serverURL = serverURL;
		this.serverHeartbeatTime = serverHeartbeatTime;
		this.properties = properties;
		this.dateCreated = dateCreated;
		this.dateModified = dateModified;
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
	public String getName() {
		return this.name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
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
	public long getServerHeartbeatTime() {
		return this.serverHeartbeatTime;
	}

	@Override
	public void setServerHeartbeatTime(long serverHeartbeatTime) {
		this.serverHeartbeatTime = serverHeartbeatTime;
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.properties;
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
		String serverHeartbeatTimeStr = DateUtil.toString(this.serverHeartbeatTime, DateUtil.getJdbcDateFormat());
		String propertiesString = JSONUtil.toJsonString(this.properties);
		String dateCreatedStr = DateUtil.toString(this.dateCreated, DateUtil.getJdbcDateFormat());
		String dateModifiedStr = DateUtil.toString(this.dateModified, DateUtil.getJdbcDateFormat());

		return "SubsTargetImpl [id=" + id + ", type=" + type + ", typeId=" + typeId + ", name=" + name + ", serverId=" + serverId + ", serverURL=" + serverURL + ", serverHeartbeatTime=" + serverHeartbeatTimeStr + ", properties=" + propertiesString + ", dateCreated=" + dateCreatedStr + ", dateModified=" + dateModifiedStr + "]";
	}

}
