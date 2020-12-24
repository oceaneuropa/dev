package org.orbit.infra.connector.subscription;

import java.util.Map;
import java.util.TreeMap;

import org.orbit.infra.api.subscription.SubsServerAPI;
import org.orbit.infra.api.subscription.SubsTarget;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class SubsTargetImpl implements SubsTarget {

	protected SubsServerAPI api;
	protected Integer id;
	protected String type;
	protected String instanceId;
	protected String name;

	protected String serverId;
	protected String serverURL;
	protected long serverHeartbeatTime;

	protected Map<String, Object> properties;
	protected long dateCreated;
	protected long dateModified;

	public SubsTargetImpl() {
	}

	public SubsServerAPI getAPI() {
		return this.api;
	}

	public void setAPI(SubsServerAPI api) {
		this.api = api;
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
	public String getInstanceId() {
		return this.instanceId;
	}

	@Override
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
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
	public synchronized Map<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new TreeMap<String, Object>();
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

}
