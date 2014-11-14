package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Locale;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterLogic;
import net.meisen.general.genmisc.raster.function.impl.BucketLabel.IFormatter;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the <code>BucketLabel</code>-
 * <code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestBucketLabel {

	// define a RasterConfiguration mock for those tests
	IRasterConfiguration<?> configMock = PowerMockito
			.mock(IRasterConfiguration.class);

	/**
	 * Initialize the RasterConfiguration mock
	 */
	@Before
	public void init() {

		// create a mock for the RasterModel
		final IRasterModel model = PowerMockito.mock(IRasterModel.class);
		final IRasterLogic<?> logic = new DateRasterLogic(
				new DateRasterGranularity(DateGranularity.MINUTES, 30));

		// combine the mocks
		when(configMock.getLogic()).thenAnswer(new Answer<IRasterLogic<?>>() {

			@Override
			public IRasterLogic<?> answer(InvocationOnMock invocation)
					throws Throwable {
				return logic;
			}
		});
		when(configMock.getLocale()).thenReturn(new Locale("de"));
		when(configMock.getModel(any(String.class))).thenReturn(model);
	}

	/**
	 * Tests the initial value of the <code>BucketLabel</code>-
	 * <code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final BucketLabel label = new BucketLabel();

		// value must be null
		assertEquals(label.getInitialValue(), null);
	}

	/**
	 * Tests the <code>BucketLabel</code> functionality
	 */
	@Test
	public void testLabel() {

		// create the function
		final BucketLabel label = new BucketLabel();

		// create mocks for the values needed
		final IRasterModelEntry entry = new BaseRasterModelEntry("LABEL",
				RasterModelEntryType.VALUE, label, "%1$tH:%1$tM - %2$tH:%2$tM");

		// execute the function and check the result
		String value;

		// get some values
		value = (String) entry.execute("MODEL", configMock,
				GeneralUtilities.getDate("01.01.2012 00:00:00"),
				GeneralUtilities.getDate("01.01.2012 00:30:00"));
		assertEquals(value, "00:00 - 00:30");

		value = (String) entry.execute("MODEL", configMock,
				GeneralUtilities.getDate("01.01.2012 00:31:00"),
				GeneralUtilities.getDate("01.01.2012 00:45:00"));
		assertEquals(value, "00:30 - 01:00");
	}

	/**
	 * Tests the <code>BucketLabel</code> functionality with an
	 * <code>IFormatter</code>
	 */
	@Test
	public void testLabelWithIFormatter() {

		// create the function
		final BucketLabel label = new BucketLabel();

		final IFormatter formatter = new IFormatter() {

			@Override
			public String format(final Object start, final Object end) {

				if (start instanceof Date) {
					final Date date = (Date) start;
					return new String("" + date.getTime());
				} else {
					return null;
				}
			}
		};

		// create mocks for the values needed
		final IRasterModelEntry entry = new BaseRasterModelEntry("LABEL",
				RasterModelEntryType.VALUE, label, formatter);

		// execute the function and check the result
		String value;

		// get some values
		value = (String) entry.execute("MODEL", configMock,
				GeneralUtilities.getDate("01.01.2012 00:00:00"),
				GeneralUtilities.getDate("01.01.2012 00:30:00"));
		assertEquals(value, "" + GeneralUtilities.getDate("01.01.2012 00:00:00").getTime());

		value = (String) entry.execute("MODEL", configMock,
				GeneralUtilities.getDate("01.01.2012 00:31:00"),
				GeneralUtilities.getDate("01.01.2012 00:45:00"));
		assertEquals(value, "" + GeneralUtilities.getDate("01.01.2012 00:30:00").getTime());
	}
}
