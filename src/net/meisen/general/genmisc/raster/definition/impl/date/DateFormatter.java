package net.meisen.general.genmisc.raster.definition.impl.date;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.meisen.general.genmisc.raster.function.impl.BucketLabel.IFormatter;


/**
 * An implementation for <code>Date</code> raster of the <code>IFormatter</code>
 * 
 * @author pmeisen
 */
public class DateFormatter implements IFormatter {
	/**
	 * the specified default date format
	 */
	protected final static String defaultDateFormat = "dd.MM.yyyy HH:mm:ss";
	/**
	 * the specified default interval format
	 */
	protected final static String defaultIntervalFormat = "%s - %s";

	private String startFormat;
	private String endFormat;
	private String intervalFormat;

	/**
	 * Default constructor which uses the default formats for the
	 * <code>startFormat</code>, <code>endFormat</code> and
	 * <code>intervalFormat</code>
	 * 
	 * @see #getDefaultDateFormat()
	 * @see #getDefaultIntervalFormat()
	 */
	public DateFormatter() {
		this(null, null, null);
	}

	/**
	 * Constructor to specify a date format which is used as
	 * <code>startFormat</code> and <code>endFormat</code>, and also an
	 * <code>intervalFormat</code>
	 * 
	 * @param dateFormat
	 *            the format to be used for <code>startFormat</code> and
	 *            <code>endFormat</code>, if <code>null</code> the default
	 *            format will be used
	 * @param intervalFormat
	 *            the interval format to be used, if <code>null</code> the
	 *            default format will be used
	 * 
	 * @see #getDefaultDateFormat()
	 * @see #getDefaultIntervalFormat()
	 */
	public DateFormatter(final String dateFormat, final String intervalFormat) {
		this(dateFormat, dateFormat, intervalFormat);
	}

	/**
	 * Constructor to specify the <code>startFormat</code>,
	 * <code>endFormat</code> and <code>intervalFormat</code>
	 * 
	 * @param startFormat
	 *            the start format to be used, if <code>null</code> the default
	 *            format will be used
	 * @param endFormat
	 *            the end format to be used, if <code>null</code> the default
	 *            format will be used
	 * @param intervalFormat
	 *            the interval format to be used, if <code>null</code> the
	 *            default format will be used
	 * 
	 * @see #getDefaultDateFormat()
	 * @see #getDefaultIntervalFormat()
	 */
	public DateFormatter(final String startFormat, final String endFormat,
			final String intervalFormat) {

		// set the defined formats
		this.startFormat = startFormat == null ? getDefaultDateFormat()
				: startFormat;
		this.endFormat = endFormat == null ? getDefaultDateFormat() : endFormat;
		this.intervalFormat = intervalFormat == null ? getDefaultIntervalFormat()
				: intervalFormat;
	}

	/**
	 * The default format used for the <code>startFormat</code> and
	 * <code>endFormat</code>
	 * 
	 * @return the default format used for the <code>startFormat</code> and
	 *         <code>endFormat</code>
	 */
	protected String getDefaultDateFormat() {
		return defaultDateFormat;
	}

	/**
	 * The default format used for the <code>intervalFormat</code>
	 * 
	 * @return the default format used for the <code>intervalFormat</code>
	 */
	protected String getDefaultIntervalFormat() {
		return defaultIntervalFormat;
	}

	@Override
	public String format(final Object start, final Object end) {
		final String startString = format(start, startFormat);
		final String endString = format(end, endFormat);

		return String.format(intervalFormat, startString, endString);
	}

	/**
	 * Helper method to format a <code>Date</code> to the specified
	 * <code>format</code>
	 * 
	 * @param date
	 *            the <code>Date</code> to be formatted
	 * @param format
	 *            the format to be used
	 * 
	 * @return the formatted date, <code>null</code> if one of the passed values
	 *         was <code>null</code>
	 */
	protected String format(final Object date, final String format) {
		if (format == null) {
			return null;
		} else if (date == null || date instanceof Date == false) {
			return null;
		}

		// format the date and return
		final SimpleDateFormat formatter = new SimpleDateFormat(format);
		return formatter.format(date);
	}
}
