package org.orbit.component.cli.tier3;

import java.util.Date;

import org.origin.common.loadbalance.LoadBalanceResource;

public class ResourcePropertyHelper {

	public static String INDEX_ITEM_ID = "index_item_id";
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRED = "heartbeat_expired";

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	public <S> Integer getIndexItemId(LoadBalanceResource<S> resource) {
		return (Integer) resource.getProperty(INDEX_ITEM_ID);
	}

	public <S> Date getHeartbeatTime(LoadBalanceResource<S> resource) {
		Long time = (Long) resource.getProperty(LAST_HEARTBEAT_TIME);
		return new Date(time);
	}

	public <S> boolean isHeartBeatExpired(LoadBalanceResource<S> resource) {
		if (resource.hasProperty(HEARTBEAT_EXPIRED)) {
			Object value = resource.getProperty(HEARTBEAT_EXPIRED);
			if (value instanceof Boolean) {
				return (Boolean) value;
			}
		}
		return false;
	}

	public <S> String getProperty(LoadBalanceResource<S> resource, String name) {
		return (String) resource.getProperty(name);
	}

}
