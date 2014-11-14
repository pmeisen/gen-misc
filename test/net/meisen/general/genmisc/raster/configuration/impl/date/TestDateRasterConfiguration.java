package net.meisen.general.genmisc.raster.configuration.impl.date;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import java.util.Locale;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.utilities.DateRasterUtilities;

import org.junit.Test;


/**
 * Tests the implementation of the <code>RasterConfiguration</code> for
 * <code>Date</code> instances.
 * 
 * @author pmeisen
 * 
 */
public class TestDateRasterConfiguration {

	/**
	 * Tests the initialization of the <code>RasterConfiguration</code>
	 */
	@Test
	public void testInitialization() {
		final DateGranularity m = DateGranularity.MINUTES;
		final Locale de = new Locale("de");

		final IRasterConfiguration<Date> configuration = DateRasterUtilities
				.createDateRasterConfiguration(m, 5, de);

		// check the getters
		assertEquals(configuration.getGranularity().getGranularity(),
				m.toString());
		assertEquals(configuration.getLocale(), de);

		// check that no models are available yet
		assertEquals(configuration.getModels().size(), 0);
	}
}
