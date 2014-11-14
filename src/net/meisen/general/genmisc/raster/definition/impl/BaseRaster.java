package net.meisen.general.genmisc.raster.definition.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelGroupCollection;
import net.meisen.general.genmisc.raster.data.impl.BaseRasterModelGroupCollection;
import net.meisen.general.genmisc.raster.definition.IRaster;
import net.meisen.general.genmisc.raster.definition.RasterBucket;

/**
 * Creates a raster for a data type. A {@link BaseRaster} defines a infinite
 * space and splits it into disjunct buckets (e.g. 0 - 10, bucket size 1 => 0 -
 * 1, 1 - 2, ...). Each bucket is identified by a unique {@link Integer}, see
 * also {@link RasterBucket}.
 * 
 * If you deal with {@link BaseRaster} objects you have to understand, that a
 * {@link BaseRaster} is created for a data type <code>T</code>, whereby an
 * internal representation (i.e. {@link Integer}) for each value of
 * <code>T</code> can be calculated. The value (internal or external) can be
 * absolute (i.e. is not within the bounds of the {@link BaseRaster} or relative
 * (i.e. is within the bounds of the {@link BaseRaster})
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type which is rastered by the {@link BaseRaster}
 */
public class BaseRaster<T> implements IRaster<T>, Observer {

	/**
	 * The configuration of the <code>Raster</code>
	 */
	protected final IRasterConfiguration<T> configuration;
	/**
	 * the data of each defined {@link BaseRaster}
	 */
	protected final Map<String, IRasterModelGroupCollection<T>> dataGroups = new HashMap<String, IRasterModelGroupCollection<T>>();

	/**
	 * @param configuration
	 *            the {@link IRasterConfiguration} to be used
	 */
	public BaseRaster(final IRasterConfiguration<T> configuration) {

		this.configuration = configuration;

		// create a raster for each model defined
		for (final String id : configuration.getModelIds()) {
			final IRasterModelGroupCollection<T> collection = new BaseRasterModelGroupCollection<T>(
					configuration, id);
			dataGroups.put(id, collection);
		}

		// add this Observer if possible
		if (configuration instanceof Observable) {
			final Observable observable = (Observable) configuration;
			observable.addObserver(this);
		}
	}

	@Override
	public void addModelData(final IModelData modelData)
			throws IllegalArgumentException {

		// get the bucket for this key
		for (final IRasterModelGroupCollection<T> dataGroup : dataGroups
				.values()) {

			// add the modelData to the raster of the RasterModel
			dataGroup.addModelData(modelData);
		}
	}

	@Override
	public IRasterConfiguration<T> getConfiguration() {
		return configuration;
	}

	@Override
	public Collection<IRasterModelData> getRasterModelData(final String model) {
		final IRasterModelGroupCollection<T> data = dataGroups.get(model);

		// return all the values
		return Collections.unmodifiableCollection(data.getAll());
	}

	@Override
	public Collection<IRasterModelData> getAll() {
		final List<IRasterModelData> allRasterData = new ArrayList<IRasterModelData>();

		// run through the data
		for (final String model : dataGroups.keySet()) {

			// get the data of the model
			final Collection<IRasterModelData> rasterData = getRasterModelData(model);
			allRasterData.addAll(rasterData);
		}

		// return the all RasterModelData
		return allRasterData;
	}

	@Override
	public void update(final Observable o, final Object object) {
		// currently there is nothing to do
	}
}
