package net.meisen.general.genmisc.raster.configuration;

import java.util.Collection;
import java.util.Locale;

import net.meisen.general.genmisc.raster.definition.IRasterGranularity;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModel;


/**
 * This interface defines the configuration of a <code>Raster</code> or better
 * the functionality needed by a raster configuration
 * 
 * @author pmeisen
 * 
 * @param <T>
 *            the type which is rastered by the <code>Raster</code>
 */
public interface IRasterConfiguration<T> {

	/**
	 * This method is used to retrieve a <code>RasterModel</code> associated to
	 * the passed <code>id</code>
	 * 
	 * @param id
	 *            the identifier of the <code>RasterModel</code> to be retrieved
	 * 
	 * @return the <code>RasterModel</code> associated with the passed
	 *         identifier, <code>null</code> if no <code>RasterModel</code> is
	 *         associated to the identifier
	 */
	public IRasterModel getModel(final String id);

	/**
	 * Returns the defined <code>RasterGranularity</code> for the
	 * <code>Raster</code>.
	 * 
	 * @return the defined <code>RasterGranularity</code> for the
	 *         <code>Raster</code>
	 */
	public IRasterGranularity<T> getGranularity();

	/**
	 * The <code>RasterLogic</code> to be used for the <code>Raster</code>
	 * 
	 * @return the <code>RasterLogic</code> to be used for the
	 *         <code>Raster</code>
	 */
	public IRasterLogic<T> getLogic();

	/**
	 * Retrieves an unmodifiable <code>Collection</code> of the
	 * <code>RasterModels</code> of this <code>RasterConfiguration</code>
	 * 
	 * @return a <code>Collection</code> of the <code>RasterModels</code> of
	 *         this <code>RasterConfiguration</code>
	 */
	public Collection<IRasterModel> getModels();

	/**
	 * Retrieves an unmodifiable <code>Collection</code> of the identifier of
	 * this <code>RasterConfiguration</code>
	 * 
	 * @return a <code>Collection</code> of the identifiers of this
	 *         <code>RasterConfiguration</code>
	 */
	public Collection<String> getModelIds();

	/**
	 * This method is used to determine the <code>Locale</code> to be used for
	 * the <code>Raster</code> creation. This method should never return
	 * <code>null</code>.
	 * 
	 * @return the <code>Locale</code> to be used, should never return
	 *         <code>null</code>
	 */
	public Locale getLocale();
}
