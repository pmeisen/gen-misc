package net.meisen.general.genmisc.raster.data;

/**
 * 
 * @author pmeisen
 * 
 */
public interface IModelData {

	/**
	 * Retrieves a specific value from the <code>ModelData</code> and tries to
	 * parse it to the specified value
	 * 
	 * @param name
	 *            the name to be retrieved
	 * @return the parsed value returned as type <code>T</code>
	 */
	public <T> T get(final String name);

	/**
	 * Returns the associated value for the passed name.
	 * 
	 * @param name
	 *            the name to get the value for
	 * @return the value associated to the name
	 * 
	 * @see IModelData#hasValue(String)
	 */
	public Object getValue(final String name);

	/**
	 * Checks if a value is defined for a specific name
	 * 
	 * @param name
	 *            the name to check for a value
	 * @return <code>true</code> if there is a value associated to the name,
	 *         otherwise <code>false</code>
	 */
	public boolean hasValue(final String name);
}
