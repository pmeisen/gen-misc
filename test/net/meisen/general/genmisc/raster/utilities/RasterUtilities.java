package net.meisen.general.genmisc.raster.utilities;

import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModel;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IRasterFunction;

/**
 * Some helper utilities when testing <code>Raster</code>
 * 
 * @author pmeisen
 * 
 */
public class RasterUtilities {

	/**
	 * Creates a <code>RasterModel</code>
	 * 
	 * @param nameStart
	 *            the name of the start
	 * @param nameEnd
	 *            the name of the end
	 * @param functionStart
	 *            the <code>RasterFunction</code> used to determine the start
	 * @param functionEnd
	 *            the <code>RasterFunction</code> used to determine the end
	 * @param parametersStart
	 *            the start parameters for the start <code>RasterFunction</code>
	 * @param parametersEnd
	 *            the end parameters for the start <code>RasterFunction</code>
	 * @return the <code>RasterModel</code>
	 */
	public static IRasterModel createRasterModel(final String nameStart,
			final String nameEnd, final IRasterFunction functionStart,
			final IRasterFunction functionEnd, final Object[] parametersStart,
			final Object[] parametersEnd) {

		// create the interval boundaries
		final BaseRasterModelEntry entryStart = new BaseRasterModelEntry(
				nameStart, RasterModelEntryType.INTERVALSTART, functionStart,
				parametersStart);
		final BaseRasterModelEntry entryEnd = new BaseRasterModelEntry(nameEnd,
				RasterModelEntryType.INTERVALEND, functionEnd, parametersEnd);

		// create the model
		final BaseRasterModel model = new BaseRasterModel(entryStart, entryEnd);

		return model;
	}
}
