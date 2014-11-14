package net.meisen.general.genmisc.raster.definition;

import java.util.Collection;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;


/**
 * The interface which specifies a <code>Raster</code>. A <code>Raster</code> is
 * specified by a
 * 
 * @author pmeisen
 * 
 * @param <T>
 */
public interface IRaster<T> {
	/**
	 * This method adds <code>ModelData</code> to the <code>Raster</code> by
	 * splitting the interval into the different buckets defined for the
	 * <code>Raster</code> by the {@link IRasterConfiguration}
	 * 
	 * @param data
	 *            the data row retrieved from the source
	 * 
	 * @throws IllegalArgumentException
	 *             if one of the passed values is not of data type
	 *             <code>T</code>
	 */
	public void addModelData(final IModelData data)
			throws IllegalArgumentException;

	/**
	 * Use this function to get the <code>RasterModelData</code> of a specific
	 * <code>RasterModel</code>
	 * 
	 * @param model
	 *            the <code>RasterModel</code> to get the data for
	 * @return the <code>RasterModelData</code> of the specified
	 *         <code>RasterModel</code>
	 */
	public Collection<IRasterModelData> getRasterModelData(final String model);

	/**
	 * Use this function to get the <code>RasterModelData</code> of all
	 * <code>RasterModels</code>
	 * 
	 * @return the <code>RasterModelData</code> of the all
	 *         <code>RasterModels</code>
	 */
	public Collection<IRasterModelData> getAll();

	/**
	 * @return the {@link IRasterConfiguration} of the <code>Raster</code>
	 */
	public IRasterConfiguration<T> getConfiguration();
}