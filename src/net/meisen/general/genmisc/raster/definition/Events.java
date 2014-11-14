package net.meisen.general.genmisc.raster.definition;

/**
 * A collection of all the Events currently supported and used by the
 * implementations
 * 
 * @author pmeisen
 * 
 */
public class Events {

	/**
	 * Events fired by <code>RasterModels</code>
	 * 
	 * @author pmeisen
	 * 
	 */
	public static class RasterModelEvents extends Events {
		/**
		 * Fired when an entry was added. The <code>Event</code> contains the
		 * added <code>RasterModelEntry</code>.
		 */
		public static final int ENTRYADDED = 1;
		/**
		 * Fired when the whole model was cleared. The <code>Event</code> has no
		 * further information attached (i.e. object is <code>null</code>)
		 */
		public static final int MODELCLEARED = 2;
	}

	/**
	 * Events fired by <code>RasterConfigurations</code>
	 * 
	 * @author pmeisen
	 * 
	 */
	public static class RasterConfigurationEvents extends Events {
		/**
		 * Fired when a new <code>RasterModel</code> was added to the
		 * <code>RasterConfiguration</code>. The <code>Event</code> contains the
		 * name of the added <code>RasterModel</code>.
		 */
		public static final int MODELADDED = 1;
		/**
		 * Fired when a the <code>RasterModel</code> was changed, i.e. an entry
		 * was added. The <code>Event</code> contains the name of the changed
		 * <code>RasterModel</code>.
		 */
		public static final int MODELCHANGED = 2;
		/**
		 * Fired when the <code>Locale</code> was changed. The
		 * <code>Event</code> contains the added <code>Locale</code>.
		 */
		public static final int LOCALECHANGED = 3;
	}

	/**
	 * A <code>Event</code> is an instance passed whenever an event occurs. An
	 * <code>Event</code> has its type (i.e. an indicator of the event-type) and
	 * the modified object.<br/>
	 * <br/>
	 * 
	 * @see RasterModelEvents
	 * @see RasterConfigurationEvents
	 * 
	 * @author pmeisen
	 * 
	 */
	public static class Event {

		private final int type;
		private final Object object;

		/**
		 * Default constructor
		 * 
		 * @param type
		 *            the type of the <code>Event</code>, which differs by the
		 *            event
		 * @param object
		 *            the object attached to the <code>Event</code>
		 * 
		 * @see RasterModelEvents
		 * @see RasterConfigurationEvents
		 */
		public Event(final int type, final Object object) {
			this.type = type;
			this.object = object;
		}

		/**
		 * Checks if the <code>Event</code> is equal to the passed type
		 * 
		 * @param type
		 *            the type of the <code>Event</code> to be checked
		 * @return <code>true</code> if the <code>type</code> is equal
		 */
		public boolean isType(final int type) {
			return this.type == type;
		}

		/**
		 * Returns the attached object
		 * 
		 * @return the object attached to the <code>Event</code>
		 */
		@SuppressWarnings("unchecked")
		public <T> T getObject() {
			return (T) object;
		}
	}
}
