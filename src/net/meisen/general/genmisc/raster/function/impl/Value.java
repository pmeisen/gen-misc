package net.meisen.general.genmisc.raster.function.impl;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsIntervalAndGroupInvariant;

/**
 * This <code>RasterFunction</code> is used to retrieve a value from the passed
 * <code>ModelData</code>. The <code>RasterFunction</code> needs one parameter,
 * the name of the value to be retrieved from the underlying
 * <code>ModelData</code>.<br/>
 * <br/>
 * 
 * <ul>
 * <li>Value(String)</li>
 * </ul>
 * 
 * @author pmeisen
 * 
 */
public class Value extends BaseRasterFunction implements
		IsIntervalAndGroupInvariant {

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final IModelData modelData) {
		
		// check if we have parameter
		final Object[] parameter = entry.getFunctionParameter();
		final Object value;
		if (modelData != null && parameter != null && parameter.length > 0
				&& parameter[0] instanceof String) {
			final String name = (String) parameter[0];

			// get the value
			value = modelData.get(name);
		} else {

			// no name is specified
			value = null;
		}

		// set the value and return it
		return value;
	}

	@Override
	public Object getInitialValue() {
		return null;
	}
}
