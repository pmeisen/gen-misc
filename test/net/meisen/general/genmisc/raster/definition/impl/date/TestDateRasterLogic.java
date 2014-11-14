package net.meisen.general.genmisc.raster.definition.impl.date;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.raster.utilities.DateRasterUtilities;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;

import org.junit.Test;

/**
 * This test tests the <code>DateRasterLogic</code>
 * 
 * @author pmeisen
 * 
 */
public class TestDateRasterLogic {

	/**
	 * Checks the calculations to determine the <code>RasterBucket</code> of a
	 * <code>RasterDate</code>
	 */
	@Test
	public void testRasterBucket() {

		// define the granularities to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;
		final DateGranularity w = DateGranularity.WEEKS;

		// test minutes
		DateRasterUtilities.checkBucket("15.02.2010 23:59:00", m, 1, 1439,
				1439, 1439, 0, 0);
		DateRasterUtilities.checkBucket("15.02.2010 00:00:00", m, 1, 0, 0, 0,
				1, 1);
		DateRasterUtilities.checkBucket("15.02.2010 02:01:00", m, 1, 121, 121,
				121, 122, 122);

		DateRasterUtilities.checkBucket("16.02.2010 23:59:00", m, 5, 1439,
				1435, 1439, 0, 4);
		DateRasterUtilities.checkBucket("16.02.2010 00:00:00", m, 5, 0, 0, 4,
				5, 9);
		DateRasterUtilities.checkBucket("16.02.2010 02:01:00", m, 5, 121, 120,
				124, 125, 129);

		// test days
		DateRasterUtilities.checkBucket("17.02.2010 23:50:00", d, 2, 4, 3, 4,
				5, 6);
		DateRasterUtilities.checkBucket("18.02.2010 15:23:00", d, 2, 5, 5, 6,
				7, 7);
		DateRasterUtilities.checkBucket("19.02.2010 11:12:00", d, 2, 6, 5, 6,
				7, 7);
		DateRasterUtilities.checkBucket("20.02.2010 01:42:00", d, 2, 7, 7, 7,
				1, 2);
		DateRasterUtilities.checkBucket("21.02.2010 03:12:00", d, 2, 1, 1, 2,
				3, 4);
		DateRasterUtilities.checkBucket("22.02.2010 03:10:00", d, 2, 2, 1, 2,
				3, 4);
		DateRasterUtilities.checkBucket("23.02.2010 16:09:00", d, 2, 3, 3, 4,
				5, 6);

		DateRasterUtilities.checkBucket("17.02.2010 23:59:00", d, 1, 4, 4, 4,
				5, 5);
		DateRasterUtilities.checkBucket("18.02.2010 00:00:00", d, 1, 5, 5, 5,
				6, 6);
		DateRasterUtilities.checkBucket("19.02.2010 02:01:00", d, 1, 6, 6, 6,
				7, 7);

		DateRasterUtilities.checkBucket("15.02.2010 23:59:00", d, 3, 2, 1, 3,
				4, 6);
		DateRasterUtilities.checkBucket("18.02.2010 00:00:00", d, 3, 5, 4, 6,
				7, 7);
		DateRasterUtilities.checkBucket("20.02.2010 02:01:00", d, 3, 7, 7, 7,
				1, 3);

		// test weeks
		DateRasterUtilities.checkBucket("01.01.2010 00:00:00", w, 4, 1, 1, 4,
				5, 8);
		DateRasterUtilities.checkBucket("26.02.2010 23:59:00", w, 4, 9, 9, 12,
				13, 16);
		DateRasterUtilities.checkBucket("01.02.2010 00:00:00", w, 4, 6, 5, 8,
				9, 12);
		DateRasterUtilities.checkBucket("01.03.2010 02:01:00", w, 4, 10, 9, 12,
				13, 16);
	}

