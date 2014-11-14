package net.meisen.general.genmisc.raster.function.impl;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsAggregatable;

/**
 * This <code>RasterFunction</code> counts the amount of entries.<br />
 * <br />
 * The <code>Count</code> function has one optional parameter:<br/>
 * <ul>
 * <li><b>Count()</b> - increased by every call</li>
 * <li><b>Count(String)</b> - increased if the passed <code>ModelData</code>
 * contains a value unequal to <code>null</code> for the passed String</li>
 * </ul>
 * 
 * @author pmeisen
 * 
 */
public class Count extends BaseRasterFunction implements IsAggregatable {

	@Override
	public Integer execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final IModelData modelData,
			final IRasterModelData rasterModelData, final Object intervalStart,
			final Object intervalEnd) {

		// check if we have parameter
		final Object[] parameter = entry.getFunctionParameter();
		int increaseBy;
		if (parameter != null && parameter.length > 0
				&& parameter[0] instanceof String) {
			final String name = (String) parameter[0];
			increaseBy = modelData.get(name) != null ? 1 : 0;
		} else {
			increaseBy = 1;
		}

		// get the current value of the count
		final Object cur = rasterModelData.get(entry.getName());
		int counter = getInitialValue();
		if (cur != null && cur instanceof Integer) {
			counter = (Integer) cur;
		}

		// increase it
		counter += increaseBy;

		// set the value and return
		rasterModelData.setValue(entry.getName(), counter);
		return counter;
	}

	@Override
	public Integer getInitialValue() {
		return 0;
	}
}
