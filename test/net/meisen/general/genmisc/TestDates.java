package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

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
		Date date = new Date();

		// date the 0-time
		date = new Date(0);
		assertEquals("01", Dates.formatDate(date, "dd"));
		assertEquals("01", Dates.formatDate(date, "MM"));
		assertEquals("1970", Dates.formatDate(date, "yyyy"));
		assertEquals("01-1970-01", Dates.formatDate(date, "dd-yyyy-MM"));
		assertEquals("00:00:00", Dates.formatDate(date, "HH:mm:ss"));
		assertEquals("000", Dates.formatDate(date, "SSS"));

		// test one ms later
		date = new Date(1);
		assertEquals("00:00:00", Dates.formatDate(date, "HH:mm:ss"));
		assertEquals("001", Dates.formatDate(date, "SSS"));
		assertEquals("1", Dates.formatDate(date, "S"));

		// test 999 ms later
		date = new Date(999);
		assertEquals("00:00:00", Dates.formatDate(date, "HH:mm:ss"));
		assertEquals("00999", Dates.formatDate(date, "SSSSS"));
		assertEquals("999", Dates.formatDate(date, "S"));
	}

	/**
	 * Tests the implementation of {@link Dates#now()}.
	 */
	@Test
	public void testNow() {

		// now should return the current time in the default TimeZone
		assertEquals(new Date(), Dates.now(TimeZone.getDefault().getID()));

		// now in UTC
		final String utcNow = Dates.formatDate(Dates.now(),
				"dd.MM.yyyy HH:mm:ss");
		final String nowNow = Dates.formatDate(new Date(),
				"dd.MM.yyyy HH:mm:ss");

		assertEquals(nowNow, utcNow);
	}

	/**
	 * Tests the implementation of
	 * {@link Dates#createDateFromString(String, String)}.
	 * 
	 * @throws ParseException
	 *             if the test uses an invalid format
	 */
	@Test
	public void testCreateDateFromString() throws ParseException {
		Date date;

		date = Dates.createDateFromString("20.01.1981 08:07:42,666",
				"dd.MM.yyyy HH:mm:ss,SSS");

		assertEquals(Dates.createStringFromDate(date, "dd"), "20");
		assertEquals(Dates.createStringFromDate(date, "MM"), "01");
		assertEquals(Dates.createStringFromDate(date, "yyyy"), "1981");
		assertEquals(Dates.createStringFromDate(date, "HH:mm:ss"), "08:07:42");
		assertEquals(Dates.createStringFromDate(date, "SSS"), "666");
	}
}