	/**
	 * Test the implementation of the calculation of the bucket number for a
	 * specific <code>Date</code>
	 * 
	 * @see DateRasterLogic#getBucket(Date)
	 */
	@Test
	public void testGetBucket() {

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;

		// create the logic
		IRasterLogic<Date> logic;
		int value;

		// do the tests
		logic = DateRasterUtilities.createDateRasterLogic(m, 1);
		value = logic
				.getBucket(GeneralUtilities.getDate("15.02.2010 13:10:00"))
				.getBucketNumber();
		assertEquals(value, 790);
		value = logic
				.getBucket(GeneralUtilities.getDate("20.01.1981 08:07:00"))
				.getBucketNumber();
		assertEquals(value, 487);

		logic = DateRasterUtilities.createDateRasterLogic(m, 10);
		value = logic
				.getBucket(GeneralUtilities.getDate("15.02.2010 13:09:59"))
				.getBucketNumber();
		assertEquals(value, 780);
		value = logic
				.getBucket(GeneralUtilities.getDate("20.01.1981 08:07:00"))
				.getBucketNumber();
		assertEquals(value, 480);

		logic = DateRasterUtilities.createDateRasterLogic(d, 2);
		value = logic
				.getBucket(GeneralUtilities.getDate("15.02.2010 13:13:55"))
				.getBucketNumber();
		assertEquals(value, 1);
		value = logic
				.getBucket(GeneralUtilities.getDate("16.02.2010 13:13:55"))
				.getBucketNumber();
		assertEquals(value, 3);
		value = logic
				.getBucket(GeneralUtilities.getDate("18.02.2010 13:10:00"))
				.getBucketNumber();
		assertEquals(value, 5);
		value = logic
				.getBucket(GeneralUtilities.getDate("20.01.1981 08:07:00"))
				.getBucketNumber();
		assertEquals(value, 3);
	}

	/**
	 * Test the implementation of the calculation of a relative value for a
	 * specific <code>Date</code>
	 * 
	 * @see DateRasterLogic#getRelativeValue(Date)
	 */
	@Test
	public void testGetRelativeValue() {

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;
		final DateGranularity w = DateGranularity.WEEKS;

		// create the logic
		IRasterLogic<Date> logic;
		int value;

		// do the tests
		logic = DateRasterUtilities.createDateRasterLogic(m, 1);
		value = logic.getRelativeValue(GeneralUtilities
				.getDate("15.02.2010 13:13:00"));
		assertEquals(value, 793);

		logic = DateRasterUtilities.createDateRasterLogic(m, 10);
		value = logic.getRelativeValue(GeneralUtilities
				.getDate("15.02.2010 13:13:00"));
		assertEquals(value, 793);

		logic = DateRasterUtilities.createDateRasterLogic(w, 7);
		value = logic.getRelativeValue(GeneralUtilities
				.getDate("17.02.2010 13:13:00"));
		assertEquals(value, 8);

		logic = DateRasterUtilities.createDateRasterLogic(d, 2);
		value = logic.getRelativeValue(GeneralUtilities
				.getDate("18.02.2010 13:13:00"));
		assertEquals(value, 5);
	}

	/**
	 * Test the implementation of the calculation of the previous absolute
	 * bucket value for a specific <code>Date</code>
	 * 
	 * @see DateRasterLogic#getAbsoluteBucketStart(Date)
	 */
	@Test
	public void testGetAbsolutBucketStart() {

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;

		IRasterLogic<Date> logic;
		Date date;

		// do the tests
		logic = DateRasterUtilities.createDateRasterLogic(m, 1);
		date = logic.getAbsoluteBucketStart(GeneralUtilities
				.getDate("15.02.2010 13:13:23"));
		assertEquals(date, GeneralUtilities.getDate("15.02.2010 13:13:00"));

		logic = DateRasterUtilities.createDateRasterLogic(m, 10);
		date = logic.getAbsoluteBucketStart(GeneralUtilities
				.getDate("15.02.2010 13:13:55"));
		assertEquals(date, GeneralUtilities.getDate("15.02.2010 13:10:00"));

		logic = DateRasterUtilities.createDateRasterLogic(d, 2);
		date = logic.getAbsoluteBucketStart(GeneralUtilities
				.getDate("16.02.2010 13:13:55"));
		assertEquals(date, GeneralUtilities.getDate("16.02.2010 00:00:00"));
	}

