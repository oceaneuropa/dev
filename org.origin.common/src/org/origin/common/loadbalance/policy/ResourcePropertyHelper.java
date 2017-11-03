package org.origin.common.loadbalance.policy;

import java.util.Date;

import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.util.DateUtil;

public class ResourcePropertyHelper {

	public static String HEARTBEAT_EXPIRE_TIME = "heartbeat_expire_time";

	public static ResourcePropertyHelper INSTANCE = new ResourcePropertyHelper();

	// ------------------------------------------------------------------------------------
	// "heartbeat_expire_time" property
	// ------------------------------------------------------------------------------------
	public <S> Date getHeartBeatExpireTime(LoadBalanceResource<S> resource) {
		Date heartBeatExpireTime = null;
		if (resource.hasProperty(HEARTBEAT_EXPIRE_TIME)) {
			Object value = resource.getProperty(HEARTBEAT_EXPIRE_TIME);
			if (value instanceof Date) {
				heartBeatExpireTime = (Date) value;

			} else if (value instanceof String) {
				heartBeatExpireTime = DateUtil.toDate((String) value, DateUtil.getCommonDateFormats());

			} else if (value instanceof Long) {
				heartBeatExpireTime = DateUtil.toDate((Long) value);
			}
		}
		return heartBeatExpireTime;
	}

	public <S> boolean isHeartBeatExpired(LoadBalanceResource<S> resource) {
		Date heartBeatExpireTime = getHeartBeatExpireTime(resource);
		if (heartBeatExpireTime == null) {
			return true;
		}
		if (heartBeatExpireTime.before(new Date())) {
			return true;
		}
		return false;
	}

}
