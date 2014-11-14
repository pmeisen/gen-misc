package net.meisen.general.genmisc.raster.definition;

/**
 * This enumeration defines the different types of a
 * <code>RasterModelEntry</code>
 * 
 * @author pmeisen
 * 
 */
public enum RasterModelEntryType {
	/**
	 * The <code>ModelEntry</code> is marked to be a value
	 */
	VALUE,
	/**
	 * The <code>ModelEntry</code> is marked to be a grouping value
	 */
	GROUP,
	/**
	 * The <code>ModelEntry</code> is the start of the interval
	 */
	INTERVALSTART,
	/**
	 * The <code>ModelEntry</code> is the end of the interval
	 */
	INTERVALEND;
}
