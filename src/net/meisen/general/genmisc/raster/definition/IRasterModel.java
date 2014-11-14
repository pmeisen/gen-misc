package net.meisen.general.genmisc.raster.definition;

import java.util.Collection;

import net.meisen.general.genmisc.raster.condition.IRasterModelCondition;
import net.meisen.general.genmisc.raster.condition.RasterModelConditions;
import net.meisen.general.genmisc.raster.condition.impl.Tautology;


/**
 * Model which describes which data can be excepted in the
 * <code>ModelData</code>
 * 
 * @author pmeisen
 * 
 */
public interface IRasterModel {

	/**
	 * The <code>RasterModelEntry</code> specified for the passed
	 * <code>name</code>.
	 * 
	 * @param name
	 *            the <code>name</code> to get the <code>RasterModelEntry</code>
	 *            for
	 * @return the <code>RasterModelEntry</code> associated with the name,
	 *         <code>null</code> if no entry is associated with the passed
	 *         <code>name</code>.
	 */
	public IRasterModelEntry getEntry(final String name);

	/**
	 * The amount of entries defined for the <code>RasterModelModel</code>
	 * 
	 * @return amount of entries defined for the <code>RasterModelModel</code>
	 */
	public int getSize();

	/**
	 * A unmodifiable <code>Collection</code> of all the specified
	 * <code>RasterModelEntries</code>.
	 * 
	 * @return a unmodifiable <code>Collection</code> of all the specified
	 *         <code>RasterModelEntries</code>
	 */
	public Collection<IRasterModelEntry> getEntries();

	/**
	 * A <code>Collection</code> of all the specified
	 * <code>RasterModelEntries</code> of the specified
	 * <code>RasterModelEntryTypies</code>
	 * 
	 * @param entryTypies
	 *            the <code>RasterModelEntryTypies</code> of the
	 *            <code>RasterModelEntries</code> to be retrieved
	 * @return all the <code>RasterModelEntries</code> of the specified
	 *         <code>RasterModelEntryTypies</code> of this
	 *         <code>RasterModel</code>
	 * 
	 * @see RasterModelEntryType
	 */
	public Collection<IRasterModelEntry> getEntries(
			final RasterModelEntryType... entryTypies);

	/**
	 * A unmodifiable <code>Collection</code> of all the names of the specified
	 * <code>RasterModelEntries</code>.
	 * 
	 * @return a unmodifiable <code>Collection</code> of all the names of the
	 *         specified <code>RasterModelEntries</code>
	 */
	public Collection<String> getNames();

	/**
	 * The defined <code>RasterModelEntry</code> which specifies the start of
	 * the interval. This <code>RasterModelEntry</code> is always of type
	 * {@link RasterModelEntryType#INTERVALSTART}.
	 * 
	 * @return the <code>RasterModelEntry</code> which specifies the start of
	 *         the interval
	 */
	public IRasterModelEntry getIntervalStartEntry();

	/**
	 * The defined <code>RasterModelEntry</code> which specifies the end of the
	 * interval. This <code>RasterModelEntry</code> is always of type
	 * {@link RasterModelEntryType#INTERVALEND}.
	 * 
	 * @return the <code>RasterModelEntry</code> which specifies the end of the
	 *         interval
	 */
	public IRasterModelEntry getIntervalEndEntry();

	/**
	 * Sets the <code>RasterModelCondition</code> used to determine if a
	 * <code>ModelData</code> instance should be applied to the
	 * <code>Raster</code>, defined by this <code>RasterModel</code>. This
	 * function <b>should never return <code>null</code></b>, use i.e. a
	 * tautology as default condition.
	 * 
	 * @return the <code>RasterModelCondition</code> used to determine if a
	 *         <code>ModelData</code> instance should be applied to the
	 *         <code>Raster</code>, defined by this <code>RasterModel</code>
	 * 
	 * @see IRasterModelCondition
	 * @see Tautology
	 * @see RasterModelConditions#TAUTOLOGY
	 */
	public IRasterModelCondition getCondition();
}
