package net.meisen.general.genmisc.raster.definition.impl;

import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IRasterFunction;
import net.meisen.general.genmisc.raster.function.IsAggregatable;
import net.meisen.general.genmisc.raster.function.IsIntervalInvariant;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the base-implementation of a <code>RasterModelEntry</code>
 * 
 * @author pmeisen
 * 
 */
public class TestBaseRasterModelEntry {

	/**
	 * Tests the creation of an invalid <code>RasterModelEntry</code>, using an
	 * invalid name
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorNameFailure() {
		final IRasterFunction functionMock = PowerMockito
				.mock(IRasterFunction.class);

		// test to create an invalid function entry
		new BaseRasterModelEntry(null, RasterModelEntryType.VALUE, functionMock);
	}

	/**
	 * Check the invalid construction of a <code>RasterModelEntry</code>, with a
	 * <code>RasterType</code> of <code>null</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorTypeFailure() {
		final IRasterFunction functionMock = PowerMockito
				.mock(IRasterFunction.class);

		// test to create an invalid function entry
		new BaseRasterModelEntry("NAME", null, functionMock);
	}

	/**
	 * Tests the creation of an invalid <code>RasterModelEntry</code>, using an
	 * invalid <code>RasterFunction</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorFunctionFailure() {

		// test to create an invalid function entry
		new BaseRasterModelEntry("NAME", RasterModelEntryType.VALUE, null);
	}

	/**
	 * Tests the creation of an invalid <code>RasterModelEntry</code>, using an
	 * invalid function as <code>RasterModelEntryType.INTERVALSTART</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorIntervalStartFunction() {
		final IsAggregatable functionMock = PowerMockito
				.mock(IsAggregatable.class);

		// test to create an invalid function entry
		new BaseRasterModelEntry("NAME", RasterModelEntryType.INTERVALSTART,
				functionMock);
	}

	/**
	 * Tests the creation of an invalid <code>RasterModelEntry</code>, using an
	 * invalid function as <code>RasterModelEntryType.INTERVALEND</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorIntervalEndFunction() {
		final IsAggregatable functionMock = PowerMockito
				.mock(IsAggregatable.class);

		// test to create an invalid function entry
		new BaseRasterModelEntry("NAME", RasterModelEntryType.INTERVALEND,
				functionMock);
	}

	/**
	 * Tests the creation of an invalid <code>RasterModelEntry</code>, using an
	 * invalid function as <code>RasterModelEntryType.GROUP</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testEntryConstructorGroupFunction() {
		final IsIntervalInvariant functionMock = PowerMockito
				.mock(IsIntervalInvariant.class);

		// test to create an invalid function entry
		new BaseRasterModelEntry("NAME", RasterModelEntryType.GROUP,
				functionMock);
	}
}
