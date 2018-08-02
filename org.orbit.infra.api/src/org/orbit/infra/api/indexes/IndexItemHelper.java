package org.orbit.infra.api.indexes;

import java.util.Date;

import org.orbit.infra.api.InfraConstants;

public class IndexItemHelper {

	public static String PLATFORM_RUNTIME_STATE = "platform.runtime_state";

	public static IndexItemHelper INSTANCE = new IndexItemHelper();

	/**
	 * 
	 * @param indexItem
	 * @return
	 */
	public boolean isOnline(IndexItem indexItem) {
		if (indexItem != null && isLastHeartbeatWithinSeconds(indexItem, 20)) {
			String indexerId = indexItem.getIndexProviderId();

			boolean checkStarted = false;
			if (InfraConstants.PLATFORM_INDEXER_ID.equals(indexerId)) {
				checkStarted = true;
			}

			if (checkStarted) {
				String runtimeState = (String) indexItem.getProperties().get(PLATFORM_RUNTIME_STATE);
				if ("STARTED".equalsIgnoreCase(runtimeState)) {
					return true;
				}
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param indexItem
	 * @param withinSeconds
	 * @return
	 */
	public boolean isLastHeartbeatWithinSeconds(IndexItem indexItem, long withinSeconds) {
		Date lastHeartbeatTime = getLastUpdateTime(indexItem);
		Date now = new Date();
		if (lastHeartbeatTime != null) {
			long milliseconds = now.getTime() - lastHeartbeatTime.getTime();
			long withinMilliseconds = withinSeconds * 1000;
			if (milliseconds <= withinMilliseconds) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param indexItem
	 * @return
	 */
	public Date getLastUpdateTime(IndexItem indexItem) {
		Date date = null;
		if (indexItem != null) {
			Object value = indexItem.getProperties().get(IndexItem.LAST_HEARTBEAT_TIME);
			if (value instanceof Long) {
				date = new Date((Long) value);
			} else if (value instanceof Date) {
				date = (Date) value;
			}
		}
		return date;
	}

}
