package net.meisen.general.genmisc.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

	/**
	 * Creates a <code>Date</code> instance based on the passed
	 * <code>date</code> and <code>format</code>.
	 * 
	 * @param date
	 *            the date to be parsed to a <code>Date</code> instance
	 * @param format
	 *            the format of the <code>date</code>
	 * @return the <code>Date</code> instance representing the passed
	 *         <code>date</code>
	 * 
	 * @throws ParseException
	 *             if the <code>date</code> cannot be parsed by the specified
	 *             <code>format</code>
	 * 
	 * @see Date
	 * @see DateFormat
	 * @see SimpleDateFormat
	 */
	public static Date createDateFromString(final String date,
			final String format) throws ParseException {
		final DateFormat formatter = new SimpleDateFormat(format);
		return formatter.parse(date);
	}

	/**
	 * Formats the <code>date</code> according to the specified
	 * <code>format</code>.
	 * 
	 * @param date
	 *            the date to be formatted
	 * @param format
	 *            the format of be applied to the <code>date</code>
	 * 
	 * @return the formatted date
	 * 
	 * @throws IllegalArgumentException
	 *             if the Format cannot format the given object
	 * 
	 * @see Date
	 * @see DateFormat
	 * @see SimpleDateFormat
	 */
	public static String formatDate(final Date date, final String format)
			throws IllegalArgumentException {
		final DateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
}
