package org.orbit.component.cli.tier3;

import java.util.Date;

import org.origin.common.loadbalance.LoadBalanceResource;

public class ResourcePropertyHelper {

	public static String INDEX_ITEM_ID = "index_item_id";
	public static String LAST_HEARTBEAT_TIME = "last_heartbeat_time";
	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	public <S> Integer getIndexItemId(LoadBalanceResource<S> resource) {
		return (Integer) resource.getProperty(INDEX_ITEM_ID);
	}

	public <S> Date getLastHeartbeatTime(LoadBalanceResource<S> resource) {
		Date time = null;
		Object value = resource.getProperty(LAST_HEARTBEAT_TIME);
		if (value instanceof Date) {
			time = (Date) value;
		}
		return time;
	}

	public <S> Date getHeartbeatExpireTime(LoadBalanceResource<S> resource) {
		Date time = null;
		Object value = resource.getProperty(HEARTBEAT_EXPIRE_TIME);
		if (value instanceof Date) {
			time = (Date) value;
		}
		return time;
	}

	public <S> String getProperty(LoadBalanceResource<S> resource, String name) {
		return (String) resource.getProperty(name);
	}

}
