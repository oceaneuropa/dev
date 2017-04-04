package org.origin.common.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	/*
	 * @see http://docs.oracle.com/javase/1.5.0/docs/api/java/text/SimpleDateFormat.html
	 */
	public static SimpleDateFormat SIMPLE_DATE_FORMAT0 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
	public static SimpleDateFormat SIMPLE_DATE_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	public static SimpleDateFormat SIMPLE_DATE_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat SIMPLE_DATE_FORMAT3 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
	public static SimpleDateFormat YEAR_MONTH_DAY_FORMAT1 = new SimpleDateFormat("yyyy-MM-dd");
	public static SimpleDateFormat MONTH_DAY_YEAR_FORMAT1 = new SimpleDateFormat("MM/dd/yyyy");

	public static SimpleDateFormat[] COMMON_DATE_FORMATS;

	static {
		COMMON_DATE_FORMATS = new SimpleDateFormat[] { SIMPLE_DATE_FORMAT0, SIMPLE_DATE_FORMAT1, SIMPLE_DATE_FORMAT2, SIMPLE_DATE_FORMAT3 };
	}

	public static SimpleDateFormat[] getCommonDateFormats() {
		return COMMON_DATE_FORMATS;
	}

	public static SimpleDateFormat getDefaultDateFormat() {
		return SIMPLE_DATE_FORMAT0;
	}

	public static SimpleDateFormat getJdbcDateFormat() {
		return SIMPLE_DATE_FORMAT3;
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
			} catch (Exception e) {
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

	/**
	 * 
	 * @param firstDate
	 * @param laterDate
	 * @return
	 */
	public static long getDaysBetween(Date firstDate, Date laterDate) {
		Calendar laterCalendar = new GregorianCalendar();
		laterCalendar.setTime(laterDate);
		int year2 = laterCalendar.get(Calendar.YEAR);
		int month2 = laterCalendar.get(Calendar.MONTH) + 1;
		int day2 = laterCalendar.get(Calendar.DAY_OF_MONTH);

		Calendar firstCalendar = new GregorianCalendar();
		firstCalendar.setTime(firstDate);
		int year1 = firstCalendar.get(Calendar.YEAR);
		int month1 = firstCalendar.get(Calendar.MONTH) + 1;
		int day1 = firstCalendar.get(Calendar.DAY_OF_MONTH);

		LocalDate laterLocalDate = LocalDate.of(year2, month2, day2);
		LocalDate firstLocalDate = LocalDate.of(year1, month1, day1);

		long daysBetween = ChronoUnit.DAYS.between(firstLocalDate, laterLocalDate);

		return daysBetween;
	}

}
