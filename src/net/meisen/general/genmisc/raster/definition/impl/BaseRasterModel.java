package net.meisen.general.genmisc.raster.definition.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Observable;

import net.meisen.general.genmisc.collections.Collections.Filter;
import net.meisen.general.genmisc.raster.condition.IRasterModelCondition;
import net.meisen.general.genmisc.raster.condition.RasterModelConditions;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.definition.Events.Event;
import net.meisen.general.genmisc.raster.definition.Events.RasterModelEvents;


/**
 * A usable base-implementation of a <code>RasterModel</code>
 * 
 * @author pmeisen
 * 
 */
public class BaseRasterModel extends Observable implements IRasterModel {
	private final Map<String, IRasterModelEntry> entries = new LinkedHashMap<String, IRasterModelEntry>();

	private IRasterModelCondition condition = RasterModelConditions.TAUTOLOGY;
	private IRasterModelEntry intervalStartEntry = null;
	private IRasterModelEntry intervalEndEntry = null;

	/**
	 * Creates a <code>RasterModel</code> with the specified start and end
	 * <code>RasterModelEntries</code>. The entries must be of the following
	 * <code>RasterModelEntryType</code>:
	 * <ul>
	 * <li>{@link RasterModelEntryType#INTERVALSTART} for
	 * <code>intervalStartEntry</code></li>
	 * <li>{@link RasterModelEntryType#INTERVALEND} for
	 * <code>intervalEndEntry</code></li>
	 * </ul>
	 * 
	 * @param intervalStartEntry
	 *            the <code>RasterModelEntry</code> to be used as interval start
	 *            entry, cannot be <code>null</code>
	 * @param intervalEndEntry
	 *            the <code>RasterModelEntry</code> to be used as interval end
	 *            entry, cannot be <code>null</code>
	 * 
	 * @throws IllegalArgumentException
	 *             if the passed <code>RasterModelEntries</code> are of the
	 *             wrong type or the names are <code>null</code>
	 */
	public BaseRasterModel(final IRasterModelEntry intervalStartEntry,
			final IRasterModelEntry intervalEndEntry)
			throws IllegalArgumentException {

		if (intervalStartEntry == null || intervalEndEntry == null) {
			throw new IllegalArgumentException(
					"The interval start and end must be defined for a RasterModel");
		} else if (!RasterModelEntryType.INTERVALSTART
				.equals(intervalStartEntry.getEntryType())) {
			throw new IllegalArgumentException(
					"The interval start must be of the RasterModelEntryType '"
							+ RasterModelEntryType.INTERVALSTART + "'");
		} else if (!RasterModelEntryType.INTERVALEND.equals(intervalEndEntry
				.getEntryType())) {
			throw new IllegalArgumentException(
					"The interval end must be of the RasterModelEntryType '"
							+ RasterModelEntryType.INTERVALEND + "'");
		}

		// set the entries
		this.intervalStartEntry = intervalStartEntry;
		this.intervalEndEntry = intervalEndEntry;

		// add those to the entries
		entries.put(intervalStartEntry.getName(), intervalStartEntry);
		entries.put(intervalEndEntry.getName(), intervalEndEntry);
	}

	/**
	 * Removes all <code>RasterFunctions</code> from the <code>Model</code>
	 */
	public void resetRasterModel() {
		entries.clear();

		setChanged();
		notifyObservers(new Event(RasterModelEvents.MODELCLEARED, null));
	}

	@Override
	public IRasterModelEntry getEntry(final String name) {
		return entries.get(name);
	}

	/**
	 * Adds the specified <code>ModelEntry</code> to the <code>Model</code>
	 * 
	 * @param entry
	 *            the <code>ModelEntry</code>
	 * @return the old <code>ModelEntry</code> which might have been associated
	 *         to the name, <code>null</code> if no <code>ModelEntry</code> was
	 *         associated
	 * 
	 * @see IRasterModelEntry
	 */
	public IRasterModelEntry addEntry(final IRasterModelEntry entry) {

		// make sure that no start or end is added
		if (RasterModelEntryType.INTERVALSTART.equals(entry.getEntryType())) {
			throw new IllegalArgumentException(
					"The interval start must be defined at construction time and cannot be manipulated afterwards");
		} else if (RasterModelEntryType.INTERVALEND
				.equals(entry.getEntryType())) {
			throw new IllegalArgumentException(
					"The interval end must be defined at construction time and cannot be manipulated afterwards");
		}

		// modify the entries
		final IRasterModelEntry oldEntry = entries.put(entry.getName(), entry);

		// notify
		setChanged();
		notifyObservers(new Event(RasterModelEvents.ENTRYADDED, entry));

		// return
		return oldEntry;
	}

	@Override
	public int getSize() {
		return entries.size();
	}

	@Override
	public Collection<String> getNames() {
		return Collections.unmodifiableCollection(entries.keySet());
	}

	@Override
	public Collection<IRasterModelEntry> getEntries() {
		return Collections.unmodifiableCollection(entries.values());
	}

	@Override
	public Collection<IRasterModelEntry> getEntries(
			final RasterModelEntryType... entryTypies) {
		final Filter<IRasterModelEntry> filter = new Filter<IRasterModelEntry>() {

			@Override
			public boolean check(final IRasterModelEntry entry) {

				boolean equal = false;

				if (entryTypies != null) {
					for (final RasterModelEntryType entryType : entryTypies) {
						if (entry.getEntryType().equals(entryType)) {
							equal = true;
							break;
						}
					}
				}

				return equal;
			}
		};

		return net.meisen.general.genmisc.collections.Collections.filter(
				entries.values(), filter);
	}

	@Override
	public boolean equals(final Object obj) {
		boolean cmpResult = true;

		// check type matching
		if (obj == null || obj instanceof IRasterModel == false) {
			cmpResult = false;
		} else {
			final IRasterModel cmpModel = (IRasterModel) obj;

			// check the sizes
			if (cmpModel.getSize() != getSize()) {
				cmpResult = false;
			} else {

				// check the names
				for (final String name : cmpModel.getNames()) {

					// check the name and the associated entry
					if (!entries.containsKey(name)) {
						cmpResult = false;
						break;
					} else if (!getEntry(name).equals(cmpModel.getEntry(name))) {
						cmpResult = false;
						break;
					}
				}
			}
		}

		// return the result
		return cmpResult;
	}

	@Override
	public IRasterModelEntry getIntervalStartEntry() {
		return intervalStartEntry;
	}

	@Override
	public IRasterModelEntry getIntervalEndEntry() {
		return intervalEndEntry;
	}

	/**
	 * This method is called to set a <code>RasterModelCondition</code> which is
	 * used to determine if a <code>ModelData</code> should be used by the
	 * <code>RasterModel</code>
	 * 
	 * @param condition
	 *            the <code>RasterModelCondition</code> to be used, if set to
	 *            <code>null</code> a tautology condition will be used
	 * 
	 * @see IRasterModelCondition
	 * @see RasterModelConditions#TAUTOLOGY
	 */
	public void setCondition(final IRasterModelCondition condition) {
		if (condition == null) {
			this.condition = RasterModelConditions.TAUTOLOGY;
		} else {
			this.condition = condition;
		}
	}

	@Override
	public IRasterModelCondition getCondition() {
		return this.condition;
	}
}
