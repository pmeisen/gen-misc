package net.meisen.general.genmisc.raster.function;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;

/**
 * A data invariant <code>RasterFunction</code> is a function which needs no
 * knowledge about the data to be executed.
 * 
 * @author pmeisen
 * 
 */
public interface IsDataInvariant extends IRasterFunction {

	/**
	 * This function executes the <code>Function</code> independently of any
	 * <code>ModelData</code>
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
	 * @param intervalStart
	 *            the current Start value while splitting
	 * @param intervalEnd
	 *            the current End value while splitting
	 * 
	 * @return the value returned by the <code>Function</code>, the type depends
	 *         on the <code>Function</code> used
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final Object intervalStart,
			final Object intervalEnd);
}
