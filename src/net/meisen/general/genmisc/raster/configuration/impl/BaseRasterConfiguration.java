package net.meisen.general.genmisc.raster.configuration.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.Events.Event;
import net.meisen.general.genmisc.raster.definition.Events.RasterConfigurationEvents;
import net.meisen.general.genmisc.types.Locales;


/**
 * A basic abstract implementation of the {@link IRasterConfiguration}
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type of the {@link IRasterConfiguration}
 */
public class BaseRasterConfiguration<T> extends Observable implements
		IRasterConfiguration<T>, Observer {

	/**
	 * The <code>RasterModels</code> defined
	 */
	protected Map<String, IRasterModel> models = new HashMap<String, IRasterModel>();
	/**
	 * the <code>RasterGranularity</code> of the <code>Raster</code>
	 */
	protected final IRasterGranularity<T> granularity;
	/**
	 * the <code>RasterLogic</code> to be used
	 */
	protected final IRasterLogic<T> logic;
	/**
	 * The {@link Locale} setting for this <code>Raster</code>
	 */
	protected Locale locale = Locale.getDefault();

	/**
	 * The <code>Granularity</code> used for the <code>Raster</code>
	 * 
	 * @param logic
	 *            the <code>RasterGranularity</code> of the <code>Raster</code>,
	 *            cannot be <code>null</code>
	 */
	public BaseRasterConfiguration(final IRasterLogic<T> logic) {

		if (logic == null) {
			throw new IllegalArgumentException("The RasterLogic cannot be null");
		}

		this.granularity = logic.getGranularity();
		this.logic = logic;
	}

	/**
	 * Adds a <code>RasterModel</code> to the <code>RasterConfiguration</code>
	 * and returns the old <code>RasterModel</code> associated, if there is one.
	 * 
	 * @param name
	 *            the name the <code>RasterModel</code> should be associated to
	 * @param model
	 *            the <code>RasterModel</code> to associate with the passed
	 *            <code>name</code>
	 * @return the old <code>RasterModel</code>, which was associated or
	 *         <code>null</code> if not <code>RasterModel</code> was associated
	 */
	public IRasterModel addModel(final String name, final IRasterModel model) {

		// add the model
		final IRasterModel oldModel = models.put(name, model);

		// remove the listener from the old Model
		if (oldModel != null && oldModel instanceof Observable) {
			final Observable observable = (Observable) oldModel;
			observable.deleteObserver(this);
		}

		// add this observer and notify the observers of the configuration
		if (model != null && model instanceof Observable) {
			final Observable observable = (Observable) model;
			observable.addObserver(this);
		}
		setChanged();
		notifyObservers(new Event(RasterConfigurationEvents.MODELADDED, name));

		// return the old model
		return oldModel;
	}

	@Override
	public IRasterModel getModel(final String id) {
		return models.get(id);
	}

	@Override
	public Collection<IRasterModel> getModels() {
		return Collections.unmodifiableCollection(models.values());
	}

	@Override
	public Collection<String> getModelIds() {
		return Collections.unmodifiableCollection(models.keySet());
	}

	@Override
	public IRasterGranularity<T> getGranularity() {
		return granularity;
	}

	/**
	 * Sets the <code>Locale</code> based on a string
	 * 
	 * @param locale
	 *            the <code>Locale</code> to be used
	 */
	public void setLocale(final String locale) {
		setLocale(Locales.getLocale(locale));
	}

	/**
	 * Sets the <code>Locale</code> settings, if <code>null</code> is passed,
	 * the default <code>Locale</code> will be used
	 * 
	 * @param locale
	 *            the <code>Locale</code> to be used
	 * 
	 * @see Locale#getDefault()
	 */
	public void setLocale(final Locale locale) {

		if (locale == null) {
			this.locale = Locale.getDefault();
		} else {
			this.locale = locale;
		}

		// notify
		setChanged();
		notifyObservers(new Event(RasterConfigurationEvents.LOCALECHANGED,
				locale));
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public IRasterLogic<T> getLogic() {
		return logic;
	}

	@Override
	public void update(final Observable o, final Object object) {

		if (object instanceof Event && o instanceof IRasterModel) {
			final IRasterModel model = (IRasterModel) o;

			// look for the model
			String name = null;
			for (final Entry<String, IRasterModel> entry : models.entrySet()) {
				if (model == entry.getValue()) {
					name = entry.getKey();
				}
			}

			// notify
			if (name != null) {
				setChanged();
				notifyObservers(new Event(
						RasterConfigurationEvents.MODELCHANGED, name));
			}
		}
	}
}
