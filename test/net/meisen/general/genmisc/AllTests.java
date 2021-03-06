package net.meisen.general.genmisc;

import net.meisen.general.genmisc.exceptions.catalog.TestExceptionCatalog;
import net.meisen.general.genmisc.exceptions.catalog.TestLocalizedExceptionCatalog;
import net.meisen.general.genmisc.exceptions.registry.TestDefaultExceptionRegistry;
import net.meisen.general.genmisc.raster.AllRasterTests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All tests together as a {@link Suite}
 * 
 * @author pmeisen
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestDates.class, TestClasses.class, TestStrings.class,
		TestWrappedDataInputReader.class, TestFileByteBufferReader.class,
		TestStreams.class, TestFiles.class, TestCollections.class,
		TestResourceInfo.class, TestResource.class, TestMultiMap.class,
		TestManifest.class, TestVersion.class, TestExceptionCatalog.class,
		TestLocalizedExceptionCatalog.class,
		TestDefaultExceptionRegistry.class, TestNumbers.class,
		TestObjects.class,

		// raster test
		AllRasterTests.class })
public class AllTests {
	// nothing more to do here
}
