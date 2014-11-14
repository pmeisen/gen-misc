package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.impl.BaseModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the Count-<code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestCount {

	/**
	 * Tests the initial value of the Count-<code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final Count count = new Count();

		// value must be null
		assertEquals(count.getInitialValue(), new Integer(0));
	}

	/**
	 * Tests the simple counting, i.e. count each <code>ModelData</code>
	 */
	@Test
	public void testSimpleCounting() {

		// create the function
		final Count count = new Count();

		// create mocks for the values needed
		final IRasterConfiguration<?> configMock = PowerMockito
				.mock(IRasterConfiguration.class);
		final IRasterModelEntry entry = new BaseRasterModelEntry("COUNT",
				RasterModelEntryType.VALUE, count);

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		final BaseRasterModelData rasterModelData = new BaseRasterModelData();
		for (int i = 1; i <= 10; i++) {
			final int value = count.execute("MODEL", configMock, entry,
					modelData, rasterModelData, null, null);

			assertEquals(value, i);
			assertEquals(rasterModelData.get(entry.getName()), i);
		}
	}

	/**
	 * Tests the counting with an additional value
	 */
	@Test
	public void testNoneNullCounting() {

		// create the function
		final Count count = new Count();

		// create mocks for the values needed
		final IRasterConfiguration<?> configMock = PowerMockito
				.mock(IRasterConfiguration.class);
		final IRasterModelEntry entry = new BaseRasterModelEntry("COUNT",
				RasterModelEntryType.VALUE, count, "NOTNULL");

		// execute the function and check the result
		final BaseModelData modelData1 = new BaseModelData();
		final BaseModelData modelData2 = new BaseModelData();
		modelData2.setValue("NOTNULL", "VALUE");
		final BaseRasterModelData rasterModelData = new BaseRasterModelData();
		for (int i = 1; i <= 10; i++) {
			final int value;

			if (i % 2 == 1) {
				value = (Integer) entry.execute("MODEL", configMock, modelData1,
						rasterModelData, null, null);
			} else {
				value = (Integer) entry.execute("MODEL", configMock, modelData2,
						rasterModelData, null, null);
			}

			assertEquals(value, ((int) i / 2));
			assertEquals(rasterModelData.get(entry.getName()), ((int) i / 2));
		}
	}
}
