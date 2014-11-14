package net.meisen.general.genmisc.raster.data;

import java.util.Collection;

/**
 * A <code>RasterModelGroupCollection</code> is used to
 * 
 * @author pmeisen
 * 
 * @param <T>
 */
public interface IRasterModelGroupCollection<T> {

	/**
	 * Add <code>ModelData</code> to the <code>RasterModelGroupCollection</code>
	 * 
	 * @param modelData
	 *            the <code>ModelData</code> to be added
	 * @return <code>true</code> if the <code>ModelData</code> was added,
	 *         otherwise <code>false</code>
	 */
	public boolean addModelData(final IModelData modelData);

	/**
	 * The collection of all the <code>RasterModelData</code> of this
	 * <code>RasterModelGroupCollection</code>
	 * 
	 * @return a collection of all the <code>IRasterModelData</code>
	 */
	public Collection<? extends IRasterModelData> getAll();

	/**
	 * The amount of <code>ModelData</code> added so far. If more than
	 * <code>Integer.MAX_VALUE</code> data is added, the method will return
	 * <code>Integer.MAX_VALUE</code>.
	 * 
	 * @return the amount of <code>ModelData</code> added so far to the
	 *         <code>RasterModelGroupCollection</code>
	 * 
	 * @see Integer#MAX_VALUE
	 */
	public int volume();

	/**
	 * Resets all currently calculated information
	 */
	void reset();
}
