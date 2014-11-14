package net.meisen.general.genmisc.raster.definition.impl.date;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;

import org.junit.Test;


/**
 * Tests the implementation <code>DateFormatter</code> of the
 * <code>IFormatter</code> interface
 * 
 * @author pmeisen
 * 
 */
public class TestDateFormatter {

	/**
	 * Tests the default formatting
	 */
	@Test
	public void testDefaultFormatter() {
		final Date start = GeneralUtilities.getDate("01.01.2012 00:00:00");
		final Date end = GeneralUtilities.getDate("01.01.2012 03:00:00");

		final DateFormatter formatter = new DateFormatter();
		final String res = formatter.format(start, end);
		assertEquals(res, "01.01.2012 00:00:00 - 01.01.2012 03:00:00");
	}

	/**
	 * Tests the formatting using one date format
	 */
	@Test
	public void testFormatterWithDateFormat() {
		final Date start = GeneralUtilities.getDate("01.01.2012 00:00:00");
		final Date end = GeneralUtilities.getDate("01.01.2012 03:00:00");

		final DateFormatter formatter = new DateFormatter("dd.MM",
				DateFormatter.defaultIntervalFormat);
		final String res = formatter.format(start, end);
		assertEquals(res, "01.01 - 01.01");
	}
	
	/**
	 * Tests the formatting using one date format and a interval format
	 */
	@Test
	public void testFormatterWithDateAndIntervalFormat() {
		final Date start = GeneralUtilities.getDate("01.01.2012 00:00:00");
		final Date end = GeneralUtilities.getDate("01.01.2012 03:00:00");

		final DateFormatter formatter = new DateFormatter("dd.MM HH",
				"%s,%s");
		final String res = formatter.format(start, end);
		assertEquals(res, "01.01 00,01.01 03");
	}
}
