package net.meisen.general.genmisc.raster.condition;

import net.meisen.general.genmisc.raster.data.IModelData;

/**
 * A <code>RasterModelCondition</code> is used to check if a
 * <code>ModelData</code> is applied to the <code>Raster</code> of the
 * <code>RasterModel</code>
 * 
 * @author pmeisen
 * 
 */
public interface IRasterModelCondition {

	/**
	 * This method is called to check if the specified <code>ModelData</code>
	 * should be used by the <code>RasterModel</code>
	 * 
	 * @param data
	 *            the <code>ModelData</code> to be checked
	 * @return <code>true</code> if the passed <code>ModelData</code> should be
	 *         added to the <code>RasterModel</code>, otherwise
	 *         <code>false</code>
	 */
	public boolean checkCondition(final IModelData data);
}
