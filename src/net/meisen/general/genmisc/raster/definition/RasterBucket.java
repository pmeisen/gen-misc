package net.meisen.general.genmisc.raster.definition;

/**
 * Creates a <code>RasterBucket</code> which is a bucket collecting some data,
 * by aggregating the defined metrics
 * 
 * @author pmeisen
 * 
 */
public class RasterBucket implements Comparable<RasterBucket> {

	/**
	 * The internal representation of the <code>RasterBucket</code>
	 */
	private Integer bucketNumber;

	/**
	 * Creates a <code>RasterBucket</code> based on the passed internal
	 * representation
	 * 
	 * @param bucketNumber
	 *            the internal representation of the <code>RasterBucket</code>
	 * @throws IllegalArgumentException
	 *             if the passed internal representation is <code>null</code>
	 */
	public RasterBucket(final Integer bucketNumber)
			throws IllegalArgumentException {

		if (bucketNumber == null)
			throw new IllegalArgumentException(
					"The bucketNumber of a RasterBucket cannot be null");

		this.bucketNumber = bucketNumber;
	}

	/**
	 * @return the internal representation
	 */
	public Integer getBucketNumber() {
		return bucketNumber;
	}

	@Override
	public boolean equals(final Object compare) {
		if (compare instanceof RasterBucket == false)
			return false;

		final RasterBucket compareBucket = (RasterBucket) compare;
		return this.getBucketNumber().equals(compareBucket.getBucketNumber());
	}

	/**
	 * @param granularity
	 *            the {@link IRasterGranularity} used by the underlying
	 *            <code>Raster</code>
	 * @return the relative value {@link Integer} of the last value included in
	 *         the bucket
	 * 
	 */
	public Integer getEndOfBucket(final IRasterGranularity<?> granularity) {
		final Integer bucketSize = granularity.getBucketSize();
		final Integer max = granularity.getMax();

		// depending on the granularity the bucket has to be recalculated
		Integer endOfBucket = bucketNumber + bucketSize - 1;
		endOfBucket = Math.min(endOfBucket, max);
		return endOfBucket;
	}

	/**
	 * Retrieves the next bucket of the bucket, based on the passed
	 * {@link IRasterGranularity}
	 * 
	 * @param granularity
	 *            the {@link IRasterGranularity} used by the underlying
	 *            <code>Raster</code>
	 * @return the next bucket value {@link Integer}
	 */
	public RasterBucket getNextBucket(final IRasterGranularity<?> granularity) {
		final Integer bucketSize = granularity.getBucketSize();

		// depending on the granularity the bucket has to be recalculated
		final Integer nextBucket = bucketNumber + bucketSize;
		return RasterBucket.getBucketOfRelativeValue(nextBucket, granularity);
	}

	/**
	 * @param value
	 *            the relative value in the <code>Raster</code>, e.g. retrieved
	 *            by {@link IRasterLogic#getRelativeValue(Object)}
	 * @param granularity
	 *            the {@link IRasterGranularity} used by the underlying
	 *            <code>Raster</code>
	 * @return the number (i.e. {@link Integer}) of the bucket,
	 *         <code>null</code> if no bucket is valid for the value
	 */
	public static RasterBucket getBucketOfRelativeValue(final Integer value,
			final IRasterGranularity<?> granularity) {

		// get some information from the granularity
		final Integer bucketSize = granularity.getBucketSize();
		final Integer min = granularity.getMin();
		final Integer max = granularity.getMax();
		final Integer diffMaxMin = max - min + 1;

		/*
		 * First lets fit the value into our space between max and min
		 */
		final Integer corrValue;
		if (value < min) {
			// a value smaller than minimum has to be "modulod" prior to the minimum
			corrValue = (value % diffMaxMin) + diffMaxMin - min;
		} else {
			corrValue = value - min;
		}
		final Integer normValue = corrValue % diffMaxMin;

		/*
		 * We have the value in the relative room of the object. We have to look
		 * for the bucket now. To do that we start at the smallest bucket (min)
		 * and start to check how many steps we have to go forward with the
		 * value, whereby the value has to be decreased by the min-value,
		 * because thats were we start - or better the value is already
		 * calculated relatively to the min-value.
		 */
		final Double bucket = min + Math.floor(normValue / bucketSize)
				* bucketSize;

		return new RasterBucket(bucket.intValue());
	}

	public int compareTo(final RasterBucket o) {
		return getBucketNumber().compareTo(o.getBucketNumber());
	}

	@Override
	public int hashCode() {
		return getBucketNumber().hashCode();
	}
}
