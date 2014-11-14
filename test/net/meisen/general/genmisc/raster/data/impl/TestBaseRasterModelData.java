package net.meisen.general.genmisc.raster.data.impl;

import static org.junit.Assert.assertEquals;

import net.meisen.general.genmisc.raster.data.IRasterModelData;

import org.junit.Test;


/**
 * Tests the base implementation of <code>RasterModelData</code>
 * 
 * @see IRasterModelData
 * 
 * @author pmeisen
 * 
 */
public class TestBaseRasterModelData {

	/**
	 * Tests the data access
	 * 
	 * @see BaseRasterModelData#setValue(String, Object)
	 * @see BaseRasterModelData#hasValue(String)
	 * @see BaseRasterModelData#getValue(String)
	 */
	@Test
	public void testDataAccess() {
		final BaseRasterModelData modelData = new BaseRasterModelData();

		assertEquals(modelData.setValue("TESTVAL", "TEST"), null);
		assertEquals(modelData.hasValue("TESTVAL"), true);
		assertEquals(modelData.getValue("TESTVAL"), "TEST");
	}
}
