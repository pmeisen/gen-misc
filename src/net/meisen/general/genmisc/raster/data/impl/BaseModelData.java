package net.meisen.general.genmisc.raster.data.impl;

import java.util.HashMap;
import java.util.Map;

import net.meisen.general.genmisc.raster.data.IModelData;


/**
 * A basic <code>ModelData</code> implementation which is based on a {@link Map}
 * 
 * @author pmeisen
 */
public class BaseModelData implements IModelData {

	// the data collector
	private final Map<String, Object> values = new HashMap<String, Object>();

	@Override
	public Object getValue(final String name) {
		return values.get(name);
	}

	@Override
	public boolean hasValue(final String name) {
		return values.containsKey(name);
	}

	/**
	 * Removes a value from the <code>ModelData</code>
	 * 
	 * @param name
	 *            the value to be removed
	 */
	public void removeValue(final String name) {
		values.remove(name);
	}

	/**
	 * Associated a specific value to a specific name
	 * 
	 * @param name
	 *            the name to associated the passed value to
	 * @param value
	 *            the value to be associated
	 * @return the old value that was associated, <code>null</code> if
	 *         <code>null</code> or no value was associated prior to the call
	 */
	public Object setValue(final String name, final Object value) {
		return values.put(name, value);
	}

	@Override
	public <T> T get(final String name) {

		@SuppressWarnings("unchecked")
		final T val = (T) getValue(name);

		return val;
	}
}
