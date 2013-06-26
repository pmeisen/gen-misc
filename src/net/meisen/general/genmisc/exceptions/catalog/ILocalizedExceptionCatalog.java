package net.meisen.general.genmisc.exceptions.catalog;

import java.util.Locale;

/**
 * This is an interface for an <code>ExceptionCatalog</code> which can keep
 * different messages for different <code>Locale</code> instances. The catalog
 * is loaded via a so called bundle, i.e. text-files ending with a language and
 * optionally with a country code, e.g.: myBundle_de_DE.properties for the
 * <code>Locale</code> with language German and the country Germany. There
 * should be exactly one default property file, without any language or country
 * code, e.g. myBundle.properties. This file is used whenever there is no
 * information found for the specified <code>Locale</code> (i.e. the default).
 * The resolving order for a number and a <code>Locale</code> is as following (
 * {@link #getMessage(Integer, Locale)}):
 * <ol>
 * <li>check if there is a file within the bundle which represents the locale's
 * <b>language and country</b></li>
 * <li>if nothing was found, check if there is a file which represents the
 * <b>locale's language only</b></li>
 * <li>if nothing was found, check if the <b>default</b> can resolve a message
 * for the specified number</li>
 * <li>if nothing was found, return <code>null</code></li>
 * </ol>
 * 
 * @author pmeisen
 * 
 * @see Locale
 * @see DefaultExceptionCatalog
 * @see DefaultLocalizedExceptionCatalog
 * 
 */
public interface ILocalizedExceptionCatalog extends IExceptionCatalog,
		IBundledExceptionCatalog {

	/**
	 * Gets the message for the specified <code>number</code> in the specified
	 * <code>locale</code>.
	 * 
	 * @param number
	 *          the number to get the message for
	 * @param locale
	 *          the <code>Locale</code> to get the message for
	 * 
	 * @return the message found for the specified <code>number</code> and the
	 *         specified <code>locale</code>
	 */
	public String getMessage(final Integer number, final Locale locale);

	/**
	 * Sets the default <code>Locale</code> to be used, when no locale is
	 * specified and a message is retrieved from the <code>Catalog</code>.
	 * 
	 * @param locale
	 *          the <code>Locale</code> to be used as default
	 */
	public void setDefaultLocale(final Locale locale);
}
