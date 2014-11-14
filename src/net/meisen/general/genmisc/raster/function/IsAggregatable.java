package net.meisen.general.genmisc.raster.function;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;

/**
 * Marks a <code>RasterFunction</code> to be aggregatable, i.e. a
 * <code>RasterFunction</code> can be executed for each interval and the value
 * changes.
 * 
 * @author pmeisen
 * 
 */
public interface IsAggregatable extends IRasterFunction {

	/**
	 * This function executes the <code>Function</code>
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
	 * @param rasterModelData
	 *            the so far calculated <code>RasterModelData</code> of the
	 *            <code>Raster</code>
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
			final IRasterModelEntry entry, final IModelData modelData,
			final IRasterModelData rasterModelData, final Object intervalStart,
			final Object intervalEnd);
}
