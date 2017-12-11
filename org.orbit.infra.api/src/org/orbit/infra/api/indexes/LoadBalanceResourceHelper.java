package org.orbit.infra.api.indexes;

import java.util.Date;

import org.orbit.infra.api.OrbitConstants;
import org.origin.common.loadbalance.LoadBalanceResource;
import org.origin.common.util.DateUtil;

public class LoadBalanceResourceHelper {

	public static LoadBalanceResourceHelper INSTANCE = new LoadBalanceResourceHelper();

	// ------------------------------------------------------------------------------------
	// "last_heartbeat_time" property
	// ------------------------------------------------------------------------------------
	public <S> boolean hasLastHeartBeatTime(LoadBalanceResource<S> resource) {
		if (resource.hasProperty(OrbitConstants.LAST_HEARTBEAT_TIME)) {
			Object value = resource.getProperty(OrbitConstants.LAST_HEARTBEAT_TIME);
			if (value instanceof Date) {
				return true;

			} else if (value instanceof String) {
				Date date = DateUtil.toDate((String) value, DateUtil.getCommonDateFormats());
				if (date != null) {
					return true;
				}

			} else if (value instanceof Long) {
				Date date = DateUtil.toDate((Long) value);
				if (date != null) {
					return true;
				}
			}
		}
		return false;
	}

	public <S> Date getLastHeartBeatTime(LoadBalanceResource<S> resource) {
		Date lastHeartBeatTime = null;
		if (resource.hasProperty(OrbitConstants.LAST_HEARTBEAT_TIME)) {
			Object value = resource.getProperty(OrbitConstants.LAST_HEARTBEAT_TIME);
			if (value instanceof Date) {
				lastHeartBeatTime = (Date) value;

			} else if (value instanceof String) {
				lastHeartBeatTime = DateUtil.toDate((String) value, DateUtil.getCommonDateFormats());

			} else if (value instanceof Long) {
				lastHeartBeatTime = DateUtil.toDate((Long) value);
			}
		}
		return lastHeartBeatTime;
	}

	// ------------------------------------------------------------------------------------
	// "heartbeat_expire_time" property
	// ------------------------------------------------------------------------------------
	public <S> Date getHeartBeatExpireTime(LoadBalanceResource<S> resource) {
		Date heartBeatExpireTime = null;
		if (resource.hasProperty(OrbitConstants.HEARTBEAT_EXPIRE_TIME)) {
			Object value = resource.getProperty(OrbitConstants.HEARTBEAT_EXPIRE_TIME);
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

	// ------------------------------------------------------------------------------------
	// "last_ping_time" property
	// ------------------------------------------------------------------------------------
	public <S> boolean hasLastPingTime(LoadBalanceResource<S> resource) {
		if (resource.hasProperty(OrbitConstants.LAST_PING_TIME)) {
			Object value = resource.getProperty(OrbitConstants.LAST_PING_TIME);
			if (value instanceof Date) {
				return true;

			} else if (value instanceof String) {
				Date date = DateUtil.toDate((String) value, DateUtil.getCommonDateFormats());
				if (date != null) {
					return true;
				}

			} else if (value instanceof Long) {
				Date date = DateUtil.toDate((Long) value);
				if (date != null) {
					return true;
				}
			}
		}
		return false;
	}

	public <S> Date getLastPingTime(LoadBalanceResource<S> resource) {
		Date lastHeartBeatTime = null;
		if (resource.hasProperty(OrbitConstants.LAST_PING_TIME)) {
			Object value = resource.getProperty(OrbitConstants.LAST_PING_TIME);
			if (value instanceof Date) {
				lastHeartBeatTime = (Date) value;

			} else if (value instanceof String) {
				lastHeartBeatTime = DateUtil.toDate((String) value, DateUtil.getCommonDateFormats());

			} else if (value instanceof Long) {
				lastHeartBeatTime = DateUtil.toDate((Long) value);
			}
		}
		return lastHeartBeatTime;
	}

	// ------------------------------------------------------------------------------------
	// "last_ping_succeed" property
	// ------------------------------------------------------------------------------------
	public <S> boolean hasLastPingSucceed(LoadBalanceResource<S> resource) {
		if (resource.hasProperty(OrbitConstants.LAST_PING_SUCCEED)) {
			Object value = resource.getProperty(OrbitConstants.LAST_PING_SUCCEED);
			if (value instanceof Boolean) {
				return true;

			} else if (value instanceof String) {
				try {
					Boolean.parseBoolean((String) value);
					return true;
				} catch (Exception e) {
				}
			}
		}
		return false;
	}

	public <S> boolean isLastPingSucceed(LoadBalanceResource<S> resource) {
		Boolean lastPingSucceed = null;
		if (resource.hasProperty(OrbitConstants.LAST_PING_SUCCEED)) {
			Object value = resource.getProperty(OrbitConstants.LAST_PING_SUCCEED);
			if (value instanceof Boolean) {
				lastPingSucceed = (Boolean) value;

			} else if (value instanceof String) {
				try {
					lastPingSucceed = Boolean.parseBoolean((String) value);
				} catch (Exception e) {
				}
			}
		}
		if (lastPingSucceed == null) {
			lastPingSucceed = Boolean.FALSE;
		}
		return lastPingSucceed;
	}

}

// public <S> boolean isHeartBeatExpired(LoadBalanceResource<S> resource, long period) {
// Date lastHeartBeatTime = getLastHeartBeatTime(resource);
// if (lastHeartBeatTime != null) {
// Date nowTime = new Date();
// long difference = nowTime.getTime() - lastHeartBeatTime.getTime();
// if (difference > period) {
// return true;
// }
// }
// return false;
// }
