package org.origin.common.util;

import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TimeUtil {

	public static final List<Long> times = Arrays.asList( //
			DAYS.toMillis(365), //
			DAYS.toMillis(30), //
			DAYS.toMillis(7), //
			DAYS.toMillis(1), //
			HOURS.toMillis(1), //
			MINUTES.toMillis(1), //
			SECONDS.toMillis(1) //
	);

	public static final List<String> timesString = Arrays.asList( //
			"year", //
			"month", //
			"week", //
			"day", //
			"hour", //
			"minute", //
			"second" //
	);

	/**
	 * Get relative time ago for date
	 *
	 * NOTE: if (duration > WEEK_IN_MILLIS) getRelativeTimeSpanString prints the date.
	 *
	 * ALT: return getRelativeTimeSpanString(date, now, SECOND_IN_MILLIS, FORMAT_ABBREV_RELATIVE);
	 *
	 * @see https://stackoverflow.com/questions/3859288/how-to-calculate-time-ago-in-java/37042254#37042254
	 * @param date
	 *            String.valueOf(TimeUtils.getRelativeTime(1000L * Date/Time in Millis)
	 * @return relative time
	 */
	public static CharSequence getRelativeTime(final long date) {
		return toDuration(Math.abs(System.currentTimeMillis() - date));
	}

	private static String toDuration(long duration) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < times.size(); i++) {
			Long current = times.get(i);
			long temp = duration / current;
			if (temp > 0) {
				sb.append(temp).append(" ").append(timesString.get(i)).append(temp > 1 ? "s" : "").append(" ago");
				break;
			}
		}
		return sb.toString().isEmpty() ? "now" : sb.toString();
	}

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
