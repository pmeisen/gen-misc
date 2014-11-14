package net.meisen.general.genmisc.raster.definition.impl.date;

import java.util.Calendar;
import java.util.Date;

import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterLogic;
import net.meisen.general.genmisc.types.Dates;


/**
 * The <code>Raster</code> used for {@link Date} data
 * 
 * @author pmeisen
 */
public class DateRasterLogic extends BaseRasterLogic<Date> {
	private DateGranularity enumGranularity;

	/**
	 * @param granularity
	 *            the <code>RasterGranularity</code> to be used for the
	 *            <code>RasterLogic</code>
	 */
	public DateRasterLogic(final IRasterGranularity<Date> granularity) {
		super(granularity);

		if (granularity instanceof DateRasterGranularity) {

			// use the granularity specified by the configuration
			final DateRasterGranularity dateGranularity = (DateRasterGranularity) granularity;
			this.enumGranularity = dateGranularity.getEnum();
		} else {
			this.enumGranularity = DateGranularity.DAYS;
		}
	}

	@Override
	public Integer getRelativeValue(final Date date) {
		final Integer value;

		if (DateGranularity.MINUTES.equals(enumGranularity)) {
			final Date truncatedDate = Dates.truncateDate(date);
			final Long diffMinutes = (long) Math
					.floor((date.getTime() - truncatedDate.getTime()) / 1000 / 60);

			value = diffMinutes.intValue();
		} else if (DateGranularity.DAYS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.SUNDAY);
			cal.setTime(date);

			value = cal.get(Calendar.DAY_OF_WEEK);
		} else if (DateGranularity.WEEKS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			value = cal.get(Calendar.WEEK_OF_YEAR);
		} else if (DateGranularity.MONTHS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();
			cal.setTime(date);

			value = cal.get(Calendar.MONTH);
		} else {
			value = null;
		}

		return value;
	}

	@Override
	public Date getBucketStart(final RasterBucket bucket) {
		return getBucketInfo(bucket, true);
	}

	@Override
	public Date getBucketEnd(final RasterBucket bucket) {
		return getBucketInfo(bucket, false);
	}

	/**
	 * Gets the information for the specified <code>RasterBucket</code>
	 * 
	 * @param bucket
	 *            the <code>RasterBucket</code> to get the information for
	 * @param start
	 *            if <code>true</code> the start of the
	 *            <code>RasterBucket</code> will be retrieved, otherwise
	 *            <code>false</code>
	 * 
	 * @return the start or the end <code>Date</code> of the
	 *         <code>RasterBucket</code>, depending on the <code>start</code>
	 *         parameter
	 */
	protected Date getBucketInfo(final RasterBucket bucket, final boolean start) {

		// get the point to start from
		final Integer bucketNumber;
		if (start) {
			bucketNumber = bucket.getBucketNumber();
		} else {

			// we have to get the start-bucket, cause it could be a relative
			// bucket and we cannot just add the bucket-size to that, to get the
			// end
			final RasterBucket sBucket = RasterBucket.getBucketOfRelativeValue(
					bucket.getBucketNumber(), granularity);
			bucketNumber = sBucket.getBucketNumber()
					+ granularity.getBucketSize();
		}

		// determine the bucket we are in right now
		final RasterBucket fBucket = RasterBucket.getBucketOfRelativeValue(
				bucketNumber, granularity);
		final Date value;

		if (DateGranularity.MINUTES.equals(enumGranularity)) {

			// get a date base
			final Date base = Dates.truncateDate(new Date());

			// calculate the absolute time and create the date
			final long time = base.getTime()
					+ (fBucket.getBucketNumber() * 1000l * 60l);
			value = new Date(time);
		} else if (DateGranularity.DAYS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();
			cal.setFirstDayOfWeek(Calendar.SUNDAY);

			cal.clear();
			cal.set(Calendar.DAY_OF_WEEK, fBucket.getBucketNumber());
			value = cal.getTime();
		} else if (DateGranularity.WEEKS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();

			cal.clear();
			cal.set(Calendar.WEEK_OF_YEAR, fBucket.getBucketNumber());

			value = cal.getTime();
		} else if (DateGranularity.MONTHS.equals(enumGranularity)) {
			final Calendar cal = Calendar.getInstance();

			cal.clear();
			cal.set(Calendar.MONTH, fBucket.getBucketNumber());

			value = cal.getTime();
		} else {
			value = null;
		}

		return value;
	}

	@Override
	public Date getAbsoluteBucketStart(final Date valueDate) {
		final Integer relValue = getRelativeValue(valueDate);
		final Integer bucket = getBucket(valueDate).getBucketNumber();
		final Integer diff = -1 * (relValue - bucket);

		final Calendar cal = Calendar.getInstance();

		// increase the date
		if (DateGranularity.MINUTES.equals(enumGranularity)) {
			cal.setTime(valueDate);
			cal.set(Calendar.MILLISECOND, 0);
			cal.set(Calendar.SECOND, 0);
			cal.add(Calendar.MINUTE, diff);
		} else if (DateGranularity.DAYS.equals(enumGranularity)) {
			cal.setTime(Dates.truncateDate(valueDate));
			cal.add(Calendar.DAY_OF_MONTH, diff);
		} else if (DateGranularity.WEEKS.equals(enumGranularity)) {
			cal.setTime(Dates.truncateDate(valueDate));
			cal.add(Calendar.WEEK_OF_YEAR, diff);
		} else if (DateGranularity.MONTHS.equals(enumGranularity)) {
			cal.setTime(Dates.truncateDate(valueDate));
			cal.add(Calendar.MONTH, diff);
		}
		return cal.getTime();
	}

	@Override
	public Date increaseAbsoluteValueByBucketSize(final Date valueDate) {
		final Integer bucketSize = granularity.getBucketSize();
		final Calendar cal = Calendar.getInstance();
		cal.setTime(valueDate);

		// increase the date
		if (DateGranularity.MINUTES.equals(enumGranularity)) {
			cal.add(Calendar.MINUTE, bucketSize);
		} else if (DateGranularity.DAYS.equals(enumGranularity)) {
			cal.add(Calendar.DAY_OF_MONTH, bucketSize);
		} else if (DateGranularity.WEEKS.equals(enumGranularity)) {
			cal.add(Calendar.WEEK_OF_YEAR, bucketSize);
		} else if (DateGranularity.MONTHS.equals(enumGranularity)) {
			cal.add(Calendar.MONTH, bucketSize);
		}

		return cal.getTime();
	}

	@Override
	public int compare(final Date value1, final Date value2) {
		return value1.compareTo(value2);
	}

	@Override
	public int getDifference(final Date minuend, final Date subtrahend) {
		final Long longMinuend = new Long(minuend.getTime() / 1000 / 60);
		final Long longSubtrahend = new Long(subtrahend.getTime() / 1000 / 60);
		final Long substraction = longMinuend - longSubtrahend;

		// return the int value
		return substraction.intValue();
	}

	@Override
	public IRasterGranularity<Date> getGranularity() {
		return granularity;
	}
}
