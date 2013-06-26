package net.meisen.general.genmisc;

import net.meisen.general.genmisc.exceptions.catalog.TestExceptionCatalog;
import net.meisen.general.genmisc.exceptions.catalog.TestLocalizedExceptionCatalog;
import net.meisen.general.genmisc.exceptions.registry.TestDefaultExceptionRegistry;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * All tests together as a {@link Suite}
 * 
 * @author pmeisen
 * 
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestClasses.class, TestStrings.class, TestStreams.class,
		TestFiles.class, TestCollections.class, TestResourceInfo.class,
		TestResource.class, TestMultiMap.class, TestManifest.class,
		TestVersion.class, TestExceptionCatalog.class,
		TestLocalizedExceptionCatalog.class, TestDefaultExceptionRegistry.class })
public class AllTests {
	// nothing more to do here
}
