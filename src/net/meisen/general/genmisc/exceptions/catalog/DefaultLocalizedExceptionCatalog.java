package net.meisen.general.genmisc.exceptions.catalog;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
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
 * @see DefaultExceptionCatalog
 * 
 */
public class DefaultLocalizedExceptionCatalog implements
		ILocalizedExceptionCatalog, Serializable {
	private static final long serialVersionUID = -6074214052182861862L;

	private Map<Locale, IExceptionCatalog> localizedCatalogs = new HashMap<Locale, IExceptionCatalog>();

	private Set<Integer> sortedKeySet = new TreeSet<Integer>();

	private Locale defLocale = null;

	/**
	 * Default constructor generates an empty catalog
	 */
	public DefaultLocalizedExceptionCatalog() {
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
	public DefaultLocalizedExceptionCatalog(final String bundle)
			throws InvalidCatalogEntryException {
		setBundle(bundle);
	}

	@Override
	public void loadBundle(final String bundle)
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
		sortedKeySet.clear();

		// remove a .properties from the bundles name
		String validBundleName;
		if (bundle.toLowerCase().endsWith(".properties")) {
			validBundleName = bundle.replaceFirst("(?i)\\.properties$", "");
		} else {
			validBundleName = bundle;
		}

		// create the pattern for the bundle's resources
		final String regExLocale = "(?:\\_([a-z]{2})(?:\\_([A-Z]{2}))?)?\\.properties$";
		final String regExClasspath = ".*"
				+ Resource.transformFileNameIntoValidRegEx(validBundleName)
				+ regExLocale;
		final Pattern patternClasspath = Pattern.compile(regExClasspath);
		final Pattern patternLocale = Pattern.compile(regExLocale);

		// get the resources
		final Collection<ResourceInfo> resInfos = Resource.getResources(
				patternClasspath, true, false);

		// get through the resources we found
		for (final ResourceInfo resInfo : resInfos) {

			// get the resource
			final InputStream stream = Resource.getResourceAsStream(resInfo);

			// create the catalog
			final IExceptionCatalog catalog = createNewCatalog(stream);

			// add the locale setting to the map for the catalog
			final String fullPath = resInfo.getFullPath();
			final Matcher matcher = patternLocale.matcher(fullPath);

			// if no matcher is found skip it
			if (!matcher.find()) {
				continue;
			}

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
			sortedKeySet.addAll(catalog.getAvailableExceptions());
		}
	}

	/**
	 * Method to create the <code>ExceptionCatalog</code> which is internally used
	 * to store all the exceptions defined for one <code>Locale</code>.
	 * 
	 * @param stream
	 *          the <code>InputStream</code> of the <code>Locale</code>'s property
	 *          file to load the <code>ExceptionCatalog</code> from
	 * 
	 * @return the created new catalog
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if the properties cannot be read by the
	 *           <code>ExceptionCatalog</code>
	 */
	public IExceptionCatalog createNewCatalog(final InputStream stream)
			throws InvalidCatalogEntryException {
		return new DefaultExceptionCatalog(stream);
	}

	@Override
	public String getMessage(final Integer number, final Locale locale) {

		// check if there is a message defined for the number
		if (!sortedKeySet.contains(number)) {
			return null;
		}

		// check what we have to validate
		boolean useDefault = Objects.empty(locale);
		boolean useLanguage = !useDefault && Objects.empty(locale.getCountry());
		boolean useCountry = !useDefault && !useLanguage;

		String message = null;
		if (useCountry) {
			final Locale l = new Locale(locale.getLanguage(), locale.getCountry());
			final IExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);

			// check if we got a message, otherwise we have to check the language
			if (message == null) {
				useLanguage = true;
			}
		}

		if (useLanguage) {
			final Locale l = new Locale(locale.getLanguage());
			final IExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);

			// check if we got a message, otherwise we have to check the default
			if (message == null) {
				useDefault = true;
			}
		}

		if (useDefault) {
			final Locale l = null;
			final IExceptionCatalog catalog = localizedCatalogs.get(l);
			message = catalog == null ? null : catalog.getMessage(number);
		}

		return message;
	}

	@Override
	public String getMessage(final Integer number) {

		// get the default locale of the VM
		final Locale locale = defLocale == null ? Locale.getDefault() : defLocale;

		// get the message for the locale
		return getMessage(number, locale);
	}

	@Override
	public void setDefaultLocale(final Locale locale) {
		this.defLocale = locale;
	}

	@Override
	public Set<Integer> getAvailableExceptions() {
		return Collections.unmodifiableSet(sortedKeySet);
	}
}
