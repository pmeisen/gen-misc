package net.meisen.general.genmisc.raster.definition.impl.date;

import java.util.Date;

import net.meisen.general.genmisc.raster.definition.IRasterGranularity;


/**
 * The {@link DateRasterGranularity} class is used to define the granularity of
 * a <code>Raster</code>.
 * 
 * @author pmeisen
 */
public class DateRasterGranularity implements IRasterGranularity<Date> {
	private DateGranularity granularity;
	private Integer bucketSize;

	/**
	 * Constructor to create a {@link DateRasterGranularity}
	 * 
	 * @param granularity
	 *            the name identifying the granularity to be used
	 * 
	 * @throws IllegalArgumentException
	 *             if the granularity is <code>null</code> or if the selected
	 *             granularity needs additional information which aren't set
	 * 
	 * @see DateGranularity
	 */
	public DateRasterGranularity(final String granularity)
			throws IllegalArgumentException {
		this(granularity, null);
	}

	/**
	 * Constructor to create a {@link DateRasterGranularity}
	 * 
	 * @param granularity
	 *            the <code>Granularity</code> identifying the granularity to be
	 *            used
	 * 
	 * @throws IllegalArgumentException
	 *             if the granularity is <code>null</code> or if the selected
	 *             granularity needs additional information which aren't set
	 */
	public DateRasterGranularity(final DateGranularity granularity)
			throws IllegalArgumentException {
		this(granularity, null);
	}

	/**
	 * Constructor to create a {@link DateRasterGranularity}
	 * 
	 * @param granularity
	 *            the name of the <code>Granularity</code> identifying the
	 *            granularity to be used
	 * @param bucketSize
	 *            the size of the bucket needed for the specified granularity
	 * 
	 * @throws IllegalArgumentException
	 *             if the granularity is <code>null</code> or if the selected
	 *             granularity needs additional information
	 * 
	 * @see DateGranularity
	 */
	public DateRasterGranularity(final String granularity,
			final Integer bucketSize) throws IllegalArgumentException {
		this(DateGranularity.isValid(granularity), bucketSize);
	}

	/**
	 * Constructor to create a {@link DateRasterGranularity}
	 * 
	 * @param granularity
	 *            the <code>Granularity</code> identifying the granularity to be
	 *            used
	 * @param bucketSize
	 *            the size of the bucket needed for the specified granularity
	 * 
	 * @throws IllegalArgumentException
	 *             if the granularity is <code>null</code> or if the selected
	 *             granularity needs additional information
	 * 
	 * @see DateGranularity
	 */
	public DateRasterGranularity(final DateGranularity granularity,
			final Integer bucketSize) throws IllegalArgumentException {

		// check if we have a granularity
		if (granularity == null) {
			throw new IllegalArgumentException("A granularity must be defined");
		}
		// check if we have a bucketSize
		else if (granularity.needsBucketSize() && bucketSize == null) {
			throw new IllegalArgumentException(
					"A bucketsize must be defined if you select the granularity '"
							+ granularity + "'");
		}

		this.granularity = granularity;
		this.bucketSize = (bucketSize == null ? 1 : bucketSize);
	}

	public String getGranularity() {
		return granularity.getName();
	}

	/**
	 * This is only used by the <code>DateRasterLogic</code> instances
	 * 
	 * @return the <code>DateGranularity</code> to be used
	 * 
	 * @see DateGranularity
	 */
	protected DateGranularity getEnum() {
		return granularity;
	}

	@Override
	public Integer getBucketSize() {
		return bucketSize;
	}

	@Override
	public Integer getMin() {
		return granularity.min();
	}

	@Override
	public Integer getMax() {
		return granularity.max();
	}
}
