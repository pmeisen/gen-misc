package net.meisen.general.genmisc.raster.data;

/**
 * Interface for the data description (Model) created by a <code>Raster</code>
 * 
 * @author pmeisen
 * 
 */
public interface IRasterModelData extends IModelData {

	/**
	 * Sets the specified <code>value</code> under the specified
	 * <code>name</code>
	 * 
	 * @param name
	 *            the name to associate the <code>value</code> to
	 * @param value
	 *            the value to be associated
	 * @return the old value that was associated to the <code>name</code>, can
	 *         be <code>null</code> if the old value was <code>null</code> or no
	 *         value was associated to the passed <code>name</code>
	 */
	public Object setValue(final String name, final Object value);
}
