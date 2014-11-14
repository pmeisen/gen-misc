package net.meisen.general.genmisc.raster;

import java.util.Locale;

import net.meisen.general.genmisc.raster.configuration.impl.date.TestDateRasterConfiguration;
import net.meisen.general.genmisc.raster.data.impl.TestBaseModelData;
import net.meisen.general.genmisc.raster.data.impl.TestBaseRasterModelData;
import net.meisen.general.genmisc.raster.data.impl.TestRasterModelGroupKey;
import net.meisen.general.genmisc.raster.data.impl.date.TestBaseRasterModelGroupCollection;
import net.meisen.general.genmisc.raster.data.impl.date.TestDateRasterModelDataCollection;
import net.meisen.general.genmisc.raster.definition.impl.TestBaseRasterModel;
import net.meisen.general.genmisc.raster.definition.impl.TestBaseRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.impl.TestRasterBucket;
import net.meisen.general.genmisc.raster.definition.impl.date.TestDateFormatter;
import net.meisen.general.genmisc.raster.definition.impl.date.TestDateRaster;
import net.meisen.general.genmisc.raster.definition.impl.date.TestDateRasterLogic;
import net.meisen.general.genmisc.raster.function.impl.TestBucketLabel;
import net.meisen.general.genmisc.raster.function.impl.TestConst;
import net.meisen.general.genmisc.raster.function.impl.TestCount;
import net.meisen.general.genmisc.raster.function.impl.TestGroup;
import net.meisen.general.genmisc.raster.function.impl.TestIntervalSum;
import net.meisen.general.genmisc.raster.function.impl.TestValue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All tests together as a {@link Suite}
 * 
 * @author pmeisen
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		//
		// ModelData
		TestBaseModelData.class,
		TestBaseRasterModelData.class,

		// RasterModel
		TestRasterBucket.class,
		TestBaseRasterModelEntry.class,
		TestBaseRasterModel.class,
		TestDateRasterLogic.class,

		// Functions
		TestDateFormatter.class, TestConst.class, TestValue.class,
		TestCount.class, TestGroup.class, TestBucketLabel.class,
		TestIntervalSum.class,

		// Configuration
		TestDateRasterConfiguration.class,

		// ModelDataCollection
		TestDateRasterModelDataCollection.class, TestRasterModelGroupKey.class,
		TestBaseRasterModelGroupCollection.class,

		// Raster
		TestDateRaster.class })
public class AllRasterTests {
	private static Locale oldLocale;

	/**
	 * Tests are written for US-Locale
	 */
	@BeforeClass
	public static void setUp() {
		oldLocale = Locale.getDefault();
		Locale.setDefault(Locale.US);
	}

	/**
	 * Reset the old Locale.
	 */
	@AfterClass
	public static void resetLocale() {
		Locale.setDefault(oldLocale);
	}
}
