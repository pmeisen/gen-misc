package net.meisen.general.genmisc.raster.definition;

import net.meisen.general.genmisc.raster.definition.impl.BaseRaster;

/**
 * This interface is used to implement the available granularity for the
 * specific {@link BaseRaster}
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type which is rastered by the {@link BaseRaster}
 */
public interface IRasterGranularity<T> {

	/**
	 * @return the size of the buckets
	 */
	public Integer getBucketSize();

	/**
	 * @return the {@link String} selected
	 */
	public String getGranularity();

	/**
	 * @return the minimal value for a specified granularity bucket (e.g.
	 *         minimal start value)
	 */
	public Integer getMin();

	/**
	 * @return the maximal value for a specified granularity bucket (e.g.
	 *         maximal start value)
	 */
	public Integer getMax();
}
