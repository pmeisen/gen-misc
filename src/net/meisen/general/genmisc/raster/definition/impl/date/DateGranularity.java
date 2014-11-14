package net.meisen.general.genmisc.raster.definition.impl.date;

/**
 * The enumeration of all available kind of <code>Granularity</code> for
 * <code>Dates</code>
 * 
 * @author pmeisen
 */
public enum DateGranularity {

	/**
	 * The raster will be on a minute level, with a specified bucket size (e.g.
	 * 00:00 - 00:05, 00:05 - 00:10, ...)
	 */
	MINUTES("MINUTES"),
	/**
	 * The raster will be on day level (e.g. Monday, Tuesday, ...)
	 */
	DAYS("DAYS"),
	/**
	 * The raster will be on week level (e.g. CW1, CW2, ...)
	 */
	WEEKS("WEEKS"),
	/**
	 * The raster will be on month level (e.g. January, February, ...)
	 */
	MONTHS("MONTHS");

	/**
	 * @return <code>true</code> if the granularity needs a additional
	 *         parameter, the bucket size, otherwise <code>false</code>
	 */
	public boolean needsBucketSize() {
		return MINUTES.equals(this);
	}

	/**
	 * The name as String of the enumerator
	 */
	private final String name;

	/**
	 * @return The name of the enumerator as String
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name
	 *            The name of the enumerator as String
	 */
	private DateGranularity(final String name) {
		this.name = name.toUpperCase();
	}

	/**
	 * The function checks if an enumeration contains a value with the passed
	 * String
	 * 
	 * @param name
	 *            The name to check for
	 * @return <b>null</b>, if no enumerator with the name
	 *         exist</br><b>otherwise</b> the enumerator
	 * 
	 */
	public final static DateGranularity isValid(final String name) {
		for (final DateGranularity valid : DateGranularity.values())
			if (valid.getName().equalsIgnoreCase(name))
				return valid;
		return null;
	}

	/**
	 * Get the minimal value for this <code>DateGranularity</code>
	 * 
	 * @return the minimal value for this <code>DateGranularity</code>
	 */
	public Integer min() {

		// decide for raster size
		if (DateGranularity.MINUTES.equals(this)) {
			return 0;
		} else if (DateGranularity.DAYS.equals(this)) {
			return 1;
		} else if (DateGranularity.WEEKS.equals(this)) {
			return 1;
		} else if (DateGranularity.MONTHS.equals(this)) {
			return 0;
		} else {
			return null;
		}
	}

	/**
	 * Get the maximal value for this <code>DateGranularity</code>
	 * 
	 * @return the maximal value for this <code>DateGranularity</code>
	 */
	public Integer max() {

		// decide for raster size
		if (DateGranularity.MINUTES.equals(this)) {
			return 1439;
		} else if (DateGranularity.DAYS.equals(this)) {
			return 7;
		} else if (DateGranularity.WEEKS.equals(this)) {
			return 52;
		} else if (DateGranularity.MONTHS.equals(this)) {
			return 11;
		} else {
			return null;
		}
	}
}
