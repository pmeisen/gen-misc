package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.impl.BaseModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the Value-<code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestValue {

	/**
	 * Tests the initial value of the Value-<code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final Value value = new Value();

		// value must be null
		assertEquals(value.getInitialValue(), null);
	}

	/**
	 * Tests the retrieval of a value through the Value-
	 * <code>RasterFunction</code>
	 */
	@Test
	public void testValueRetrieval() {

		// create the function
		final Value value = new Value();

		// create mocks for the values needed
		final IRasterConfiguration<?> configMock = PowerMockito
				.mock(IRasterConfiguration.class);
		final IRasterModelEntry entryString = new BaseRasterModelEntry(
				"STRINGVALUE", RasterModelEntryType.VALUE, value, "STRING");
		final IRasterModelEntry entryDate = new BaseRasterModelEntry(
				"DATEVALUE", RasterModelEntryType.VALUE, value, "DATE");
		final IRasterModelEntry entryInteger = new BaseRasterModelEntry(
				"INTVALUE", RasterModelEntryType.VALUE, value, "INTEGER");

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		final String string = "STRING";
		final Date date = new Date();
		final Integer integer = new Integer(100);
		modelData.setValue("STRING", string);
		modelData.setValue("DATE", date);
		modelData.setValue("INTEGER", integer);

		final Object stringValue = entryString.execute("MODEL", configMock,
				modelData);
		assertEquals(stringValue, string);

		final Object dateValue = entryDate.execute("MODEL", configMock,
				modelData);
		assertEquals(dateValue, date);

		final Object intValue = entryInteger.execute("MODEL", configMock,
				modelData);
		assertEquals(intValue, integer);
	}
}
