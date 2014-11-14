package net.meisen.general.genmisc.raster.definition.impl;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModel;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsIntervalAndGroupInvariant;

import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the base-implementation of a <code>RasterModel</code>
 * 
 * @author pmeisen
 * 
 */
public class TestBaseRasterModel {

	private final IsIntervalAndGroupInvariant dummyFunctionMock = PowerMockito
			.mock(IsIntervalAndGroupInvariant.class);
	private final BaseRasterModelEntry entryStart = new BaseRasterModelEntry(
			"START", RasterModelEntryType.INTERVALSTART, dummyFunctionMock);
	private final BaseRasterModelEntry entryEnd = new BaseRasterModelEntry(
			"END", RasterModelEntryType.INTERVALEND, dummyFunctionMock);

	/**
	 * Helper function used to create a starter <code>RasterModel</code> for
	 * testing purposes, which is based on the defined dummy
	 * <code>IRasterFunction</code> and the <code>RasterModelEntries</code>
	 * 
	 * @return a new <code>RasterModel</code>
	 */
	protected BaseRasterModel createModel() {
		return new BaseRasterModel(entryStart, entryEnd);
	}

	/**
	 * Test the creation of an invalid <code>RasterModel</code>, i.e. invalid
	 * because of a <code>null</code> value for the interval start
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructionStart() {
		new BaseRasterModel(null, entryEnd);
	}

	/**
	 * Test the creation of an invalid <code>RasterModel</code>, i.e. invalid
	 * because of a <code>null</code> value for the interval end
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructionEnd() {
		new BaseRasterModel(entryStart, null);
	}

	/**
	 * Tests the initial creation of an empty <code>RasterModel</code>
	 */
	@Test
	public void testEmptyModel() {
		final BaseRasterModel model = createModel();

		// the empty models should be empty
		assertEquals(model.getSize(), 2);
	}

	/**
	 * Tests the implementation of
	 * {@link BaseRasterModel#addEntry(IRasterModelEntry)}
	 */
	@Test
	public void testAddEntry() {
		final BaseRasterModel model = createModel();

		// test a group entry
		final BaseRasterModelEntry entryGroup1 = new BaseRasterModelEntry(
				"GROUP1", RasterModelEntryType.VALUE, dummyFunctionMock);
		final BaseRasterModelEntry entryGroup2 = new BaseRasterModelEntry(
				"GROUP2", RasterModelEntryType.VALUE, dummyFunctionMock);

		// add the entry and check the models-size
		model.addEntry(entryGroup1);
		assertEquals(model.getSize(), 3);

		// add the same entry with different name check the models-size
		model.addEntry(entryGroup2);
		assertEquals(model.getSize(), 4);

		// overwrite a value
		final BaseRasterModelEntry entry = new BaseRasterModelEntry("GROUP1",
				RasterModelEntryType.VALUE, dummyFunctionMock);

		// add another first entry and check the models-size
		assertEquals(model.addEntry(entry), entryGroup1);
		assertEquals(model.getSize(), 4);
	}

	/**
	 * Try to add an INTERVALSTART <code>RasterModelEntry</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddEntryFailureStart() {
		final BaseRasterModel model = createModel();

		final BaseRasterModelEntry entry = new BaseRasterModelEntry(
				"InvalidEntry", RasterModelEntryType.INTERVALSTART,
				dummyFunctionMock);
		model.addEntry(entry);
	}

	/**
	 * Try to add an INTERVALEND <code>RasterModelEntry</code>
	 */
	@Test(expected = IllegalArgumentException.class)
	public void testAddEntryFailureEnd() {
		final BaseRasterModel model = createModel();

		final BaseRasterModelEntry entry = new BaseRasterModelEntry(
				"InvalidEntry", RasterModelEntryType.INTERVALEND,
				dummyFunctionMock);
		model.addEntry(entry);
	}

	/**
	 * Tests the equals implementation of the <code>RasterModel</code>
	 * implementation
	 */
	@Test
	public void testEquality() {
		final BaseRasterModelEntry entryStart = new BaseRasterModelEntry(
				"START1", RasterModelEntryType.INTERVALSTART, dummyFunctionMock);
		final BaseRasterModelEntry entryEnd = new BaseRasterModelEntry("END1",
				RasterModelEntryType.INTERVALEND, dummyFunctionMock);

		final BaseRasterModel model1 = createModel();
		final BaseRasterModel model2 = new BaseRasterModel(entryStart,
				model1.getIntervalEndEntry());
		final BaseRasterModel model3 = new BaseRasterModel(
				model1.getIntervalStartEntry(), entryEnd);
		final BaseRasterModel model4 = new BaseRasterModel(
				model1.getIntervalStartEntry(), model1.getIntervalEndEntry());

		// null equality
		assertEquals(model1.equals(null), false);

		// self equality
		assertEquals(model1.equals(model1), true);
		assertEquals(model2.equals(model2), true);
		assertEquals(model3.equals(model3), true);
		assertEquals(model4.equals(model4), true);

		// check cross equality
		assertEquals(model1.equals(model2), false);
		assertEquals(model1.equals(model3), false);
		assertEquals(model1.equals(model4), true);
		assertEquals(model2.equals(model3), false);
		assertEquals(model2.equals(model4), false);
		assertEquals(model3.equals(model4), false);

		// modify the model nr. 1 by adding an entry
		final BaseRasterModelEntry entry = new BaseRasterModelEntry("ENTRY",
				RasterModelEntryType.VALUE, dummyFunctionMock);
		assertEquals(model1.addEntry(entry), null);
		assertEquals(model1.equals(model4), false);
		assertEquals(model4.equals(model1), false);

		// modify the model nr. 4 by adding an entry
		assertEquals(model4.addEntry(entry), null);
		assertEquals(model1.equals(model4), true);
		assertEquals(model4.equals(model1), true);
	}

	/**
	 * Test the implementation to retrieve <code>RasterModelEntries</code>
	 * 
	 * @see IRasterModel#getEntries()
	 * @see IRasterModel#getEntries(RasterModelEntryType[])
	 */
	@Test
	public void testEntryRetrieval() {
		final BaseRasterModel model = createModel();
		Collection<IRasterModelEntry> c;

		// check the filtering
		c = model.getEntries();
		assertEquals(c.size(), 2);
		assertEquals(c.contains(entryStart), true);
		assertEquals(c.contains(entryEnd), true);

		c = model.getEntries(RasterModelEntryType.INTERVALSTART);
		assertEquals(c.size(), 1);
		assertEquals(c.contains(entryStart), true);

		c = model.getEntries(RasterModelEntryType.INTERVALEND);
		assertEquals(c.size(), 1);
		assertEquals(c.contains(entryEnd), true);

		c = model.getEntries(RasterModelEntryType.INTERVALEND,
				RasterModelEntryType.INTERVALSTART);
		assertEquals(c.size(), 2);
		assertEquals(c.contains(entryStart), true);
		assertEquals(c.contains(entryEnd), true);
	}
}
