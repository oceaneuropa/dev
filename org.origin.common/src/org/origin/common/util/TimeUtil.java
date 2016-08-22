package org.origin.common.util;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

	/**
	 * 
	 * @param beforeTime
	 * @param timeToAdd
	 * @param timeUnit
	 * @return
	 */
	public static Date addTimeToDate(Date beforeTime, long timeToAdd, TimeUnit timeUnit) {
		long curTimeInMs = beforeTime.getTime();
		long millisecsToAdd = TimeUnit.MILLISECONDS.convert(timeToAdd, timeUnit);
		return new Date(curTimeInMs + millisecsToAdd);
	}

}
