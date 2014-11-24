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
	 * The used full format to get all values of a date.
	 */
	public static final String FULL_FORMAT = "dd.MM.yyyy HH:mm:ss,SSS";

	/**
	 * Default patterns used to detect date formats within a string
	 * 
	 * @see #isDate(String)
	 */
	public static final String[] PATTERNS = new String[] {
			"dd.MM.yyyy HH:mm:ss", "dd.MM.yyyy", "yyyy-MM-dd HH:mm:ss",
			"yyyy-MM-dd", "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy",
			"yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd", null };

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
		return truncDate(date, TimeZone.getDefault().getID());
	}

	/**
	 * Truncates the date assuming it's in the {@link #GENERAL_TIMEZONE}.
	 * 
	 * @param date
	 *            the date to be truncated
	 * 
	 * @return the truncated date
	 */
	public static Date truncDate(final Date date) {
		return truncDate(date, GENERAL_TIMEZONE);
	}

	/**
	 * Truncates the date assuming it's in the {@link #GENERAL_TIMEZONE}.
	 * 
	 * @param date
	 *            the date to be truncated
	 * @param truncateLevel
	 *            one of:
	 *            <ul>
	 *            <li>{@link Calendar#YEAR}</li>
	 *            <li>{@link Calendar#MONTH}</li>
	 *            <li>{@link Calendar#DATE}</li>
	 *            <li>{@link Calendar#HOUR}</li>
	 *            <li>{@link Calendar#MINUTE}</li>
	 *            <li>{@link Calendar#SECOND}</li>
	 *            <li>{@link Calendar#MILLISECOND}</li>
	 *            </ul>
	 * 
	 * @return the truncated date
	 */
	public static Date truncDate(final Date date, final int truncateLevel) {
		return truncDate(date, GENERAL_TIMEZONE, truncateLevel);
	}

	/**
	 * Truncates the date assuming it's in the specified {@code timezone}.
	 * 
	 * @param date
	 *            the date to be truncated
	 * @param timezone
	 *            the timezone
	 * 
	 * @return the truncated date
	 */
	public static Date truncDate(final Date date, final String timezone) {
		return truncDate(date, timezone, Calendar.DATE);
	}

	/**
	 * Truncates the date assuming it's in the specified {@code timezone} to the
	 * specified {@code truncateLevel}.
	 * 
	 * @param date
	 *            the date to be truncated
	 * @param timezone
	 *            the timezone
	 * @param truncateLevel
	 *            one of:
	 *            <ul>
	 *            <li>{@link Calendar#YEAR}</li>
	 *            <li>{@link Calendar#MONTH}</li>
	 *            <li>{@link Calendar#DATE}</li>
	 *            <li>{@link Calendar#HOUR}</li>
	 *            <li>{@link Calendar#MINUTE}</li>
	 *            <li>{@link Calendar#SECOND}</li>
	 *            <li>{@link Calendar#MILLISECOND}</li>
	 *            </ul>
	 * 
	 * @return the truncated date
	 */
	public static Date truncDate(final Date date, final String timezone,
			final int truncateLevel) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeZone(TimeZone.getTimeZone(timezone));
		calendar.setTime(date);

		switch (truncateLevel) {
		case Calendar.YEAR:
			calendar.set(Calendar.MONTH, 0);
		case Calendar.MONTH:
			calendar.set(Calendar.DAY_OF_MONTH, 1);
		case Calendar.DATE:
			calendar.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR:
			calendar.set(Calendar.MINUTE, 0);
		case Calendar.MINUTE:
			calendar.set(Calendar.SECOND, 0);
		case Calendar.SECOND:
			calendar.set(Calendar.MILLISECOND, 0);
		}

		return calendar.getTime();
	}

	/**
	 * This function calculates the difference between two {@link Date} objects
	 * in minutes. The difference is calculated by {@code date1 - date2}.
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

		// get the default TimeZone
		final TimeZone curTz = TimeZone.getDefault();

		// create the formatter
		final DateFormat formatter;
		if (format == null) {
			formatter = new SimpleDateFormat();
		} else {
			formatter = new SimpleDateFormat(format);
		}

		// set the timezone to the one we need for parsing
		final TimeZone tz = TimeZone.getTimeZone(timezone);
		formatter.setTimeZone(tz);

		Date parsedDate;
		try {
			TimeZone.setDefault(tz);
			parsedDate = formatter.parse(date);
		} catch (final ParseException e) {
			throw e;
		} catch (final Exception e) {
			throw new ParseException("Parsing faile because of: "
					+ e.getMessage(), 0);
		} finally {

			// reset the timezone again
			TimeZone.setDefault(curTz);
		}

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
	 * <code>TimeZone</code>. That means, e.g. assume that it is currently
	 * {@code 21.11.2014 12:00} in {@code Europe/Berlin}, the method gets that
	 * time as UTC time, i.e. converting it back to {@code Europe/Berlin} would
	 * than be +1.
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
		return mapToTimezone(new Date(), TimeZone.getDefault().getID(),
				timezone);
	}

	/**
	 * The method is used to map the {@code date} of the {@code fromTZ} to the
	 * {@code toTZ}. The method assumes that the date currently represented is
	 * recorded wrongly, i.e. it meant to be in the {@code toTZ} but is
	 * represented in {@code fromTZ}. Therefore this method maps a date, so that
	 * the printing of the date in the {@code fromTZ} before is the same as the
	 * printing in the {@code toTZ} afterwards.
	 * 
	 * @param date
	 *            the date to be mapped
	 * @param fromTZ
	 *            the timezone to map the date from
	 * @param toTZ
	 *            the timezone to map the date to
	 * 
	 * @return the mapped date
	 */
	public static Date mapToTimezone(final Date date, final String fromTZ,
			final String toTZ) {
		final String tmp = Dates.formatDate(date, FULL_FORMAT, fromTZ);
		try {
			return Dates.parseDate(tmp, FULL_FORMAT, toTZ);
		} catch (final ParseException e) {
			return null;
		}
	}
}
