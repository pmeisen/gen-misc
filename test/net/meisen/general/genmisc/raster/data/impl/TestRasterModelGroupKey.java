package net.meisen.general.genmisc.raster.data.impl;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.impl.BaseRasterModelEntry;
import net.meisen.general.genmisc.raster.function.impl.Value;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import org.powermock.api.mockito.PowerMockito;


/**
 * Tests the implementation of the <code>RasterModelGroupKey</code>
 * 
 * @author pmeisen
 * 
 */
public class TestRasterModelGroupKey {

	/**
	 * Tests the generation of the <code>RasterModelGroupKey</code>
	 */
	@Test
	public void testKeyGeneration() {

		// do some mocking
		final IRasterConfiguration<?> configMock = PowerMockito
				.mock(IRasterConfiguration.class);
		final IRasterModel model = PowerMockito.mock(IRasterModel.class);
		when(configMock.getModel(any(String.class))).thenReturn(model);
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

		// create the data to create the key for
		RasterModelGroupKey key;
		final BaseModelData modelData = new BaseModelData();
		key = new RasterModelGroupKey(modelData, "MODEL", configMock);
		assertEquals(key.getKey().size(), 3); // null - null - null
		assertEquals(key.getKey().get(0), null);
		assertEquals(key.getKey().get(1), null);
		assertEquals(key.getKey().get(2), null);

		// add a value for GROUP2 values
		modelData.setValue("GROUP2", "VALUE2");
		key = new RasterModelGroupKey(modelData, "MODEL", configMock);
		assertEquals(key.getKey().size(), 3); // null - "VALUE2" - null
		assertEquals(key.getKey().get(0), null);
		assertEquals(key.getKey().get(1), "VALUE2");
		assertEquals(key.getKey().get(2), null);

		// add a value for GROUP1 values
		modelData.setValue("GROUP1", "VALUE1");
		key = new RasterModelGroupKey(modelData, "MODEL", configMock);
		assertEquals(key.getKey().size(), 3); // "VALUE1" - "VALUE2" - null
		assertEquals(key.getKey().get(0), "VALUE1");
		assertEquals(key.getKey().get(1), "VALUE2");
		assertEquals(key.getKey().get(2), null);

		// add a value for GROUP3 values
		modelData.setValue("GROUP3", "VALUE3");
		key = new RasterModelGroupKey(modelData, "MODEL", configMock);
		assertEquals(key.getKey().size(), 3); // "VALUE1" - "VALUE2" - "VALUE3"
		assertEquals(key.getKey().get(0), "VALUE1");
		assertEquals(key.getKey().get(1), "VALUE2");
		assertEquals(key.getKey().get(2), "VALUE3");
	}
}
