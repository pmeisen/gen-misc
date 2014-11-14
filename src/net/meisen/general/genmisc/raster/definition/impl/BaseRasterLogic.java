package net.meisen.general.genmisc.raster.definition.impl;

import java.util.Date;

import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.types.Classes;


/**
 * Creates a raster for a data type. A {@link BaseRaster} defines a infinite
 * space and splits it into disjunct buckets (e.g. 0 - 10, bucket size 1 => 0 -
 * 1, 1 - 2, ...). Each bucket is identified by a unique {@link Integer}, see
 * also {@link RasterBucket}.
 * 
 * If you deal with {@link BaseRaster} objects you have to understand, that a
 * {@link BaseRaster} is created for a data type <code>T</code>, whereby an
 * internal representation (i.e. {@link Integer}) for each value of
 * <code>T</code> can be calculated. The value (internal or external) can be
 * absolute (i.e. is not within the bounds of the {@link BaseRaster} or relative
 * (i.e. is within the bounds of the {@link BaseRaster})
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type which is rastered by the {@link BaseRaster}
 */
public abstract class BaseRasterLogic<T> implements IRasterLogic<T> {

	/**
	 * The {@link Class} of the generic type <code>T</code>
	 */
	protected final Class<?> clazz;
	/**
	 * The defined <code>IRasterGranularity</code> of the
	 * <code>RasterLogic</code>
	 */
	protected final IRasterGranularity<T> granularity;

	/**
	 * The default constructor for the <code>RasterLogic</code>, which is
	 * created for a specific <code>IRasterGranularity</code>
	 * 
	 * @param granularity
	 *            the <code>IRasterGranularity</code> which is the foundation
	 *            for the <code>RasterLogic</code>
	 */
	public BaseRasterLogic(final IRasterGranularity<T> granularity) {
		this.clazz = Classes.getGenericClass(this);
		this.granularity = granularity;
	}

	@Override
	public RasterBucket getBucket(final T object) {
		final Integer value = getRelativeValue(object);

		return RasterBucket.getBucketOfRelativeValue(value, granularity);
	}

	@Override
	public abstract Integer getRelativeValue(final T value);

	/**
	 * Just a wrapper to transform an object into the specified data type, it
	 * must be ensured that a transformation is possible
	 * 
	 * @param object
	 *            the object to transform
	 * @return the object as {@link Date}
	 * @throws IllegalArgumentException
	 *             if the passed object is not of the generic type
	 */
	@SuppressWarnings("unchecked")
	protected T getData(final Object object) throws IllegalArgumentException {
		if (object == null) {
			return (T) null;
		} else if (getClassOfGeneric().isAssignableFrom(object.getClass()) == false) {
			throw new IllegalArgumentException(
					"The object must be of the type '" + getClassOfGeneric()
							+ "', but is of type '" + object.getClass() + "'");
		}

		return (T) object;
	}

	/**
	 * @return the {@link Class} of the generic type used
	 */
	public Class<?> getClassOfGeneric() {
		return clazz;
	}

	@Override
	public T getAbsoluteBucketEnd(final T value) {
		return increaseAbsoluteValueByBucketSize(getAbsoluteBucketStart(value));
	}
}