	/**
	 * Test the implementation of the calculation for the next absolute bucket
	 * value for a specific <code>Date</code>
	 * 
	 * @see DateRasterLogic#getAbsoluteBucketEnd(Date)
	 */
	@Test
	public void testGetAbsolutBucketEnd() {

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;

		// create the logic
		IRasterLogic<Date> logic;
		Date date;

		// do the tests
		logic = DateRasterUtilities.createDateRasterLogic(m, 1);
		date = logic.getAbsoluteBucketEnd(GeneralUtilities
				.getDate("15.02.2010 13:13:43"));
		assertEquals(date, GeneralUtilities.getDate("15.02.2010 13:14:00"));

		logic = DateRasterUtilities.createDateRasterLogic(m, 10);
		date = logic.getAbsoluteBucketEnd(GeneralUtilities
				.getDate("15.02.2010 13:13:55"));
		assertEquals(date, GeneralUtilities.getDate("15.02.2010 13:20:00"));

		logic = DateRasterUtilities.createDateRasterLogic(d, 2);
		date = logic.getAbsoluteBucketEnd(GeneralUtilities
				.getDate("16.02.2010 13:13:55"));
		assertEquals(date, GeneralUtilities.getDate("18.02.2010 00:00:00"));
	}

	/**
	 * Test the comparing capabilities of the <code>RasterLogic</code>
	 * 
	 * @see DateRasterLogic#compare(Date, Date)
	 */
	@Test
	public void testCompare() {

		// define the granularity to use
		final DateGranularity w = DateGranularity.WEEKS;

		// create the logic
		IRasterLogic<Date> logic;
		int value;

		logic = DateRasterUtilities.createDateRasterLogic(w, 7);
		value = logic.compare(GeneralUtilities.getDate("17.12.2010 13:10:00"),
				GeneralUtilities.getDate("17.12.2010 13:10:00"));
		assertEquals(value, 0);
		value = logic.compare(GeneralUtilities.getDate("17.12.2010 13:10:00"),
				GeneralUtilities.getDate("18.12.2010 13:10:00"));
		assertEquals(value, -1);
		value = logic.compare(GeneralUtilities.getDate("17.12.2009 13:10:00"),
				GeneralUtilities.getDate("18.12.2010 13:10:00"));
		assertEquals(value, -1);
		value = logic.compare(GeneralUtilities.getDate("17.12.2020 13:10:00"),
				GeneralUtilities.getDate("17.12.2010 13:10:00"));
		assertEquals(value, 1);
	}

	/**
	 * Tests the difference calculation
	 * 
	 * @see DateRasterLogic#getDifference(Date, Date)
	 */
	@Test
	public void testDifference() {
		// define the granularity to use
		final DateGranularity w = DateGranularity.WEEKS;

		// create the logic
		IRasterLogic<Date> logic;
		int value;

		logic = DateRasterUtilities.createDateRasterLogic(w, 7);
		value = logic.getDifference(
				GeneralUtilities.getDate("17.12.2010 13:10:00"),
				GeneralUtilities.getDate("17.12.2010 13:10:00"));
		assertEquals(value, 0);
		value = logic.getDifference(
				GeneralUtilities.getDate("17.12.2010 13:20:00"),
				GeneralUtilities.getDate("17.12.2010 13:10:00"));
		assertEquals(value, 10);
		value = logic.getDifference(
				GeneralUtilities.getDate("17.12.2010 13:20:00"),
				GeneralUtilities.getDate("17.12.2010 13:10:55"));
		assertEquals(value, 10);
		value = logic.getDifference(
				GeneralUtilities.getDate("17.12.2010 13:19:59"),
				GeneralUtilities.getDate("17.12.2010 13:10:01"));
		assertEquals(value, 9);
	}

	/**
	 * Tests the implementation of
	 * <code>RasterBucket.getBucketStart(RasterBucket)</code>
	 */
	@Test
	public void testBucketStart() {
		final SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss");
		final Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;
		final DateGranularity w = DateGranularity.WEEKS;
		final DateGranularity mo = DateGranularity.MONTHS;

		// create the logic
		IRasterLogic<Date> logic;
		RasterBucket bucket;
		Date value;

		// first use MINUTES
		logic = DateRasterUtilities.createDateRasterLogic(m, 5);

		bucket = new RasterBucket(0);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "00:00:00");

