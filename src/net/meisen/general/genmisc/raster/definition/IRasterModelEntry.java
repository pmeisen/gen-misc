package net.meisen.general.genmisc.raster.definition;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.function.IsAggregatable;
import net.meisen.general.genmisc.raster.function.IsIntervalAndGroupInvariant;
import net.meisen.general.genmisc.raster.function.IsIntervalInvariant;

/**
 * A <code>RasterModelEntry</code> is an entry which specifies which data is
 * included within the created <code>Raster</code>
 * 
 * @author pmeisen
 * 
 */
public interface IRasterModelEntry {

	/**
	 * The name of the <code>RasterModelEntry</code> used to refer to the entry
	 * within a <code>RasterModel</code>
	 * 
	 * @return the name of the <code>RasterModelEntry</code>
	 */
	public String getName();

	/**
	 * The <code>RasterModelEntry</code> defines the semantic information behind
	 * this <code>RasterModelEntry</code>.
	 * 
	 * @return the <code>RasterModelEntry</code> defined for the
	 *         <code>RasterModelEntry</code>
	 */
	public RasterModelEntryType getEntryType();

	/**
	 * @return the parameters which should be passed to the
	 *         <code>RasterFunction</code> of this entry
	 */
	public Object[] getFunctionParameter();

	/**
	 * This method is used to apply the <code>RasterModelEntry</code> to the
	 * passed <code>RasterModelData</code> using the specified
	 * <code>RasterFunction</code> and parameters, i.e. values
	 * 
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 * @param modelData
	 *            the <code>ModelData</code> added to the <code>Raster</code>
	 * @param rasterModelData
	 *            the <code>RasterModelData</code> to which the
	 *            <code>RasterModelEntry</code> should be applied
	 * @param intervalStart
	 *            the current Start value while splitting
	 * @param intervalEnd
	 *            the current End value while splitting
	 * 
	 * @return the value of the <code>RasterModelEntry</code> under the passed
	 *         circumstances
	 * 
	 * @see IsAggregatable
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IModelData modelData, final IRasterModelData rasterModelData,
			final Object intervalStart, final Object intervalEnd);

	/**
	 * This method is used to execute the <code>RasterFunction</code> without
	 * any knowledge about the data (i.e. the <code>RasterFunction</code> is
	 * data invariant).
	 * 
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 * @param intervalStart
	 *            the current Start value while splitting
	 * @param intervalEnd
	 *            the current End value while splitting
	 * 
	 * @return the value of the <code>RasterModelEntry</code> under the passed
	 *         circumstances
	 * 
	 * @see IsIntervalAndGroupInvariant
	 * @see IsIntervalInvariant
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final Object intervalStart, final Object intervalEnd);

	/**
	 * This method is used to execute the <code>RasterFunction</code> without
	 * any knowledge about pre-calculations (i.e. this is important for
	 * aggregations, those don't have any dependent information) and the
	 * interval.
	 * 
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 * @param modelData
	 *            the <code>ModelData</code> added to the <code>Raster</code>
	 * 
	 * @return the value of the <code>RasterModelEntry</code> under the passed
	 *         circumstances
	 * 
	 * @see IsIntervalAndGroupInvariant
	 * @see IsIntervalInvariant
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IModelData modelData);

	/**
	 * This method is used to execute the <code>RasterFunction</code> if it is
	 * invariant.
	 * 
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 * 
	 * @return the value of the <code>RasterModelEntry</code> under the passed
	 *         circumstances
	 * 
	 * @see IsIntervalAndGroupInvariant
	 * @see IsIntervalInvariant
	 */
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration);

	/**
	 * Initializes the <code>RasterModelEntry</code> within the passed
	 * <code>RasterModelData</code>
	 * 
	 * @param rasterModelData
	 *            the <code>RasterModelData</code> which should be initialized
	 */
	public void initTo(final IRasterModelData rasterModelData);

	/**
	 * Checks if the used <code>RasterFunction</code> is interval and group
	 * invariant
	 * 
	 * @return <code>true</code> if the used <code>RasterFunction</code> is
	 *         interval and group invariant, otherwise <code>false</code>
	 */
	public boolean isIntervalAndGroupInvariant();

	/**
	 * Checks if the used <code>RasterFunction</code> is aggregatable
	 * 
	 * @return <code>true</code> if the used <code>RasterFunction</code> is
	 *         aggregatable, otherwise <code>false</code>
	 */
	public boolean isAggregatable();

	/**
	 * Checks if the used <code>RasterFunction</code> is interval invariant
	 * 
	 * @return <code>true</code> if the used <code>RasterFunction</code> is
	 *         interval invariant, otherwise <code>false</code>
	 */
	public boolean isIntervalInvariant();

	/**
	 * Checks if the used <code>RasterFunction</code> is data invariant
	 * 
	 * @return <code>true</code> if the used <code>RasterFunction</code> is data
	 *         invariant, otherwise <code>false</code>
	 */
	public boolean isDataInvariant();

	/**
	 * Checks if the used <code>RasterFunction</code> is invariant
	 * 
	 * @return <code>true</code> if the used <code>RasterFunction</code> is
	 *         invariant, otherwise <code>false</code>
	 */
	public boolean isInvariant();
}
