package net.meisen.general.genmisc.raster.function.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.impl.BaseModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.utilities.GeneralUtilities;

import org.junit.Test;
import org.junit.Before;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;


/**
 * Tests the implementation of the <code>Group</code>-
 * <code>RasterFunction</code>
 * 
 * @author pmeisen
 * 
 */
public class TestGroup {

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

		// combine the mocks
		when(configMock.getModel(any(String.class))).thenReturn(model);
		when(configMock.getLocale()).thenReturn(new Locale("de"));
		when(model.getEntries(any(RasterModelEntryType[].class))).thenAnswer(
				new Answer<Collection<IRasterModelEntry>>() {

					@Override
					public Collection<IRasterModelEntry> answer(
							final InvocationOnMock invocation) throws Throwable {
						final List<IRasterModelEntry> entries = new ArrayList<IRasterModelEntry>();
						final Object[] args = invocation.getArguments();

						// check the type
						boolean group = false;
						if (args.length > 0
								&& args[0] instanceof RasterModelEntryType) {
							for (final Object type : args) {
								if (type.equals(RasterModelEntryType.GROUP)) {
									group = true;
									break;
								}
							}
						}

						// check if we have the right one and return it
						if (group) {
							entries.add(new BaseRasterModelEntry("ENTRYGROUP1",
									RasterModelEntryType.GROUP, new Value(),
									"GROUP1"));
							entries.add(new BaseRasterModelEntry("ENTRYGROUP2",
									RasterModelEntryType.GROUP, new Value(),
									"GROUP2"));
							entries.add(new BaseRasterModelEntry("ENTRYGROUP3",
									RasterModelEntryType.GROUP, new Value(),
									"GROUP3"));
						}
						return entries;
					}
				});
	}

	/**
	 * Tests the initial value of the Count-<code>RasterFunction</code>
	 */
	@Test
	public void testInitialValue() {

		// create the function
		final Group group = new Group();

		// value must be null
		assertEquals(group.getInitialValue(), null);
	}

	/**
	 * Tests the definition of a fixed grouping value (i.e. always the same
	 * value will be returned)
	 */
	@Test
	public void testFixedGroup() {

		// create the function
		final Group group = new Group();

		// create the entry of the RasterFunction
		final IRasterModelEntry entry = new BaseRasterModelEntry("GROUP",
				RasterModelEntryType.VALUE, group, "My Group");

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();

		// see the result of the function
		final String value = (String) entry.execute("MODEL", configMock,
				modelData);
		assertEquals(value, "My Group");
	}

	/**
	 * Tests the usage of a format for Grouping
	 */
	@Test
	public void testSimpleFormattedGroupUsingStrings() {

		// create the function
		final Group group = new Group();

		// create the entry of the RasterFunction
		final IRasterModelEntry entry = new BaseRasterModelEntry("GROUP",
				RasterModelEntryType.VALUE, group,
				"The value of the ModelData-field GROUP1 is '[ENTRYGROUP1]'");

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		modelData.setValue("GROUP1", "My Group");

		// see the result of the function
		final String value = (String) entry.execute("MODEL", configMock,
				modelData);
		assertEquals(value,
				"The value of the ModelData-field GROUP1 is 'My Group'");
	}

	/**
	 * Tests the usage of a format for Grouping
	 */
	@Test
	public void testSimpleFormattedGroupUsingInteger() {

		// create the function
		final Group group = new Group();

		// create the entry of the RasterFunction
		final IRasterModelEntry entry = new BaseRasterModelEntry("GROUP",
				RasterModelEntryType.VALUE, group,
				"The value of the ModelData-field GROUP1 is '[ENTRYGROUP1]'");

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		modelData.setValue("GROUP1", 5);

		// see the result of the function
		final String value = (String) entry.execute("MODEL", configMock,
				modelData);
		assertEquals(value, "The value of the ModelData-field GROUP1 is '5'");
	}

	/**
	 * Tests the usage of a format for Grouping
	 */
	@Test
	public void testAdvancedFormattedGroup() {

		// create the function
		final Group group = new Group();

		// create the entry of the RasterFunction
		final IRasterModelEntry entry = new BaseRasterModelEntry("GROUP",
				RasterModelEntryType.VALUE, group,
				"[ENTRYGROUP1$td]-[ENTRYGROUP1$tB]-[ENTRYGROUP1$tY]");

		// execute the function and check the result
		final BaseModelData modelData = new BaseModelData();
		modelData.setValue("GROUP1",
				GeneralUtilities.getDate("20.01.1981 00:00:00"));

		// see the result of the function
		final String value = (String) entry.execute("MODEL", configMock,
				modelData);
		assertEquals(value, "20-Januar-1981");
	}
}
