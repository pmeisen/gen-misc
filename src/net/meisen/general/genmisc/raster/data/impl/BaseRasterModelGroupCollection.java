package net.meisen.general.genmisc.raster.data.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelDataCollection;
import net.meisen.general.genmisc.raster.data.IRasterModelGroupCollection;
import net.meisen.general.genmisc.raster.definition.Events.Event;
import net.meisen.general.genmisc.raster.definition.Events.RasterModelEvents;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;

/**
 * The base implementation of the <code>RasterModelGroupCollection</code>
 * interface
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the parameter which defines the type of the <code>Raster</code>
 */
public class BaseRasterModelGroupCollection<T> implements
		IRasterModelGroupCollection<T>, Observer {

	/**
	 * The collection which holds the data for each
	 * <code>RasterModelGroup</code>
	 */
	protected final Map<RasterModelGroupKey, IRasterModelDataCollection<T>> dataCollections = new HashMap<RasterModelGroupKey, IRasterModelDataCollection<T>>();
	/**
	 * The <code>RasterConfiguration</code> the <code>RasterModel</code> is
	 * defined in
	 */
	protected final IRasterConfiguration<T> configuration;
	/**
	 * The <code>RasterModel</code> this <code>RasterModelDataCollection</code>
	 * is defined for
	 */
	protected final IRasterModel model;
	/**
	 * The identifier of the <code>RasterModel</code> used within the
	 * <code>RasterConfiguration</code>
	 */
	protected final String modelId;
	/**
	 * the amount of <code>ModelData</code> added so far
	 */
	protected int addedModelData = 0;

	/**
	 * The default constructor used to create a
	 * <code>RasterModelGroupCollection</code>, which contains all the data for
	 * a specific <code>RasterModelGroupCollection</code>
	 * 
	 * @param configuration
	 * @param modelId
	 */
	public BaseRasterModelGroupCollection(
			final IRasterConfiguration<T> configuration, final String modelId) {

		if (configuration == null) {
			throw new IllegalArgumentException(
					"The RasterConfiguration must be defined.");
		}

		this.configuration = configuration;
		this.model = configuration.getModel(modelId);
		this.modelId = modelId;

		// observe the model if possible
		if (this.model == null) {
			throw new IllegalArgumentException("The RasterModel with id '"
					+ modelId
					+ "' cannot be find within the passed RasterConfiguration");
		} else if (this.model instanceof Observable) {
			final Observable observable = (Observable) this.model;

			// observe the model
			observable.addObserver(this);
		}

		// create a RasterModelDataCollection
		final IRasterModelDataCollection<T> dataCollection = new BaseRasterModelDataCollection<T>(
				configuration, modelId);
		this.dataCollections.put(null, dataCollection);
	}

	@Override
	public boolean addModelData(final IModelData modelData) {

		// get the group-values
		final RasterModelGroupKey key = new RasterModelGroupKey(modelData,
				modelId, configuration);

		// get the group and add the data there
		IRasterModelDataCollection<T> collection = dataCollections.get(key);
		if (collection == null) {
			collection = new BaseRasterModelDataCollection<T>(configuration,
					modelId);
			dataCollections.put(key, collection);

			// set the invariant data
			for (final IRasterModelEntry entry : model
					.getEntries(RasterModelEntryType.VALUE)) {

				// execute each invariant function
				if (!entry.isAggregatable() && !entry.isDataInvariant()) {

					// get the value of the function
					final Object value = entry.execute(modelId, configuration,
							modelData);

					// set the calculated value
					for (final IRasterModelData rasterModelData : collection
							.getAll()) {
						rasterModelData.setValue(entry.getName(), value);
					}
				}
			}
		}

		// add the data
		if (collection.addModelData(modelData)) {

			// remove the null group
			if (addedModelData == 0) {
				dataCollections.remove(null);
			}

			// increase the counting
			addedModelData++;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Collection<? extends IRasterModelData> getAll() {
		final List<IRasterModelData> rasterModelData = new ArrayList<IRasterModelData>();

		for (final IRasterModelDataCollection<T> collection : dataCollections
				.values()) {
			rasterModelData.addAll(collection.getAll());
		}

		return rasterModelData;
	}

	@Override
	public void update(final Observable o, final Object object) {

		if (object instanceof Event && o instanceof IRasterModel) {
			final Event event = (Event) object;

			if (event.isType(RasterModelEvents.ENTRYADDED)) {
				final IRasterModelEntry entry = event.getObject();
				final boolean isGroup = entry.getEntryType().equals(
						RasterModelEntryType.GROUP);

				// if data is added and the group modified throw
				if (addedModelData > 0 && isGroup) {
					throw new IllegalStateException(
							"Trying to add the Grouping-RasterEntry '"
									+ entry.getName()
									+ "' after RasterModelData was created");
				}
			}
		}
	}

	@Override
	public int volume() {
		return addedModelData;
	}

	@Override
	public void reset() {
		dataCollections.clear();
		addedModelData = 0;
	}
}
