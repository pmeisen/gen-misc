package net.meisen.general.genmisc.raster.definition.impl.date;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.meisen.general.genmisc.collections.Collections;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseModelData;
import net.meisen.general.genmisc.raster.definition.IRaster;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModel;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.function.impl.BucketLabel;
import net.meisen.general.genmisc.raster.function.impl.Const;
import net.meisen.general.genmisc.raster.function.impl.Count;
import net.meisen.general.genmisc.raster.function.impl.Group;
import net.meisen.general.genmisc.raster.function.impl.IntervalSum;
import net.meisen.general.genmisc.raster.function.impl.Value;
import net.meisen.general.genmisc.raster.utilities.DateRasterUtilities;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;
import net.meisen.general.genmisc.raster.utilities.RasterUtilities;

import org.junit.Test;

/**
 * Tests the implementation of a <code>Raster</code> using the <code>Date</code>
 * -<code>RasterLogic</code>
 * 
 * @author pmeisen
 * 
 */
public class TestDateRaster {
	/**
	 * The name of the <code>RasterModel</code> which is created and added to
	 * the <code>RasterModel</code> in this testcase
	 */
	private static String MODELNAME = "MODEL";
	/**
	 * The name of the interval-start data-field in the <code>ModelData</code>
	 */
	private static String MD_INT_START = "INT_START";
	/**
	 * The name of the interval-end data-field in the <code>ModelData</code>
	 */
	private static String MD_INT_END = "INT_END";
	/**
	 * The name of the interval-start data-field in the
	 * <code>RasterModelData</code>
	 */
	private static String MRD_INT_START = "START";
	/**
	 * The name of the interval-end data-field in the
	 * <code>RasterModelData</code>
	 */
	private static String MRD_INT_END = "END";

	/**
	 * Creates a <code>Raster</code> with a specific <code>RasterModel</code>
	 * 
	 * @param granularity
	 *            the <code>DateGranularity</code> of the <code>Raster</code>
	 *            created
	 * @param bucketSize
	 *            the bucket-size used
	 * @param locale
	 *            the <code>Locale</code> used
	 * @return the created <code>Raster</code>
	 */
	public IRaster<Date> createRasterWithModel(
			final DateGranularity granularity, final int bucketSize,
			final Locale locale) {

		// create the models
		final Map<String, IRasterModel> models = new HashMap<String, IRasterModel>();
		final Object[] paramStart = { MD_INT_START };
		final Object[] paramEnd = { MD_INT_END };

		final IRasterModel model = RasterUtilities.createRasterModel(
				MRD_INT_START, MRD_INT_END, new Value(), new Value(),
				paramStart, paramEnd);
		models.put(MODELNAME, model);

		// create the raster
		final IRaster<Date> raster = DateRasterUtilities.createDateRaster(
				granularity, bucketSize, locale, models);

		return raster;
	}

	/**
	 * Tests the result of adding nothing to the <code>Raster</code>
	 */
	@Test
	public void testEmptyData() {
		final DateGranularity m = DateGranularity.MINUTES;
		final Locale de = new Locale("de");

		// create the raster
		final IRaster<Date> raster = createRasterWithModel(m, 1, de);

		// add some functions to the model
		final BaseRasterModel model = (BaseRasterModel) raster
				.getConfiguration().getModel(MODELNAME);
		model.addEntry(new BaseRasterModelEntry("TEST",
				RasterModelEntryType.VALUE, new Count()));

		// test the result
		final Collection<IRasterModelData> rasterData = raster
				.getRasterModelData(MODELNAME);
		assertEquals(rasterData.size(), 1440);

		for (int i = 0; i < rasterData.size(); i++) {
			final IRasterModelData rmd = Collections.get(i, rasterData);
			assertEquals(rmd.get("TEST"), 0);
		}
	}

