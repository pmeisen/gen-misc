package net.meisen.general.genmisc.raster.function;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;

/**
 * A <code>RasterFunction</code> which is interval invariant, i.e. the value of
 * the function is the same for each interval/bucket.
 * 
 * @author pmeisen
 */
public interface IsIntervalInvariant extends IRasterFunction {

	/**
	 * This function executes the <code>Function</code>, which is interval
	 * invariant, i.e. returns the same value for each bucket.
	 * 
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 * @param entry
	 *            the <code>RasterModelEntry</code> the
	 *            <code>RasterFunction</code> is defined for
	 * @param modelData
	 *            the <code>ModelData</code> added to the <code>Raster</code>
	 * 
	 * @return the value returned by the <code>Function</code>, the type depends
	 *         on the <code>Function</code> used
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final IModelData modelData);
}
