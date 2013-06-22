package net.meisen.general.genmisc.exceptions.catalog;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.meisen.general.genmisc.resources.Resource;
import net.meisen.general.genmisc.resources.ResourceInfo;
import net.meisen.general.genmisc.types.Objects;

/**
 * This is an <code>ExceptionCatalog</code> which can keep different messages
 * for different <code>Locale</code> instances. The catalog is loaded via a so
 * called bundle, i.e. text-files ending with a language and optionally with a
 * country code, e.g.: myBundle_de_DE.properties for the <code>Locale</code>
 * with language German and the country Germany. There should be exactly one
 * default property file, without any language or country code, e.g.
 * myBundle.properties. This file is used whenever there is no information found
 * for the specified <code>Locale</code> (i.e. the default). The resolving order
 * for a number and a <code>Locale</code> is as following (
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
 * @see ExceptionCatalog
 * 
 */
public class LocalizedExceptionCatalog implements Serializable {
	private static final long serialVersionUID = -6074214052182861862L;

	private Map<Locale, ExceptionCatalog> localizedCatalogs = new HashMap<Locale, ExceptionCatalog>();

	/**
	 * Default constructor generates an empty catalog
	 */
	public LocalizedExceptionCatalog() {
		// nothing to do
	}

	/**
	 * Constructor which specifies the bundle to be used.
	 * 
	 * @param bundle
	 *          the bundle to be used as resource for the
	 *          <code>LocalizedExceptionCatalog</code>
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if one of the entries of the catalog is invalid
	 */
	public LocalizedExceptionCatalog(final String bundle)
			throws InvalidCatalogEntryException {
		setBundle(bundle);
	}

	/**
	 * Sets the bundle to be used.
	 * 
	 * @param bundle
	 *          the bundle to be used as resource for the
	 *          <code>LocalizedExceptionCatalog</code>
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if one of the entries of the catalog is invalid
	 */
	public void setBundle(final String bundle)
			throws InvalidCatalogEntryException {

		// remove everything we have so far
		localizedCatalogs.clear();

		// remove a .properties from the bundles name
		String validBundleName;
		if (bundle.toLowerCase().endsWith(".properties")) {
			validBundleName = bundle.replaceFirst("(?i)\\.properties$", "");
		} else {
			validBundleName = bundle;
		}

		// create the pattern for the bundle's resources
		final String regEx = ".*"
				+ Resource.transformFileNameIntoValidRegEx(validBundleName)
				+ "(?:\\_([a-z]{2})(?:\\_([A-Z]{2}))?)?\\.properties$";
		final Pattern pattern = Pattern.compile(regEx);

		// get the resources
		final Collection<ResourceInfo> resInfos = Resource.getResources(pattern,
				true, false);

		// get through the resources we found
		for (final ResourceInfo resInfo : resInfos) {

			// get the resource
			final InputStream stream = Resource.getResourceAsStream(resInfo);

			// create the catalog
			final ExceptionCatalog catalog = new ExceptionCatalog(stream);

			// add the locale setting to the map for the catalog
			final String fullPath = resInfo.getFullPath();
			final Matcher matcher = pattern.matcher(fullPath);
			matcher.find();

			// get the language and country from the match
			final String language = matcher.group(1);
			final String country = matcher.group(2);

			// finally get the locale
			final Locale locale;
			if (language == null) {
				locale = null;
			} else if (country == null) {
				locale = new Locale(language);
			} else {
				locale = new Locale(language, country);
			}

			// add it
			localizedCatalogs.put(locale, catalog);
		}
	}

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
	public String getMessage(final Integer number, final Locale locale) {

		// check what we have to validate
		boolean useDefault = Objects.empty(locale);
		boolean useLanguage = !useDefault && Objects.empty(locale.getCountry());
		boolean useCountry = !useDefault && !useLanguage;

		String message = null;
		if (useCountry) {
			final Locale l = new Locale(locale.getLanguage(), locale.getCountry());
			final ExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);

			// check if we got a message, otherwise we have to check the language
			if (message == null) {
				useLanguage = true;
			}
		}

		if (useLanguage) {
			final Locale l = new Locale(locale.getLanguage());
			final ExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);

			// check if we got a message, otherwise we have to check the default
			if (message == null) {
				useDefault = true;
			}
		}

		if (useDefault) {
			final Locale l = null;
			final ExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);
		}

		return message;
	}
}
