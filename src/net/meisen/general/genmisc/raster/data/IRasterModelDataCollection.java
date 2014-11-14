package net.meisen.general.genmisc.raster.data;

import java.util.Collection;

import net.meisen.general.genmisc.raster.definition.RasterBucket;


/**
 * A <code>RasterModelDataCollection</code> is a collection of the
 * <code>RasterModelData</code> used for a specific <code>RasterModel</code>
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the data-type of the interval data
 * 
 */
public interface IRasterModelDataCollection<T> {

	/**
	 * Returns the current <code>RasterModelData</code> defined for the
	 * <code>RasterBucket</code>
	 * 
	 * @param bucket
	 *            the <code>RasterBucket</code>
	 * @return the <code>RasterModelData</code> of the passed
	 *         <code>bucket</code>, <code>null</code> if no data is available
	 */
	public IRasterModelData get(final RasterBucket bucket);

	/**
	 * Resets all currently calculated information
	 */
	public void reset();

	/**
	 * The collection of all the <code>IRasterModelData</code> of this
	 * <code>RasterModelDataCollection</code>
	 * 
	 * @return a collection of all the <code>IRasterModelData</code>
	 */
	public Collection<? extends IRasterModelData> getAll();

	/**
	 * Adds <code>ModelData</code> to the
	 * <code>RastterModelDataCollection</code>
	 * 
	 * @param modelData
	 *            the <code>ModelData</code> to be added
	 * @return <code>true</code> if the data was added, otherwise
	 *         <code>false</code> (i.e. condition of <code>RasterModel</code>
	 *         not fullfilled, start or end is <code>null</code>)
	 */
	public boolean addModelData(final IModelData modelData);

	/**
	 * The amount of <code>ModelData</code> added so far. If more than
	 * <code>Integer.MAX_VALUE</code> data is added, the method will return
	 * <code>Integer.MAX_VALUE</code>.
	 * 
	 * @return the amount of <code>ModelData</code> added so far to the
	 *         <code>RasterModelDataCollection</code>
	 * 
	 * @see Integer#MAX_VALUE
	 */
	public int volume();
}
