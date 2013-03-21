package net.meisen.general.genmisc.types;

import java.util.Calendar;
import java.util.Date;

/**
 * Utility class when working with dates
 * 
 * @author pmeisen
 * 
 */
public class Dates {

	/**
	 * @param date
	 *            the {@link Date} to be truncated
	 * @return the truncated {@link Date}, i.e. no time information
	 */
	public static Date truncateDate(final Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}

	/**
	 * This function calculates the difference between two {@link Date} objects
	 * in minutes
	 * 
	 * @param date1
	 *            the {@link Date} object to be subtracted from
	 * @param date2
	 *            the {@link Date} object to subtract
	 * @return the difference of the two {@link Date} objects in minutes
	 */
	public static int getDateDiffInMinutes(final Date date1, final Date date2) {
		return Math.round((date1.getTime() - date2.getTime()) / 1000 / 60);
	}
}
