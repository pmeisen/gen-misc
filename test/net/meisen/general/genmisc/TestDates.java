package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.Calendar;
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
	 * 
	 * @throws ParseException
	 *             if the re-parsing cannot be done
	 */
	@Test
	public void testNow() throws ParseException {

		// now should return the current time in the default TimeZone
		assertEquals((new Date()).getTime(),
				Dates.now(TimeZone.getDefault().getID()).getTime(), 500);

		// now in UTC
		final String utcNow = Dates.formatDate(Dates.now(),
				"dd.MM.yyyy HH:mm:ss");
		final String nowNow = Dates.formatDate(new Date(),
				"dd.MM.yyyy HH:mm:ss", TimeZone.getDefault().getID());

		final Date uctNowDate = Dates.parseDate(utcNow, "dd.MM.yyyy HH:mm:ss");
		final Date nowNowDate = Dates.parseDate(nowNow, "dd.MM.yyyy HH:mm:ss");

		assertEquals(uctNowDate.getTime(), nowNowDate.getTime(), 500);
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

	/**
	 * Tests the implementation of {@code Dates#isDate(String)}.
	 * 
	 * @throws ParseException
	 *             if the comparison date cannot be created
	 */
	@Test
	public void testIsDate() throws ParseException {

		// tests null
		assertNull(Dates.isDate(null));

		// tests German dates
		assertEquals(Dates.createDateFromString("20.01.1981", "dd.MM.yyyy"),
				Dates.isDate("20.01.1981"));
		assertEquals(Dates.createDateFromString("20.1.1981", "dd.MM.yyyy"),
				Dates.isDate("20.1.1981 00:00:00"));
		assertEquals(Dates.createDateFromString("10.05.2012 10:00:00",
				"dd.MM.yyyy HH:mm:ss"), Dates.isDate("10.5.2012 10:00:00"));
		assertEquals(Dates.createDateFromString("29.04.2010 00:00:01",
				"dd.MM.yyyy HH:mm:ss"), Dates.isDate("29.04.2010 00:00:01"));

		// tests US dates
		assertEquals(Dates.createDateFromString("22.08.2013", "dd.MM.yyyy"),
				Dates.isDate("22/08/2013"));
		assertEquals(Dates.createDateFromString("22.08.2013 12:22:16",
				"dd.MM.yyyy HH:mm:ss"), Dates.isDate("22/08/2013 12:22:16"));
	}

	/**
	 * Test the resetting of the timezone.
	 * 
	 * @throws ParseException
	 *             if the parsing failed
	 */
	@Test
	public void testResetOfTimezoneWithIsDate() throws ParseException {
		final TimeZone tz = TimeZone.getDefault();
		assertFalse(tz.getID().equals(Dates.GENERAL_TIMEZONE));
		assertNull(Dates.isDate("noonewill", Dates.GENERAL_TIMEZONE));
		assertEquals(TimeZone.getDefault().getID(), tz.getID());

	}

	/**
	 * Tests the mapping of a date using
	 * {@link Dates#mapToTimezone(Date, String, String)}.
	 * 
	 * @throws ParseException
	 *             if the date cannot be parsed
	 */
	@Test
	public void testMapping() throws ParseException {
		Date dateGer = Dates.parseDate("20.01.1981 08:07:00,000",
				Dates.FULL_FORMAT, "Europe/Berlin");
		Date dateUs = Dates.mapToTimezone(dateGer, "Europe/Berlin",
				"America/Los_Angeles");
		assertEquals(Dates.parseDate("20.01.1981 08:07:00,000",
				Dates.FULL_FORMAT, "America/Los_Angeles"), dateUs);

	}

	/**
	 * Tests the truncation of dates.
	 * 
	 * @throws ParseException
	 *             if a test format is invalid
	 */
	@Test
	public void testTruncDate() throws ParseException {
		final String timezone = TimeZone.getDefault().getID();
		final Date now = Dates.now(timezone);
		final Date nowTrunc = Dates.truncateDate(now);

		assertEquals(Dates.formatDate(now, "dd.MM.yyyy", timezone),
				Dates.formatDate(nowTrunc, "dd.MM.yyyy", timezone));

		Date chicDate = Dates.parseDate("10.02.2000 19:12:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago");

		// the UTC was truncated
		assertEquals(Dates.parseDate("10.02.2000 18:00:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"),
				Dates.truncDate(chicDate));
		assertEquals(Dates.parseDate("10.02.2000 00:00:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago"));

		chicDate = Dates.parseDate("02.11.2014 02:15:12,312",
				"dd.MM.yyyy HH:mm:ss,SSS", "America/Chicago");
		assertEquals(Dates.parseDate("01.11.2014 00:00:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.MONTH));
		assertEquals(Dates.parseDate("02.11.2014 00:00:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.DATE));
		assertEquals(Dates.parseDate("02.11.2014 02:00:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.HOUR));
		assertEquals(Dates.parseDate("02.11.2014 02:15:00",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.MINUTE));
		assertEquals(Dates.parseDate("02.11.2014 02:15:12",
				"dd.MM.yyyy HH:mm:ss", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.SECOND));
		assertEquals(Dates.parseDate("02.11.2014 02:15:12,312",
				"dd.MM.yyyy HH:mm:ss,SSS", "America/Chicago"), Dates.truncDate(
				chicDate, "America/Chicago", Calendar.MILLISECOND));
	}
}
