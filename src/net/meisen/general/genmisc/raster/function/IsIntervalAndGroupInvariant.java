package net.meisen.general.genmisc.raster.function;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;

/**
 * Marks a <code>RasterFunction</code> to be invariant concerning the interval
 * (i.e. bucket) and the group the data is used in.
 * 
 * @author pmeisen
 */
public interface IsIntervalAndGroupInvariant extends IRasterFunction {

	/**
	 * This function executes the <code>Function</code> independently of any
	 * <code>RasterModelData</code>, i.e. pre-calculations
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
