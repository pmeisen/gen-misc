package net.meisen.general.genmisc.raster.data.impl.date;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.configuration.impl.BaseRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelDataCollection;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelGroupCollection;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModel;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.function.impl.Count;
import net.meisen.general.genmisc.raster.function.impl.Value;
import net.meisen.general.genmisc.raster.utilities.DateRasterUtilities;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;
import net.meisen.general.genmisc.raster.utilities.RasterUtilities;

import org.junit.Test;


/**
 * Tests the base-implementation of the <code>IRasterModelGroupCollection</code>
 * 
 * @author pmeisen
 * 
 */
public class TestBaseRasterModelGroupCollection {

	/**
	 * Tests the {@link BaseRasterModelGroupCollection#volume()} implementation
	 */
	@Test
	public void testAmountCount() {
		final String[] paramStart = { "START" };
		final String[] paramEnd = { "END" };

		final IRasterConfiguration<Date> configuration = DateRasterUtilities
				.createDateRasterConfiguration(DateGranularity.MINUTES, 1, null);
		final BaseRasterConfiguration<Date> dateConfiguration = (BaseRasterConfiguration<Date>) configuration;
		final IRasterModel model = RasterUtilities.createRasterModel(
				paramStart[0], paramEnd[0], new Value(), new Value(),
				paramStart, paramEnd);
		dateConfiguration.addModel("MODEL", model);

		// create the group collection
		final BaseRasterModelGroupCollection<Date> groupCollection = new BaseRasterModelGroupCollection<Date>(
				dateConfiguration, "MODEL");

		// add some data
		final BaseRasterModelData md = new BaseRasterModelData();
		md.setValue(paramStart[0],
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(paramEnd[0],
				GeneralUtilities.getDate("01.01.2010 24:00:00"));

		// check the initial state
		assertEquals(groupCollection.volume(), 0);

		// add some data and check
		groupCollection.addModelData(md);
		assertEquals(groupCollection.volume(), 1);
		groupCollection.addModelData(md);
		assertEquals(groupCollection.volume(), 2);

		// add some invalid data
		groupCollection.addModelData(null);
		assertEquals(groupCollection.volume(), 2);
		groupCollection.addModelData(new BaseRasterModelData());
		assertEquals(groupCollection.volume(), 2);

		// check after reseting
		groupCollection.reset();
		assertEquals(groupCollection.volume(), 0);
	}

	/**
	 * Tests the {@link BaseRasterModelDataCollection#volume()} implementation
	 */
	@Test
	public void testGetAll() {
		final String[] paramStart = { "START" };
		final String[] paramEnd = { "END" };

		final IRasterConfiguration<Date> configuration = DateRasterUtilities
				.createDateRasterConfiguration(DateGranularity.MINUTES, 1, null);
		final BaseRasterConfiguration<Date> dateConfiguration = (BaseRasterConfiguration<Date>) configuration;
		final BaseRasterModel model = (BaseRasterModel) RasterUtilities
				.createRasterModel(paramStart[0], paramEnd[0], new Value(),
						new Value(), paramStart, paramEnd);
		model.addEntry(new BaseRasterModelEntry("TESTVALUE1",
				RasterModelEntryType.GROUP, new Value(), "VALUE"));
		model.addEntry(new BaseRasterModelEntry("TESTVALUE2",
				RasterModelEntryType.VALUE, new Count(), "VALUE"));
		model.addEntry(new BaseRasterModelEntry("TESTVALUE3",
				RasterModelEntryType.VALUE, new Value(), "VALUE"));
		dateConfiguration.addModel("MODEL", model);

		// create the group collection
		final BaseRasterModelGroupCollection<Date> groupCollection = new BaseRasterModelGroupCollection<Date>(
				dateConfiguration, "MODEL");

		// create the data to be added
		final BaseRasterModelData md = new BaseRasterModelData();
		md.setValue(paramStart[0],
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(paramEnd[0],
				GeneralUtilities.getDate("01.01.2010 24:00:00"));
		md.setValue("VALUE", "Philipp Meisen");

		// add a value
		groupCollection.addModelData(md);
		assertEquals(groupCollection.getAll().size(), 1440);

		// get all the values, the GROUP should have been ignored
		for (final IRasterModelData data : groupCollection.getAll()) {
			assertEquals(null, data.getValue("TESTVALUE1"));
			assertEquals(1, data.getValue("TESTVALUE2"));
			assertEquals("Philipp Meisen", data.getValue("TESTVALUE3"));
		}

		// modify the group
		md.setValue("VALUE", "Christian Kohlschein");

		// add a value
		groupCollection.addModelData(md);
		assertEquals(groupCollection.getAll().size(), 2880);

		// get all the values, the GROUP should have been ignored
		for (final IRasterModelData data : groupCollection.getAll()) {
			assertEquals(null, data.getValue("TESTVALUE1"));
			assertEquals(1, data.getValue("TESTVALUE2"));
		}

		// add another value
		groupCollection.addModelData(md);
		assertEquals(groupCollection.getAll().size(), 2880);

		// get all the values, the GROUP should have been ignored
		for (final IRasterModelData data : groupCollection.getAll()) {
			assertEquals(null, data.getValue("TESTVALUE1"));

			if (data.getValue("TESTVALUE3").equals("Philipp Meisen")) {
				assertEquals(1, data.getValue("TESTVALUE2"));
			} else {
				assertEquals(2, data.getValue("TESTVALUE2"));
			}
		}
	}
}
