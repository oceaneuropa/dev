package org.orbit.infra.model.subs.dto;

import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.origin.common.json.JSONUtil;

@XmlRootElement
public class SubsMappingDTO {

	@XmlElement
	protected Integer id;
	@XmlElement
	protected Integer sourceId;
	@XmlElement
	protected Integer targetId;
	@XmlElement
	protected String clientId;
	@XmlElement
	protected String clientURL;
	@XmlElement
	protected long clientHeartbeatTime;
	@XmlElement
	protected String properties;
	@XmlElement
	protected long dateCreated;
	@XmlElement
	protected long dateModified;

	public SubsMappingDTO() {
	}

	@XmlElement
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@XmlElement
	public Integer getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	@XmlElement
	public Integer getTargetId() {
		return this.targetId;
	}

	public void setTargetId(Integer targetId) {
		this.targetId = targetId;
	}

	@XmlElement
	public String getClientId() {
		return this.clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	@XmlElement
	public String getClientURL() {
		return this.clientURL;
	}

	public void setClientURL(String clientURL) {
		this.clientURL = clientURL;
	}

	@XmlElement
	public long getClientHeartbeatTime() {
		return this.clientHeartbeatTime;
	}

	public void setClientHeartbeatTime(long clientHeartbeatTime) {
		this.clientHeartbeatTime = clientHeartbeatTime;
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
		return dateModified;
	}

	public void setDateModified(long dateModified) {
		this.dateModified = dateModified;
	}

}
