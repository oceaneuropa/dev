package org.orbit.infra.api.indexes;

import java.util.Date;

public class IndexItemHelper {

	public static IndexItemHelper INSTANCE = new IndexItemHelper();

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

}
