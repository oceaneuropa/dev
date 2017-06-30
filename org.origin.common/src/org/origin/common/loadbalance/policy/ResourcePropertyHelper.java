package org.origin.common.loadbalance.policy;

import org.origin.common.loadbalance.LoadBalanceResource;

public class ResourcePropertyHelper {

	public static String HEARTBEAT_EXPIRED = "heartbeat_expired";

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	// ------------------------------------------------------------------------------------
	// "heartbeat_expired" property
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
