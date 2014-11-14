package net.meisen.general.genmisc.raster.data.impl;

import static org.junit.Assert.assertEquals;

import net.meisen.general.genmisc.raster.data.IModelData;

import org.junit.Test;


/**
 * Tests the base implementation of <code>ModelData</code>
 * 
 * @see IModelData
 * 
 * @author pmeisen
 * 
 */
public class TestBaseModelData {

	/**
	 * Tests the access of the data
	 * 
	 * @see BaseModelData#setValue(String, Object)
	 * @see BaseModelData#hasValue(String)
	 * @see BaseModelData#getValue(String)
	 */
	@Test
	public void testDataAccess() {
		final BaseModelData modelData = new BaseModelData();
		
		assertEquals(modelData.setValue("TESTVAL", "TEST"), null);
		assertEquals(modelData.hasValue("TESTVAL"), true);
		assertEquals(modelData.getValue("TESTVAL"), "TEST");
	}
}
