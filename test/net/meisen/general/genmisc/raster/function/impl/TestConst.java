package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the <code>Const</code>-
 * <code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestConst {

	/**
	 * Tests the initial value of the Const-<code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final Const constF = new Const();

		// value must be null
		assertEquals(constF.getInitialValue(), null);
	}

	/**
	 * Tests the <code>Const</code> functionality
	 */
	@Test
	public void testConst() {

		// create the function
		final Const constF = new Const();

		// create mocks for the values needed
		final IRasterConfiguration<?> configMock = PowerMockito
				.mock(IRasterConfiguration.class);
		final IRasterModelEntry entry = new BaseRasterModelEntry("CONST",
				RasterModelEntryType.VALUE, constF, "MyValue");

		// execute the function and check the result
		final String value = constF.execute("MODEL", configMock, entry);
		assertEquals(value, "MyValue");
	}
}
