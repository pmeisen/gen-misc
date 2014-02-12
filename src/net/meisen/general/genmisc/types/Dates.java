package net.meisen.general.genmisc.types;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utility class when working with dates
 * 
 * @author pmeisen
 * 
 */
public class Dates {
	/**
	 * The <code>TimeZone</code> used when none is provided.
	 * 
	 * @see TimeZone
	 */
	public static final String GENERAL_TIMEZONE = "UTC";

	/**
	 * Default patterns used to detect date formats within a string
	 * 
	 * @see #isDate(String)
	 */
	public static final String[] PATTERNS = new String[] { null,
			"dd.MM.yyyy HH:mm:ss", "dd.MM.yyyy", "yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd" };

	/**
	 * Checks if the presented {@code text} can be understood as {@code Date}
	 * using the patterns defined by {@link #PATTERNS}.
	 * 
	 * @param text
	 *            the text to be checked
	 * 
	 * @return the parsed {@code Date} or {@code null} if parsing wasn't
	 *         possible
	 */
	public static Date isDate(final String text) {
		return isDate(text, PATTERNS);
	}

	/**
	 * Checks if the presented {@code text} can be understood as {@code Date}
	 * using the patterns defined by {@link #PATTERNS}.
	 * 
	 * @param text
	 *            the text to be checked
	 * @param timezone
	 *            the timezone to parse the date in
	 * 
	 * @return the parsed {@code Date} or {@code null} if parsing wasn't
	 *         possible
	 */
	public static Date isDate(final String text, final String timezone) {
		return isDate(text, timezone, PATTERNS);
	}

	/**
	 * Checks if the presented {@code text} can be understood as {@code Date}
	 * using the specified {@code patterns}. The date will be parsed to the
	 * locale timezone, use {@link #isDate(String, String, String[])} to specify
	 * the timezone.
	 * 
	 * @param text
	 *            the text to be checked
	 * @param patterns
	 *            the patterns to be checked against, if {@code null} the
	 *            default patterns defined by {@link #PATTERNS} will be used
	 * 
	 * @return the parsed {@code Date} or {@code null} if parsing wasn't
	 *         possible
	 */
	public static Date isDate(final String text, final String[] patterns) {
		return isDate(text, TimeZone.getDefault().getID(), patterns);
	}

	/**
	 * Checks if the presented {@code text} can be understood as {@code Date}
	 * using the specified {@code patterns}. T
	 * 
	 * @param text
	 *            the text to be checked
	 * @param timezone
	 *            the timezone to parse the date to
	 * @param patterns
	 *            the patterns to be checked against, if {@code null} the
	 *            default patterns defined by {@link #PATTERNS} will be used
	 * 
	 * @return the parsed {@code Date} or {@code null} if parsing wasn't
	 *         possible
	 */
	public static Date isDate(final String text, final String timezone,
			final String[] patterns) {
		if (text == null) {
			return null;
		} else if ("".equals(text)) {
			return null;
		} else {

			// we have to validate the format of the String
			final SimpleDateFormat formatter = new SimpleDateFormat();
			for (final String pattern : (patterns == null ? PATTERNS : patterns)) {

				if (pattern != null) {
					try {
						formatter.applyPattern(pattern);
					} catch (final IllegalArgumentException e) {
						return null;
					}
				}

				try {
					return parseDate(text, pattern, timezone);
				} catch (final ParseException e) {
					// nothing ignore it
				}
			}

			return null;
		}
	}

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
	 * <code>date</code> and <code>format</code> for the current default
	 * <code>TimeZone</code> (see {@link TimeZone#getDefault()}).
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
	 * @see TimeZone
	 */
	public static Date createDateFromString(final String date,
			final String format) throws ParseException {
		return parseDate(date, format, TimeZone.getDefault().getID());
	}

	/**
	 * Formats the <code>date</code> according to the specified
	 * <code>format</code> in the current TimeZone.
	 * 
	 * @param date
	 *            the date to be formatted
	 * @param format
	 *            the format of the <code>date</code>
	 * 
	 * @return the formatted Date
	 * 
	 * @see Date
	 * @see DateFormat
	 * @see SimpleDateFormat
	 */
	public static String createStringFromDate(final Date date,
			final String format) {
		return formatDate(date, format, TimeZone.getDefault().getID());
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
	public static Date parseDate(final String date, final String format)
			throws ParseException {
		return parseDate(date, format, GENERAL_TIMEZONE);
	}

	/**
	 * Creates a <code>Date</code> instance based on the passed
	 * <code>date</code> and <code>format</code>.
	 * 
	 * @param date
	 *            the date to be parsed to a <code>Date</code> instance
	 * @param format
	 *            the format of the <code>date</code>
	 * @param timezone
	 *            the <code>TimeZone</code> used to format the date, i.e. the
	 *            date can be different in each TimeZone
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
	public static Date parseDate(final String date, final String format,
			final String timezone) throws ParseException {
		final DateFormat formatter;
		if (format == null) {
			formatter = new SimpleDateFormat();
		} else {
			formatter = new SimpleDateFormat(format);
		}

		// get the TimeZone
		final TimeZone tz = TimeZone.getTimeZone(timezone);
		formatter.setTimeZone(tz);

		final TimeZone curTz = TimeZone.getDefault();
		TimeZone.setDefault(tz);

		final Date parsedDate = formatter.parse(date);
		TimeZone.setDefault(curTz);

		return parsedDate;
	}

	/**
	 * Formats the <code>date</code> according to the specified
	 * <code>format</code>.
	 * 
	 * @param date
	 *            the date to be formatted
	 * @param format
	 *            the format of be applied to the <code>date</code>
	 * @param timezone
	 *            the <code>TimeZone</code> used to format the date, i.e. the
	 *            date can be different in each TimeZone
	 * 
	 * @return the formatted date
	 * 
	 * @throws IllegalArgumentException
	 *             if the Format cannot format the given object
	 * 
	 * @see Date
	 * @see DateFormat
	 * @see SimpleDateFormat
	 * @see TimeZone
	 */
	public static String formatDate(final Date date, final String format,
			final String timezone) {
		final DateFormat formatter = new SimpleDateFormat(format);

		// set the TimeZone
		final TimeZone tz = TimeZone.getTimeZone(timezone);
		formatter.setTimeZone(tz);

		// format it
		return formatter.format(date);
	}

	/**
	 * Formats the <code>date</code> according to the specified
	 * <code>format</code>. The formatting is done in {@link #GENERAL_TIMEZONE}.
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
		return formatDate(date, format, GENERAL_TIMEZONE);
	}

	/**
	 * Get the current <code>Date</code> of the system in the general defined
	 * <code>TimeZone</code>.
	 * 
	 * @return the current <code>Date</code> of the system in the general
	 *         defined <code>TimeZone</code>
	 */
	public static Date now() {
		return now(GENERAL_TIMEZONE);
	}

	/**
	 * Get the current <code>Date</code> of the system in the defined
	 * <code>TimeZone</code>.
	 * 
	 * @param timezone
	 *            the <code>TimeZone</code> to get the date in
	 * 
	 * @return the current <code>Date</code> of the system in the defined
	 *         <code>TimeZone</code>
	 */
	public static Date now(final String timezone) {
		final String format = "ddMMyyyy HH:mm:ss,SSS";
		final Date now = new Date();

		final String nowString = formatDate(now, format);
		try {
			return parseDate(nowString, format);
		} catch (final ParseException e) {
			throw new IllegalStateException("Unreachable code was reached.", e);
		}
	}
}
