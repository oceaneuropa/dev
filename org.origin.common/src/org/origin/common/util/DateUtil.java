package org.origin.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

	public static SimpleDateFormat SIMPLE_DATE_FORMAT0 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	public static SimpleDateFormat SIMPLE_DATE_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat SIMPLE_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat[] COMMON_DATE_FORMATS;

	static {
		COMMON_DATE_FORMATS = new SimpleDateFormat[] { SIMPLE_DATE_FORMAT0, SIMPLE_DATE_FORMAT1, SIMPLE_DATE_FORMAT2 };
	}

	public static SimpleDateFormat getDefaultDateFormat() {
		return SIMPLE_DATE_FORMAT0;
	}

	public static SimpleDateFormat[] getCommonDateFormats() {
		return COMMON_DATE_FORMATS;
	}

	/**
	 * 
	 * @param dateString
	 * @param formats
	 * @return
	 */
	public static Date toDate(String dateString, DateFormat... formats) {
		if (dateString == null) {
			throw new IllegalArgumentException("dateString is null.");
		}
		if (formats == null) {
			throw new IllegalArgumentException("formats is null.");
		}
		Date date = null;
		for (DateFormat format : formats) {
			try {
				date = format.parse(dateString);
				if (date != null) {
					break;
				}
			} catch (ParseException e) {
				// e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * 
	 * @param time
	 * @return
	 */
	public static Date toDate(long time) {
		return new Date(time);
	}

	/**
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public static String toString(Date date, DateFormat format) {
		if (date == null) {
			throw new IllegalArgumentException("date is null.");
		}
		if (format == null) {
			throw new IllegalArgumentException("format is null.");
		}
		return format.format(date);
	}

}
