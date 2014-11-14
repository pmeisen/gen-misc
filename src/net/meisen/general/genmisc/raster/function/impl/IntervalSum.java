package net.meisen.general.genmisc.raster.function.impl;

import java.math.BigDecimal;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsAggregatable;


/**
 * A <code>RasterFunction</code> which adds up the interval values for each
 * bucket, concerning to the <code>ModelData</code> passed
 * 
 * @author pmeisen
 * 
 */
public class IntervalSum extends BaseRasterFunction implements IsAggregatable {

	@Override
	public BigDecimal getInitialValue() {
		return new BigDecimal(0.0);
	}

	@Override
	public BigDecimal execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final IModelData modelData,
			final IRasterModelData rasterModelData, final Object intervalStart,
			final Object intervalEnd) {

		// get the current value of the count
		final Object cur = rasterModelData.get(entry.getName());
		BigDecimal sum = getInitialValue();
		if (cur != null && cur instanceof BigDecimal) {
			sum = (BigDecimal) cur;
		}

		// get the new value to be added
		@SuppressWarnings("unchecked")
		final IRasterLogic<Object> logic = (IRasterLogic<Object>) configuration
				.getLogic();

		// get the difference
		final int diff = logic.getDifference(intervalEnd, intervalStart);
		
		// finally add the stuff up
		return sum.add(new BigDecimal(diff));
	}

}
