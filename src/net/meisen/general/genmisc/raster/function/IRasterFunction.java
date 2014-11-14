package net.meisen.general.genmisc.raster.function;

/**
 * A <code>RasterFunction</code> is used to calculate a specific value for a
 * <code>Raster</code> based on the <code>ModelData</code> added.<br/>
 * <br/>
 * It is important to understand, that a function is called every time
 * <code>ModelData</code> is added to a <code>RasterBucket</code>. A
 * <code>RasterFunction</code> doesn't have to return the final value directly
 * but can use pre-calculated values (e.g. from a previous call) to support
 * aggregation (i.e. Count, IntervalSum, Avg, ...).
 * 
 * @author pmeisen
 */
public interface IRasterFunction {

	/**
	 * This method is called once when the <code>Raster</code> is initialized
	 * (i.e. no data is added so far)
	 * 
	 * @return the value which should be when the <code>RasterFunction</code> is
	 *         initialized, i.e. used in a <code>Raster</code> which has no data
	 *         added
	 */
	public Object getInitialValue();
}