		bucket = new RasterBucket(4);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "00:00:00");

		bucket = new RasterBucket(5);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "00:05:00");

		bucket = new RasterBucket(31);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "00:30:00");

		bucket = new RasterBucket(1440);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "00:00:00");

		bucket = new RasterBucket(-1);
		value = logic.getBucketStart(bucket);
		assertEquals(fm.format(value), "23:55:00");

		// now use DAYS
		logic = DateRasterUtilities.createDateRasterLogic(d, 2);

		bucket = new RasterBucket(1);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(2);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(3);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(4);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(7);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 7);

		bucket = new RasterBucket(8);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(9);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(-1);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 5);

		bucket = new RasterBucket(0);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 7);

		bucket = new RasterBucket(-1);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 5);

		// now use WEEKS
		logic = DateRasterUtilities.createDateRasterLogic(w, 2);

		bucket = new RasterBucket(1);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		bucket = new RasterBucket(2);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		bucket = new RasterBucket(53);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		bucket = new RasterBucket(52);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 51);

		bucket = new RasterBucket(51);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 51);

		bucket = new RasterBucket(0);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 51);

		// now use MONTHS
		logic = DateRasterUtilities.createDateRasterLogic(mo, 4);

		bucket = new RasterBucket(0);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 0);

		bucket = new RasterBucket(3);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 0);

		bucket = new RasterBucket(4);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 4);

		bucket = new RasterBucket(11);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 8);

		bucket = new RasterBucket(13);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 0);

		bucket = new RasterBucket(-1);
		value = logic.getBucketStart(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 8);
	}

	/**
	 * Tests the implementation of
	 * <code>RasterBucket.getBucketEnd(RasterBucket)</code>
	 */
	@Test
	public void testBucketEnd() {
		final SimpleDateFormat fm = new SimpleDateFormat("HH:mm:ss");
		final Calendar calendar = Calendar.getInstance();
		calendar.setFirstDayOfWeek(Calendar.SUNDAY);

		// define the granularity to use
		final DateGranularity m = DateGranularity.MINUTES;
		final DateGranularity d = DateGranularity.DAYS;
		final DateGranularity w = DateGranularity.WEEKS;
		final DateGranularity mo = DateGranularity.MONTHS;

		// create the logic
		IRasterLogic<Date> logic;
		RasterBucket bucket;
		Date value;

		// first use MINUTES
		logic = DateRasterUtilities.createDateRasterLogic(m, 5);

		bucket = new RasterBucket(0);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:05:00");

		bucket = new RasterBucket(4);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:05:00");

		bucket = new RasterBucket(5);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:10:00");

		bucket = new RasterBucket(31);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:35:00");

		bucket = new RasterBucket(1440);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:05:00");

		bucket = new RasterBucket(-1);
		value = logic.getBucketEnd(bucket);
		assertEquals(fm.format(value), "00:00:00");

		// now use DAYS
		logic = DateRasterUtilities.createDateRasterLogic(d, 2);

		bucket = new RasterBucket(1);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(2);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(3);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 5);

		bucket = new RasterBucket(4);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 5);

		bucket = new RasterBucket(7);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(8);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(9);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 3);

		bucket = new RasterBucket(-1);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 7);

		bucket = new RasterBucket(0);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 1);

		bucket = new RasterBucket(-1);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.DAY_OF_WEEK), 7);

		// now use WEEKS
		logic = DateRasterUtilities.createDateRasterLogic(w, 2);

		bucket = new RasterBucket(1);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 3);

		bucket = new RasterBucket(2);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 3);

		bucket = new RasterBucket(53);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 3);

		bucket = new RasterBucket(52);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		bucket = new RasterBucket(51);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		bucket = new RasterBucket(0);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.WEEK_OF_YEAR), 1);

		// now use MONTHS
		logic = DateRasterUtilities.createDateRasterLogic(mo, 4);

		bucket = new RasterBucket(0);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 4);

		bucket = new RasterBucket(3);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 4);

		bucket = new RasterBucket(4);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 8);

		bucket = new RasterBucket(11);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 0);

		bucket = new RasterBucket(13);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 4);

		bucket = new RasterBucket(-1);
		value = logic.getBucketEnd(bucket);
		calendar.setTime(value);
		assertEquals(calendar.get(Calendar.MONTH), 0);
	}
}