	/**
	 * Tests the result of adding some <code>ModelData</code> to the
	 * <code>Raster</code> using the <code>Count</code>-
	 * <code>RasterFunction</code>
	 */
	@Test
	public void testCountedData() {
		final DateGranularity m = DateGranularity.MINUTES;
		final Locale de = new Locale("de");

		// create the raster
		final IRaster<Date> raster = createRasterWithModel(m, 1, de);

		// add some functions to the model
		final BaseRasterModel model = (BaseRasterModel) raster
				.getConfiguration().getModel(MODELNAME);
		model.addEntry(new BaseRasterModelEntry("TEST",
				RasterModelEntryType.VALUE, new Count()));

		// begin the tests
		BaseModelData md;
		Collection<IRasterModelData> rasterData;

		// create data for the whole interval
		md = new BaseModelData();
		md.setValue(MD_INT_START,
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(MD_INT_END, GeneralUtilities.getDate("01.01.2010 24:00:00"));
		raster.addModelData(md);

		// test the result
		rasterData = raster.getRasterModelData(MODELNAME);
		assertEquals(rasterData.size(), 1440);

		for (int i = 0; i < rasterData.size(); i++) {
			final IRasterModelData rmd = Collections.get(i, rasterData);
			assertEquals(rmd.get("TEST"), 1);
		}

		// create data for half a day
		md = new BaseModelData();
		md.setValue(MD_INT_START,
				GeneralUtilities.getDate("01.01.2010 00:00:00"));
		md.setValue(MD_INT_END, GeneralUtilities.getDate("01.01.2010 12:00:00"));
		raster.addModelData(md);

		// test the result
		rasterData = raster.getRasterModelData(MODELNAME);
		assertEquals(rasterData.size(), 1440);

		for (int i = 0; i < rasterData.size(); i++) {
			final IRasterModelData rmd = Collections.get(i, rasterData);

			if (i < 720) {
				assertEquals(rmd.get("TEST"), 2);
			} else {
				assertEquals(rmd.get("TEST"), 1);
			}
		}
	}

	/**
	 * Some complex test using the <code>Raster</code> and some
	 * <code>RasterFunctions</code>
	 */
	@Test
	public void testGroupAndFunctions() {
		final DateGranularity m = DateGranularity.MINUTES;
		final Locale de = new Locale("de");

		// create the raster
		final IRaster<Date> raster = createRasterWithModel(m, 30, de);

		// add some functions to the model
		final BaseRasterModel model = (BaseRasterModel) raster
				.getConfiguration().getModel(MODELNAME);
		model.addEntry(new BaseRasterModelEntry("GROUPER0",
				RasterModelEntryType.GROUP, new Const(), "Const"));
		model.addEntry(new BaseRasterModelEntry("GROUPER1",
				RasterModelEntryType.GROUP, new Value(), "GROUP1"));
		model.addEntry(new BaseRasterModelEntry("GROUPER2",
				RasterModelEntryType.GROUP, new Value(), "GROUP2"));
		model.addEntry(new BaseRasterModelEntry("COUNT",
				RasterModelEntryType.VALUE, new Count()));
		model.addEntry(new BaseRasterModelEntry("COUNTNONULLS",
				RasterModelEntryType.VALUE, new Count(), "ISNULL"));
		model.addEntry(new BaseRasterModelEntry("SUM",
				RasterModelEntryType.VALUE, new IntervalSum()));
		model.addEntry(new BaseRasterModelEntry("GROUP",
				RasterModelEntryType.VALUE, new Group(),
				"[GROUPER1] of [GROUPER2] (Const: [GROUPER0])"));
		model.addEntry(new BaseRasterModelEntry("VALUER0",
				RasterModelEntryType.VALUE, new Value(), "VALUE0"));
		model.addEntry(new BaseRasterModelEntry("LABELER",
				RasterModelEntryType.VALUE, new BucketLabel(),
				"%1$tH:%1$tM - %2$tH:%2$tM"));

		BaseModelData modelData;
		Collection<IRasterModelData> rasterModelData;

		// now create different ModelData to be added
		modelData = new BaseModelData();
		modelData.setValue("GROUP1", "Planned");
		modelData.setValue("GROUP2", "Cleaner");
		modelData.setValue("ISNULL", null);
		modelData.setValue("VALUE0", "Some Value");
		modelData.setValue(MD_INT_START,
				GeneralUtilities.getDate("20.01.1981 00:00:00"));
		modelData.setValue(MD_INT_END,
				GeneralUtilities.getDate("20.01.1981 00:45:00"));
		raster.addModelData(modelData);

		// add the data and check results
		rasterModelData = raster.getAll();
		assertEquals(rasterModelData.size(), 48);

		IRasterModelData d;
		d = Collections.get(0, rasterModelData);
		assertEquals(d.get("COUNT"), 1);
		assertEquals(d.get("COUNTNONULLS"), 0);
		assertEquals(d.get("SUM"), new BigDecimal(30.0));
		assertEquals(d.get("GROUP"), "Planned of Cleaner (Const: Const)");
		assertEquals(d.get("VALUER0"), "Some Value");
		assertEquals(d.get("LABELER"), "00:00 - 00:30");
		d = Collections.get(1, rasterModelData);
		assertEquals(d.get("COUNT"), 1);
		assertEquals(d.get("COUNTNONULLS"), 0);
		assertEquals(d.get("SUM"), new BigDecimal(15.0));
		assertEquals(d.get("GROUP"), "Planned of Cleaner (Const: Const)");
		assertEquals(d.get("VALUER0"), "Some Value");
		assertEquals(d.get("LABELER"), "00:30 - 01:00");

		int i = 0;
		for (final IRasterModelData dRest : rasterModelData) {

			if (i > 1) {
				assertEquals(dRest.get("COUNT"), 0);
				assertEquals(dRest.get("COUNTNONULLS"), 0);
				assertEquals(dRest.get("SUM"), new BigDecimal(0));
				assertEquals(dRest.get("GROUP"),
						"Planned of Cleaner (Const: Const)");
				assertEquals(dRest.get("VALUER0"), "Some Value");
			}

			i++;
		}

		// now create different ModelData to be added
		modelData = new BaseModelData();
		modelData.setValue("GROUP1", "Real");
		modelData.setValue("GROUP2", "Cleaner");
		modelData.setValue("ISNULL", null);
		modelData.setValue(MD_INT_START,
				GeneralUtilities.getDate("20.01.1981 00:18:00"));
		modelData.setValue(MD_INT_END,
				GeneralUtilities.getDate("20.01.1981 00:54:00"));
		raster.addModelData(modelData);

		// add the data and check results
		rasterModelData = raster.getAll();
		assertEquals(rasterModelData.size(), 96);

		d = Collections.get(48, rasterModelData);
		assertEquals(d.get("COUNT"), 1);
		assertEquals(d.get("COUNTNONULLS"), 0);
		assertEquals(d.get("SUM"), new BigDecimal(12.0));
		assertEquals(d.get("GROUP"), "Real of Cleaner (Const: Const)");
		assertEquals(d.get("LABELER"), "00:00 - 00:30");
		d = Collections.get(49, rasterModelData);
		assertEquals(d.get("COUNT"), 1);
		assertEquals(d.get("COUNTNONULLS"), 0);
		assertEquals(d.get("SUM"), new BigDecimal(24.0));
		assertEquals(d.get("GROUP"), "Real of Cleaner (Const: Const)");
		assertEquals(d.get("LABELER"), "00:30 - 01:00");

		i = 0;
		for (final IRasterModelData dRest : rasterModelData) {

			if (i > 50) {
				assertEquals(dRest.get("COUNT"), 0);
				assertEquals(dRest.get("COUNTNONULLS"), 0);
				assertEquals(dRest.get("SUM"), new BigDecimal(0));
				assertEquals(dRest.get("GROUP"),
						"Real of Cleaner (Const: Const)");
			}

			i++;
		}
	}
}
