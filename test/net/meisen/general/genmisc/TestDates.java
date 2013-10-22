package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.meisen.general.genmisc.types.Dates;

import org.junit.Test;

/**
 * Tests the implementation of the <code>Dates</code> class.
 * 
 * @author pmeisen
 * 
 */
public class TestDates {

	/**
	 * Tests the {@link Dates#formatDate(Date, String)} implementation.
	 */
	@Test
	public void testFormatDate() {
		Date date;

		// date the 0-time
		date = new Date(0);
		assertEquals("01", Dates.formatDate(date, "dd"));
		assertEquals("01", Dates.formatDate(date, "MM"));
		assertEquals("1970", Dates.formatDate(date, "yyyy"));
		assertEquals("01-1970-01", Dates.formatDate(date, "dd-yyyy-MM"));
		assertEquals("01:00:00", Dates.formatDate(date, "hh:mm:ss"));
		assertEquals("000", Dates.formatDate(date, "SSS"));

		// test one ms later
		date = new Date(1);
		assertEquals("01:00:00", Dates.formatDate(date, "hh:mm:ss"));
		assertEquals("001", Dates.formatDate(date, "SSS"));
		assertEquals("1", Dates.formatDate(date, "S"));

		// test 999 ms later
		date = new Date(999);
		assertEquals("01:00:00", Dates.formatDate(date, "hh:mm:ss"));
		assertEquals("00999", Dates.formatDate(date, "SSSSS"));
		assertEquals("999", Dates.formatDate(date, "S"));
	}
}
