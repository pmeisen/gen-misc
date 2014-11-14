package net.meisen.general.genmisc.raster.data.impl;

import java.util.ArrayList;
import java.util.List;

import net.meisen.general.genmisc.collections.Collections;
import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.types.Objects;


/**
 * A key which is used to identify a <code>RasterGroup</code>
 * 
 * @author pmeisen
 * 
 */
public class RasterModelGroupKey {

	/**
	 * The key which will be generated
	 */
	private final List<Object> key = new ArrayList<Object>();

	/**
	 * Default constructor, which directly generates the
	 * <code>RasterModelGroupKey</code>
	 * 
	 * @param modelData
	 *            the <code>ModelData</code> to create the
	 *            <code>RasterModelGroupKey</code> for
	 * @param modelId
	 *            the identifier of the <code>RasterModel</code>, which defines
	 *            the <code>RasterModelEntry</code>
	 * @param configuration
	 *            the <code>IRasterConfiguration</code> the
	 *            <code>RasterModelEntry</code> is used in
	 */
	public RasterModelGroupKey(final IModelData modelData,
			final String modelId, final IRasterConfiguration<?> configuration) {

		// get the model
		final IRasterModel model = configuration.getModel(modelId);

		// create the key for the ModelData
		if (modelData != null) {
			for (final IRasterModelEntry entry : model
					.getEntries(RasterModelEntryType.GROUP)) {

				// execute each function
				final Object o = entry.execute(modelId, configuration,
						modelData);
				key.add(o);
			}
		}
	}

	/**
	 * @return the generated key
	 */
	protected List<Object> getKey() {
		return key;
	}

	@Override
	public int hashCode() {
		return Collections.generateHashCode(key, 10, 7);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean equal = true;

		if (obj instanceof RasterModelGroupKey) {
			final RasterModelGroupKey rmgk = (RasterModelGroupKey) obj;
			final List<Object> key = rmgk.key;

			if (key.size() != this.key.size()) {
				equal = false;
			} else {

				// compare the lists
				for (int i = 0; i < key.size(); i++) {
					if (!Objects.equals(key.get(i), this.key.get(i))) {
						equal = false;
						break;
					}
				}
			}
		} else {
			equal = false;
		}

		return equal;
	}

	@Override
	public String toString() {
		String keyString = "";
		for (int i = 0; i < key.size(); i++) {
			keyString += (i == 0 ? "" : "-") + key.get(0);
		}

		return keyString;
	}
}
