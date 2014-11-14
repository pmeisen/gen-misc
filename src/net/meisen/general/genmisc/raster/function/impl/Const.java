package net.meisen.general.genmisc.raster.function.impl;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsInvariant;

/**
 * A <code>RasterFunction</code> to add a constant value (i.e. a String)
 * 
 * @author pmeisen
 * 
 */
public class Const extends BaseRasterFunction implements IsInvariant {

	@Override
	public String getInitialValue() {
		return null;
	}

	@Override
	public String execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry) {

		// check if we have parameter
		final Object[] parameter = entry.getFunctionParameter();
		final String value;
		if (parameter != null && parameter.length > 0
				&& parameter[0] instanceof String) {
			value = (String) parameter[0];
		} else {
			value = null;
		}

		return value;
	}

}
