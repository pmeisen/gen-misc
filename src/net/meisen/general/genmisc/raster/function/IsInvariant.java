package net.meisen.general.genmisc.raster.function;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;

/**
 * A completely invariant <code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public interface IsInvariant extends IRasterFunction {

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
	 * 
	 * @return the value returned by the <code>Function</code>, the type depends
	 *         on the <code>Function</code> used
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry);
}
