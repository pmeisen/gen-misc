package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.impl.BaseModelData;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.impl.date.DateGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterGranularity;
import net.meisen.general.genmisc.raster.definition.impl.date.DateRasterLogic;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;

import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the <code>IntervalSum</code>-
 * <code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestIntervalSum {

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
				new DateRasterGranularity(DateGranularity.MINUTES, 1));

		// combine the mocks
		when(configMock.getLogic()).thenAnswer(new Answer<IRasterLogic<?>>() {

			@Override
			public IRasterLogic<?> answer(InvocationOnMock invocation)
					throws Throwable {
				return logic;
			}
		});
		when(configMock.getModel(any(String.class))).thenReturn(model);
	}

	/**
	 * Tests the initial value of the Count-<code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final IntervalSum sum = new IntervalSum();

		// value must be null
		assertEquals(sum.getInitialValue(), new BigDecimal(0.0));
	}

	/**
	 * Tests the definition of a fixed grouping value (i.e. always the same
	 * value will be returned)
	 */
	@Test
	public void testSumming() {

		// create the function
		final IntervalSum sum = new IntervalSum();

		// create the entry of the RasterFunction
		final IRasterModelEntry entry = new BaseRasterModelEntry("SUM",
				RasterModelEntryType.VALUE, sum);

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		final BaseRasterModelData rasterModelData = new BaseRasterModelData();

		// see the result of the function
		BigDecimal value;
		value = (BigDecimal) entry.execute("MODEL", configMock, modelData,
				rasterModelData,
				GeneralUtilities.getDate("17.12.2010 13:10:00"),
				GeneralUtilities.getDate("17.12.2010 13:20:00"));
		assertEquals(value, new BigDecimal(10.0));

		// add another value
		value = (BigDecimal) entry.execute("MODEL", configMock, modelData,
				rasterModelData,
				GeneralUtilities.getDate("17.12.2010 08:00:00"),
				GeneralUtilities.getDate("17.12.2010 08:07:00"));
		assertEquals(value, new BigDecimal(17.0));

		// and another another value
		value = (BigDecimal) entry.execute("MODEL", configMock, modelData,
				rasterModelData,
				GeneralUtilities.getDate("17.12.2010 01:00:00"),
				GeneralUtilities.getDate("17.12.2010 04:00:00"));
		assertEquals(value, new BigDecimal(197.0));
	}
}
