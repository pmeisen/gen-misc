package net.meisen.general.genmisc.raster.definition.impl;

import static org.junit.Assert.assertEquals;

import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterGranularity;

import org.junit.Test;


/**
 * Tests the <code>RasterBucket</code> implementation
 * 
 * @author pmeisen
 * 
 */
public class TestRasterBucket {

	/**
	 * Test the <code>RasterBucket.getBucketOfRelativeValue</code>
	 * implementation for <code>DataGranularity.MINUTES</code> and a bucket-size
	 * of 1.
	 * 
	 * @see RasterBucket#getBucketOfRelativeValue(Integer, IRasterGranularity)
	 */
	@Test
	public void testMinutes1() {
		final DateRasterGranularity m1 = new DateRasterGranularity(
				DateGranularity.MINUTES, 1);

		assertEquals(RasterBucket.getBucketOfRelativeValue(1, m1)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(5, m1)
				.getBucketNumber(), new Integer(5));
		assertEquals(RasterBucket.getBucketOfRelativeValue(0, m1)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(14400, m1)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-1, m1)
				.getBucketNumber(), new Integer(1439));
	}

	/**
	 * Test the <code>RasterBucket.getBucketOfRelativeValue</code>
	 * implementation for <code>DataGranularity.MINUTES</code> and a bucket-size
	 * of 30.
	 * 
	 * @see RasterBucket#getBucketOfRelativeValue(Integer, IRasterGranularity)
	 */
	@Test
	public void testMinutes30() {
		final DateRasterGranularity m30 = new DateRasterGranularity(
				DateGranularity.MINUTES, 30);

		assertEquals(RasterBucket.getBucketOfRelativeValue(1, m30)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(5, m30)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(41, m30)
				.getBucketNumber(), new Integer(30));
		assertEquals(RasterBucket.getBucketOfRelativeValue(0, m30)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-31, m30)
				.getBucketNumber(), new Integer(1380));
	}

	/**
	 * Test the <code>RasterBucket.getBucketOfRelativeValue</code>
	 * implementation for <code>DataGranularity.DAY</code> and a bucket-size of
	 * 2.
	 * 
	 * @see RasterBucket#getBucketOfRelativeValue(Integer, IRasterGranularity)
	 */
	@Test
	public void testDays2() {
		final DateRasterGranularity d2 = new DateRasterGranularity(
				DateGranularity.DAYS, 2);

		assertEquals(RasterBucket.getBucketOfRelativeValue(1, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(5, d2)
				.getBucketNumber(), new Integer(5));
		assertEquals(RasterBucket.getBucketOfRelativeValue(21, d2)
				.getBucketNumber(), new Integer(7));
		assertEquals(RasterBucket.getBucketOfRelativeValue(22, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(0, d2)
				.getBucketNumber(), new Integer(7));
		assertEquals(RasterBucket.getBucketOfRelativeValue(8, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(9, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-8, d2)
				.getBucketNumber(), new Integer(5));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-9, d2)
				.getBucketNumber(), new Integer(5));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-5, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-4, d2)
				.getBucketNumber(), new Integer(3));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-3, d2)
				.getBucketNumber(), new Integer(3));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-7, d2)
				.getBucketNumber(), new Integer(7));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-12, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-13, d2)
				.getBucketNumber(), new Integer(1));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-6, d2)
				.getBucketNumber(), new Integer(1));
	}

	/**
	 * Test the <code>RasterBucket.getBucketOfRelativeValue</code>
	 * implementation for <code>DataGranularity.MONTHS</code> and a bucket-size
	 * of 2.
	 * 
	 * @see RasterBucket#getBucketOfRelativeValue(Integer, IRasterGranularity)
	 */
	@Test
	public void testMonths2() {
		final DateRasterGranularity m2 = new DateRasterGranularity(
				DateGranularity.MONTHS, 2);

		assertEquals(RasterBucket.getBucketOfRelativeValue(0, m2)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(1, m2)
				.getBucketNumber(), new Integer(0));
		assertEquals(RasterBucket.getBucketOfRelativeValue(2, m2)
				.getBucketNumber(), new Integer(2));
		assertEquals(RasterBucket.getBucketOfRelativeValue(3, m2)
				.getBucketNumber(), new Integer(2));
		assertEquals(RasterBucket.getBucketOfRelativeValue(4, m2)
				.getBucketNumber(), new Integer(4));
		assertEquals(RasterBucket.getBucketOfRelativeValue(5, m2)
				.getBucketNumber(), new Integer(4));
		assertEquals(RasterBucket.getBucketOfRelativeValue(6, m2)
				.getBucketNumber(), new Integer(6));
		assertEquals(RasterBucket.getBucketOfRelativeValue(7, m2)
				.getBucketNumber(), new Integer(6));
		assertEquals(RasterBucket.getBucketOfRelativeValue(8, m2)
				.getBucketNumber(), new Integer(8));
		assertEquals(RasterBucket.getBucketOfRelativeValue(9, m2)
				.getBucketNumber(), new Integer(8));
		assertEquals(RasterBucket.getBucketOfRelativeValue(10, m2)
				.getBucketNumber(), new Integer(10));
		assertEquals(RasterBucket.getBucketOfRelativeValue(11, m2)
				.getBucketNumber(), new Integer(10));
		assertEquals(RasterBucket.getBucketOfRelativeValue(-1, m2)
				.getBucketNumber(), new Integer(10));
	}
}
