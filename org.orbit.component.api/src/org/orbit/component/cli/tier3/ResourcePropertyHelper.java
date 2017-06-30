package org.orbit.component.cli.tier3;

import java.util.Date;

import org.origin.common.loadbalance.LoadBalanceResource;

public class ResourcePropertyHelper {

	public static String HEARTBEAT_EXPIRED = "heartbeat_expired";

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	// ------------------------------------------------------------------------------------
	// "index_item_id"
	// ------------------------------------------------------------------------------------
	public <S> Integer getIndexItemId(LoadBalanceResource<S> resource) {
		return (Integer) resource.getProperty("index_item_id");
	}

	// ------------------------------------------------------------------------------------
	// "domain_mgmt.host.url"
	// ------------------------------------------------------------------------------------
	public <S> String getHostUrl(LoadBalanceResource<S> resource) {
		return (String) resource.getProperty("domain_mgmt.host.url");
	}

	// ------------------------------------------------------------------------------------
	// "domain_mgmt.context_root"
	// ------------------------------------------------------------------------------------
	public <S> String getContextRoot(LoadBalanceResource<S> resource) {
		return (String) resource.getProperty("domain_mgmt.context_root");
	}

	// ------------------------------------------------------------------------------------
	// "domain_mgmt.name"
	// ------------------------------------------------------------------------------------
	public <S> String getName(LoadBalanceResource<S> resource) {
		return (String) resource.getProperty("domain_mgmt.name");
	}

	// ------------------------------------------------------------------------------------
	// "last_heartbeat_time"
	// ------------------------------------------------------------------------------------
	public <S> Date getHeartbeatTime(LoadBalanceResource<S> resource) {
		Long time = (Long) resource.getProperty("last_heartbeat_time");
		return new Date(time);
	}

	// ------------------------------------------------------------------------------------
	// "heartbeat_expired"
	// ------------------------------------------------------------------------------------
	public <S> boolean isHeartBeatExpired(LoadBalanceResource<S> resource) {
		if (resource.hasProperty(HEARTBEAT_EXPIRED)) {
			Object value = resource.getProperty(HEARTBEAT_EXPIRED);
			if (value instanceof Boolean) {
				return (Boolean) value;
			}
		}
		return false;
	}

}
