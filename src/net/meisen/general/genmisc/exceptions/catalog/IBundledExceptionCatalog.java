package net.meisen.general.genmisc.exceptions.catalog;

/**
 * Defines an <code>ExceptionCatalog</code> which is bundled via a bundle name,
 * i.e. property-files which have different extensions for different
 * <code>Locale</code> instances.<br/>
 * <br/>
 * <b>Example of a bundle:</b>
 * <ul>
 * <li>myExceptions.properties</li>
 * <li>myExceptions_de.properties</li>
 * <li>myExceptions_en.properties</li>
 * <li>myExceptions_fr.properties</li>
 * </ul>
 * 
 * 
 * 
 * @author pmeisen
 * 
 */
public interface IBundledExceptionCatalog {

	/**
	 * Loads the exceptions which are defined by the bundle. All other exceptions
	 * are removed prior to loading the bundle. If the bundle points to no files
	 * nothing is loaded.
	 * 
	 * @param bundle
	 *          the bundle to be loaded
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if the bundle contains invalid definitions
	 */
	public void loadBundle(final String bundle)
			throws InvalidCatalogEntryException;
}
