package org.orbit.infra.model.subs.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class SubsTargetDTO {

	@XmlElement
	protected Integer id;
	@XmlElement
	protected String type;
	@XmlElement
	protected String instanceId;
	@XmlElement
	protected String name;

	@XmlElement
	protected String serverId;
	@XmlElement
	protected String serverURL;
	@XmlElement
	protected long serverHeartbeatTime;

	@XmlElement
	protected String properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public SubsTargetDTO() {
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement
	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	@XmlElement
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement
	public String getServerId() {
		return this.serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	@XmlElement
	public String getServerURL() {
		return this.serverURL;
	}

	public void setServerURL(String serverURL) {
		this.serverURL = serverURL;
	}

	@XmlElement
	public long getServerHeartbeatTime() {
		return this.serverHeartbeatTime;
	}

	public void setServerHeartbeatTime(long serverHeartbeatTime) {
		this.serverHeartbeatTime = serverHeartbeatTime;
	}

	@XmlElement
	public String getPropertiesString() {
		return this.properties;
	}

	public void setProperties(String propertiesString) {
		this.properties = propertiesString;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = JSONUtil.toJsonString(properties);
	}

	@XmlElement
	public long getDateCreated() {
		return this.dateCreated;
	}

	public void setDateCreated(long dateCreated) {
		this.dateCreated = dateCreated;
	}

	@XmlElement
	public long getDateModified() {
		return this.dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
