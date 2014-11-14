package net.meisen.general.genmisc.raster.data.impl.date;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.configuration.impl.BaseRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelDataCollection;
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
 * Tests the base-implementation of the <code>IRasterModelDataCollection</code>
 * 
 * @author pmeisen
 * 
 */
public class TestDateRasterModelDataCollection {

	/**
	 * Tests the {@link BaseRasterModelDataCollection#volume()} implementation
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

		// create the test instance
		final BaseRasterModelDataCollection<Date> dataCollection = new BaseRasterModelDataCollection<Date>(
				configuration, "MODEL");
		final BaseRasterModelData md = new BaseRasterModelData();
		md.setValue(paramStart[0],
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(paramEnd[0],
				GeneralUtilities.getDate("01.01.2010 24:00:00"));

		// check the initial state
		assertEquals(dataCollection.volume(), 0);

		// add some data and check
		dataCollection.addModelData(md);
		assertEquals(dataCollection.volume(), 1);
		dataCollection.addModelData(md);
		assertEquals(dataCollection.volume(), 2);

		// add some invalid data
		dataCollection.addModelData(null);
		assertEquals(dataCollection.volume(), 2);
		dataCollection.addModelData(new BaseRasterModelData());
		assertEquals(dataCollection.volume(), 2);

		// check after reseting
		dataCollection.reset();
		assertEquals(dataCollection.volume(), 0);
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
		dateConfiguration.addModel("MODEL", model);

		// create the test instance
		final BaseRasterModelDataCollection<Date> dataCollection = new BaseRasterModelDataCollection<Date>(
				configuration, "MODEL");
		final BaseRasterModelData md = new BaseRasterModelData();
		md.setValue(paramStart[0],
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(paramEnd[0],
				GeneralUtilities.getDate("01.01.2010 24:00:00"));
		md.setValue("VALUE", "Philipp Meisen");
		
		// add a value
		dataCollection.addModelData(md);

		// get all the values, the GROUP should have been ignored
		for (final IRasterModelData data : dataCollection.getAll()) {
			assertEquals(null, data.getValue("TESTVALUE1"));
			assertEquals(1, data.getValue("TESTVALUE2"));
		}
	}
}
