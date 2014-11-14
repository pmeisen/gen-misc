package net.meisen.general.genmisc.raster.utilities;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.configuration.impl.BaseRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRaster;
import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.raster.definition.impl.BaseRaster;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterLogic;


/**
 * Utilities used when testing <code>Raster</code> functionality
 * 
 * @author pmeisen
 * 
 */
public class DateRasterUtilities {

	/**
	 * Helper function when testing the bucket calculation of a
	 * <code>Raster</code>.
	 * 
	 * @param date
	 *            the <code>Date</code>
	 * @param granularity
	 *            granularity of the raster
	 * @param size
	 *            the size of the bucket
	 * @param rel_Value
	 *            the value that should be returned
	 * @param rel_BucketNumber
	 *            the bucket number that should be checked
	 * @param rel_EndOfBucket
	 *            the end of the bucker number
	 * @param rel_NextBucketNumber
	 *            the next bucket number expected
	 * @param rel_NextEndOfBucket
	 *            the next end of bucket
	 */
	public static void checkBucket(final String date,
			final DateGranularity granularity, final Integer size,
			final Integer rel_Value, final Integer rel_BucketNumber,
			final Integer rel_EndOfBucket, final Integer rel_NextBucketNumber,
			final Integer rel_NextEndOfBucket) {

		// create the raster
		final IRasterLogic<Date> logic = createDateRasterLogic(granularity,
				size);
		final IRasterGranularity<Date> dateGranularity = logic.getGranularity();

		// calculate the relative value and check it
		final Integer relValue = logic.getRelativeValue(GeneralUtilities
				.getDate(date));
		assertEquals(relValue, rel_Value);

		// get the relative bucket and check it
		final RasterBucket bucket = RasterBucket.getBucketOfRelativeValue(
				relValue, dateGranularity);
		assertEquals(bucket.getBucketNumber(), rel_BucketNumber);
		assertEquals(bucket.getEndOfBucket(dateGranularity), rel_EndOfBucket);

		// get the next bucket and check it
		final RasterBucket nextBucket = bucket.getNextBucket(dateGranularity);
		assertEquals(nextBucket.getBucketNumber(), rel_NextBucketNumber);
		assertEquals(nextBucket.getEndOfBucket(dateGranularity),
				rel_NextEndOfBucket);
	}

	/**
	 * Helper function to create a <code>RasterLogic</code>
	 * 
	 * @param granularity
	 *            the <code>RasterGranularity</code> to use with the
	 *            <code>RasterLogic</code>
	 * @param bucketSize
	 *            the bucket size to be used
	 * @return the created <code>RasterLogic</code>
	 */
	public static IRasterLogic<Date> createDateRasterLogic(
			final DateGranularity granularity, final Integer bucketSize) {

		final DateRasterGranularity dateRasterGranularity = new DateRasterGranularity(
				granularity, bucketSize);
		final DateRasterLogic dateRasterLogic = new DateRasterLogic(
				dateRasterGranularity);

		return dateRasterLogic;
	}

	/**
	 * Helper function to create a <code>RasterConfiguration</code>
	 * 
	 * @param granularity
	 *            the <code>RasterGranularity</code> to use with the
	 *            <code>RasterConfiguration</code>
	 * @param bucketSize
	 *            the bucket size to be used
	 * @param locale
	 *            the <code>Locale</code> to be set within the
	 *            <code>RasterConfiguration</code>, can be <code>null</code>
	 * @return the created <code>RasterConfiguration</code>
	 */
	public static IRasterConfiguration<Date> createDateRasterConfiguration(
			final DateGranularity granularity, final Integer bucketSize,
			final Locale locale) {

		final IRasterLogic<Date> dateRasterLogic = createDateRasterLogic(
				granularity, bucketSize);
		final BaseRasterConfiguration<Date> configuration = new BaseRasterConfiguration<Date>(
				dateRasterLogic);
		configuration.setLocale(locale);

		return configuration;
	}

	/**
	 * Helper function to create a <code>Raster</code>
	 * 
	 * @param granularity
	 *            the <code>RasterGranularity</code> to use with the
	 *            <code>Raster</code>
	 * @param bucketSize
	 *            the bucket size to be used
	 * @param locale
	 *            the <code>Locale</code> to be set within the
	 *            <code>Raster</code>, can be <code>null</code>
	 * @param models
	 *            the <code>RasterModels</code> to add to the
	 *            <code>Raster</code>
	 * @return the created <code>Raster</code>
	 */
	public static IRaster<Date> createDateRaster(
			final DateGranularity granularity, final Integer bucketSize,
			final Locale locale, final Map<String, IRasterModel> models) {
		final IRasterConfiguration<Date> configuration = createDateRasterConfiguration(
				granularity, bucketSize, locale);

		// add some models to the configuration
		final BaseRasterConfiguration<Date> dateConfiguration = (BaseRasterConfiguration<Date>) configuration;

		// add the models
		if (models != null) {
			for (final Entry<String, IRasterModel> entry : models.entrySet()) {
				dateConfiguration.addModel(entry.getKey(), entry.getValue());
			}
		}

		// create the raster
		final IRaster<Date> raster = new BaseRaster<Date>(configuration);

		return raster;
	}
}
