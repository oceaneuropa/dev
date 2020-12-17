package org.orbit.infra.model.subs.impl;

import java.util.LinkedHashMap;
import java.util.Map;

import org.orbit.infra.model.subs.SubsMapping;
import org.origin.common.json.JSONUtil;
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
	protected long clientHeartbeatTime;

	protected Map<String, Object> properties = new LinkedHashMap<String, Object>();
	protected long dateCreated;
	protected long dateModified;

	public SubsMappingImpl() {
	}

	/**
	 * 
	 * @param id
	 * @param sourceId
	 * @param targetId
	 * @param clientId
	 * @param clientUrl
	 * @param clientHeartbeatTime
	 * @param properties
	 * @param dateCreated
	 * @param dateModified
	 */
	public SubsMappingImpl(Integer id, Integer sourceId, Integer targetId, String clientId, String clientUrl, long clientHeartbeatTime, Map<String, Object> properties, long dateCreated, long dateModified) {
		this.id = id;
		this.sourceId = sourceId;
		this.targetId = targetId;

		this.clientId = clientId;
		this.clientURL = clientUrl;
		this.clientHeartbeatTime = clientHeartbeatTime;

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
	public long getClientHeartbeatTime() {
		return this.clientHeartbeatTime;
	}

	@Override
	public void setClientHeartbeatTime(long heartbeatTime) {
		this.clientHeartbeatTime = heartbeatTime;
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
		String clientHeartbeatTimeStr = DateUtil.toString(this.clientHeartbeatTime, DateUtil.getJdbcDateFormat());
		String propertiesString = JSONUtil.toJsonString(this.properties);
		String dateCreatedStr = DateUtil.toString(this.dateCreated, DateUtil.getJdbcDateFormat());
		String dateModifiedStr = DateUtil.toString(this.dateModified, DateUtil.getJdbcDateFormat());

		return "SubsMappingImpl [id=" + id + ", sourceId=" + sourceId + ", targetId=" + targetId + ", clientId=" + clientId + ", clientURL=" + clientURL + ", clientHeartbeatTime=" + clientHeartbeatTimeStr + ", properties=" + propertiesString + ", dateCreated=" + dateCreatedStr + ", dateModified=" + dateModifiedStr + "]";
	}

}
