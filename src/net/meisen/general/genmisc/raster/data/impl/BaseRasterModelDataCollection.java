package net.meisen.general.genmisc.raster.data.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelDataCollection;
import net.meisen.general.genmisc.raster.definition.Events.Event;
import net.meisen.general.genmisc.raster.definition.Events.RasterModelEvents;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterBucket;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.types.Classes;

/**
 * Implementation of the <code>RasterModelDataCollection</code>
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type of data of the rastered interval
 * 
 */
public class BaseRasterModelDataCollection<T> implements
		IRasterModelDataCollection<T>, Observer {

	/**
	 * The {@link Class} of the generic type <code>T</code>
	 */
	protected final Class<?> clazz;
	/**
	 * The data of the specific <code>Raster</code>
	 */
	protected final Map<RasterBucket, IRasterModelData> dataCollection = new TreeMap<RasterBucket, IRasterModelData>();
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
	 * Creates a <code>RasterModelDataCollection</code> which collects all the
	 * <code>RasterModelData</code> of a <code>RasterModel</code>
	 * 
	 * @param configuration
	 *            the <code>RasterConfiguration</code> of the
	 *            <code>Raster</code>
	 * @param modelId
	 *            the model identifier within the
	 *            <code>RasterConfiguration</code> this
	 *            <code>RasterModelDataCollection</code> is created for
	 */
	public BaseRasterModelDataCollection(
			final IRasterConfiguration<T> configuration, final String modelId) {

		if (configuration == null) {
			throw new IllegalArgumentException(
					"The RasterConfiguration must be defined.");
		}

		this.clazz = Classes.getGenericClass(configuration.getLogic());
		this.configuration = configuration;
		this.model = configuration.getModel(modelId);
		this.modelId = modelId;

		if (this.model == null) {
			throw new IllegalArgumentException("The RasterModel with id '"
					+ modelId
					+ "' cannot be find within the passed RasterConfiguration");
		} else if (this.model instanceof Observable) {
			final Observable observable = (Observable) this.model;

			// observe the model
			observable.addObserver(this);
		}

		// initialize the current known situation
		initialize();
	}

	@Override
	public IRasterModelData get(final RasterBucket bucket) {
		return dataCollection.get(bucket);
	}

	@Override
	public void reset() {
		dataCollection.clear();
		initialize();
	}

	/**
	 * This method is used to initialize the <code>RasterModelData</code> for
	 * all the currently known <code>RasterModelEntries</code> of the
	 * <code>RasterModelConfiguration</code>.
	 */
	protected void initialize() {

		final int bucketStart = configuration.getGranularity().getMin();
		final int bucketEnd = configuration.getGranularity().getMax();
		final int step = configuration.getGranularity().getBucketSize();
		final IRasterLogic<T> logic = configuration.getLogic();

		// create the granularity raster data
		for (int i = bucketStart; i <= bucketEnd; i += step) {

			// create the key for this row
			final RasterBucket bucket = new RasterBucket(i);

			/*
			 * now lets create the array for the data of the BaseRaster. We can
			 * calculate some values for the RasterFunctions already (i.e. the
			 * static once).
			 */
			final IRasterModelData bucketData = new BaseRasterModelData();

			/*
			 * Now we will add a the data of the init RasterFunction which is
			 * used by this BaseRaster
			 */
			for (final IRasterModelEntry e : model.getEntries()) {
				e.initTo(bucketData);

				if (e.isInvariant()) {

					// execute the function
					final Object value = e.execute(modelId, configuration);
					bucketData.setValue(e.getName(), value);
				} else if (e.isDataInvariant()) {
					final Object intervalStart = logic.getBucketStart(bucket);
					final Object intervalEnd = logic.getBucketEnd(bucket);

					// execute the function
					final Object value = e.execute(modelId, configuration,
							intervalStart, intervalEnd);
					bucketData.setValue(e.getName(), value);
				}
			}

			dataCollection.put(bucket, bucketData);
		}

		// set the amount of data to 0
		addedModelData = 0;
	}

	/**
	 * This method is used to initialize the <code>RasterModelData</code> for a
	 * specific <code>RasterModelEntry</code>
	 * 
	 * @param modelEntryName
	 *            the name of the <code>RasterModelEntry</code> which is part of
	 *            the configuration of the <code>RasterModel</code> to be
	 *            initialized
	 */
	protected void initializeEntry(final String modelEntryName) {
		initializeEntry(model.getEntry(modelEntryName));
	}

	/**
	 * Initializes the passed <code>RasterModelEntry</code>
	 * 
	 * @param entry
	 *            the <code>RasterModelEntry</code> to be initialized
	 */
	protected void initializeEntry(final IRasterModelEntry entry) {

		// make sure we have an entry
		if (entry == null) {
			// nothing to do
		} else if (entry.getEntryType().equals(RasterModelEntryType.VALUE)) {
			final int bucketStart = configuration.getGranularity().getMin();
			final int bucketEnd = configuration.getGranularity().getMax();
			final int step = configuration.getGranularity().getBucketSize();

			if (addedModelData > 0) {
				throw new IllegalStateException("The RasterModelEntry '"
						+ entry.getName() + "' was added to the RasterModel '"
						+ modelId + "' after ModelData was added.");
			}

			// create the granularity raster data
			for (int i = bucketStart; i <= bucketEnd; i += step) {

				// create the key for this row
				final RasterBucket bucket = new RasterBucket(i);

				/*
				 * now lets create the array for the data of the BaseRaster. We
				 * can calculate some values for the RasterFunctions already
				 * (i.e. the static once).
				 */
				final IRasterModelData bucketData = new BaseRasterModelData();

				/*
				 * Now we will add a the data of the init RasterFunction which
				 * is used by this BaseRaster
				 */
				entry.initTo(bucketData);

				// add the value for the bucket
				dataCollection.put(bucket, bucketData);
			}
		}
	}

	@Override
	public void update(final Observable o, final Object object) {

		if (object instanceof Event && o instanceof IRasterModel) {
			final Event event = (Event) object;

			if (event.isType(RasterModelEvents.ENTRYADDED)) {
				final IRasterModelEntry entry = event.getObject();
				initializeEntry(entry);
			}
		}
	}

	@Override
	public Collection<? extends IRasterModelData> getAll() {
		return Collections.unmodifiableCollection(dataCollection.values());
	}

	/**
	 * Just a wrapper to transform an object into the specified data type, it
	 * must be ensured that a transformation is possible
	 * 
	 * @param object
	 *            the object to transform
	 * @return the object as {@link Date}
	 * @throws IllegalArgumentException
	 *             if the passed object is not of the generic type
	 */
	@SuppressWarnings("unchecked")
	protected T getData(final Object object) throws IllegalArgumentException {
		if (object == null) {
			return (T) null;
		} else if (clazz.isAssignableFrom(object.getClass()) == false) {
			throw new IllegalArgumentException(
					"The object must be of the type '" + clazz
							+ "', but is of type '" + object.getClass() + "'");
		}

		return (T) object;
	}

	@Override
	public boolean addModelData(final IModelData modelData) {

		// check if the model has to use this data
		if (!model.getCondition().checkCondition(modelData)
				|| modelData == null) {
			return false;
		}

		final IRasterLogic<T> logic = configuration.getLogic();

		// determine the start and end values of the data
		final IRasterModelEntry startEntry = model.getIntervalStartEntry();
		final IRasterModelEntry endEntry = model.getIntervalEndEntry();

		// get the start and end value of the interval
		final Object startVal = startEntry.execute(modelId, configuration,
				modelData);
		final Object endVal = endEntry.execute(modelId, configuration,
				modelData);
		final T start = getData(startVal);
		final T end = getData(endVal);

		// make sure we have an interval defined
		if (start == null || end == null) {
			return false;
		}

		/*
		 * Within the next part of this method the word "value" is used not to
		 * indicate some relative value. It's used to describe that we are
		 * dealing here with real data value working through the interval by
		 * increasing it by the size of the bucket in each iteration.
		 * 
		 * We have to stop the iteration if we reached the end, whereby the most
		 * right value we can reach is the end of the interval.
		 * 
		 * The main problem is to get the first value increased to the next
		 * bucket border. To do so, we use the function:
		 * increaseAbsoluteValueByBucketSize and getNextAbsolutBucketValue,
		 * whereby the second one does the trick
		 */
		int i = 0;
		T bucketValue = start;
		T endValue;

		while (logic.compare(bucketValue, end) < 0) {

			// check if its the first iteration or in between
			if (i == 0) {
				endValue = logic.getAbsoluteBucketEnd(bucketValue);
			} else {
				endValue = logic.increaseAbsoluteValueByBucketSize(bucketValue);
			}

			// get the next bucket value
			if (logic.compare(endValue, end) >= 0) {
				endValue = end;
			}

			// create the key
			final RasterBucket bucketPosition = logic.getBucket(bucketValue);
			final IRasterModelData rasterModelData = dataCollection
					.get(bucketPosition);

			// apply the entries to their RasterModelData instance
			for (final IRasterModelEntry entry : model
					.getEntries(RasterModelEntryType.VALUE)) {

				// execute each function
				if (entry.isAggregatable()) {
					entry.execute(modelId, configuration, modelData,
							rasterModelData, bucketValue, endValue);
				}
			}

			bucketValue = endValue;
			i++;
		}

		addedModelData++;
		return true;
	}

	@Override
	public int volume() {
		return addedModelData;
	}
}
