package org.orbit.component.cli;

import java.util.Date;

import org.orbit.infra.api.InfraConstants;
import org.origin.common.loadbalance.LoadBalanceResource;

public class ResourcePropertyHelper {

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	public <S> Integer getIndexItemId(LoadBalanceResource<S> resource) {
		return (Integer) resource.getProperty(InfraConstants.INDEX_ITEM_ID);
	}

	public <S> Date getLastHeartbeatTime(LoadBalanceResource<S> resource) {
		Date time = null;
		Object value = resource.getProperty(InfraConstants.SERVICE__LAST_HEARTBEAT_TIME);
		if (value instanceof Date) {
			time = (Date) value;
		}
		return time;
	}

	public <S> String getProperty(LoadBalanceResource<S> resource, String name) {
		return (String) resource.getProperty(name);
	}

}
