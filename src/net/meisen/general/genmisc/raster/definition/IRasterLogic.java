package net.meisen.general.genmisc.raster.definition;

/**
 * The interface which specifies a <code>RasterLogic</code>. A
 * <code>RasterLogic</code> specifies the mapping to <code>RasterBuckets</code>
 * and the calculations to determine a <code>RasterBucket</code>.
 * 
 * @author pmeisen
 * 
 * @param <T>
 */
public interface IRasterLogic<T> {

	/**
	 * @param value
	 *            the value to determine the bucket for
	 * @return the {@link RasterBucket}, <code>null</code> if no bucket is valid
	 *         for the value
	 */
	public RasterBucket getBucket(final T value);

	/**
	 * This function returns the relative value for the <code>Raster</code> on
	 * the selected granularity. This value is relative to some starting point
	 * (e.g. 0), whereby a backward calculation to the absolute value is not
	 * possible.
	 * 
	 * @param value
	 *            the value to map into the <code>Raster</code> dimension (i.e.
	 *            {@link Integer}
	 * @return the number (i.e. {@link Integer}) of the <code>Raster</code>,
	 *         <code>null</code> if no value is valid
	 */
	public Integer getRelativeValue(final T value);

	/**
	 * Compares two <code>T</code> objects
	 * 
	 * @param value1
	 *            the first value to compare, must be of type <code>T</code>
	 * @param value2
	 *            the value to compare with, must be of type <code>T</code>
	 * @return the value <code>0</code> if this <code>T</code> is equal to the
	 *         argument <code>T</code>; a value less than <code>0</code> if this
	 *         <code>T</code> is less than the argument <code>T</code>; and a
	 *         value greater than <code>0</code> if this <code>T</code> is
	 *         greater than the argument <code>T</code>
	 */
	public int compare(final T value1, final T value2);

	/**
	 * @param bucket
	 *            the bucket number to determine the absolute start value for
	 * @return the absolute start value of the bucket
	 */
	public T getBucketStart(final RasterBucket bucket);

	/**
	 * @param bucket
	 *            the bucket number to determine the absolute end value for
	 * @return the absolute end value of the bucket
	 */
	public T getBucketEnd(final RasterBucket bucket);

	/**
	 * @param value
	 *            the object of type <code>T</code> to retrieve the absolute
	 *            start of the bucket for
	 * @return the previous bucket as absolute value
	 */
	public T getAbsoluteBucketStart(final T value);

	/**
	 * @param value
	 *            the object of type <code>T</code> to retrieve the absolut end
	 *            of the bucket for
	 * @return the next bucket as absolute value
	 */
	public T getAbsoluteBucketEnd(final T value);

	/**
	 * @param value
	 *            the value of type T to be increased by the bucket size
	 * @return the absolute value of the passed object increased by the defined
	 *         bucket size
	 */
	public T increaseAbsoluteValueByBucketSize(final T value);

	/**
	 * The <code>RasterGranularity</code> used for the <code>RasterLogic</code>
	 * 
	 * @return the <code>RasterGranularity</code> used for the
	 *         <code>RasterLogic</code>
	 */
	public IRasterGranularity<T> getGranularity();

	/**
	 * The <code>RasterLogic</code> must be capable to determine the difference
	 * of two values.
	 * 
	 * @param minuend
	 *            the minuend of the substraction
	 * @param subtrahend
	 *            the subtrahend of the substraction
	 * @return the difference between those two
	 */
	public int getDifference(final T minuend, final T subtrahend);
}