package net.meisen.general.genmisc.resources;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.meisen.general.genmisc.types.Locales;

/**
 * Wrapper class to enable UTF-8 support for {@link ResourceBundle}
 * 
 * @author pmeisen
 * 
 */
public abstract class Utf8ResourceBundle {

	/**
	 * @param baseName
	 *            the baseName of the {@link ResourceBundle} to be returned
	 * @return the {@link ResourceBundle} with the specified
	 *         <code>baseName</code>
	 */
	public static final ResourceBundle getBundle(String baseName) {
		ResourceBundle bundle = null;

		try {
			bundle = ResourceBundle.getBundle(baseName);
		} catch (MissingResourceException e) {
			// TODO add logging
			bundle = null;
		}
		return createUtf8PropertyResourceBundle(bundle);
	}

	/**
	 * @param baseName
	 *            the baseName of the {@link ResourceBundle} to be returned
	 * @param locale
	 *            the {@link Locales} to be used for the {@link ResourceBundle}
	 * @return the {@link ResourceBundle} with the specified
	 *         <code>baseName</code>
	 */
	public static final ResourceBundle getBundle(String baseName, Locale locale) {
		ResourceBundle bundle = null;

		try {
			bundle = ResourceBundle.getBundle(baseName, locale);
		} catch (MissingResourceException e) {
			// TODO add logging
			bundle = null;
		}

		return createUtf8PropertyResourceBundle(bundle);
	}

	/**
	 * @param baseName
	 *            the baseName of the {@link ResourceBundle} to be returned
	 * @param locale
	 *            the {@link Locales} to be used for the {@link ResourceBundle}
	 * @param loader
	 *            the {@link ClassLoader} which identifies the
	 *            {@link ResourceBundle} to be loaded
	 * @return the {@link ResourceBundle} with the specified
	 *         <code>baseName</code>
	 */
	public static ResourceBundle getBundle(String baseName, Locale locale,
			ClassLoader loader) {
		ResourceBundle bundle = null;

		try {
			bundle = ResourceBundle.getBundle(baseName, locale, loader);
		} catch (MissingResourceException e) {
			// TODO add logging
			bundle = null;
		}

		return createUtf8PropertyResourceBundle(bundle);
	}

	private static ResourceBundle createUtf8PropertyResourceBundle(
			ResourceBundle bundle) {
		if (!(bundle instanceof PropertyResourceBundle))
			return bundle;

		return new Utf8PropertyResourceBundle((PropertyResourceBundle) bundle);
	}

	private static class Utf8PropertyResourceBundle extends ResourceBundle {
		PropertyResourceBundle bundle;

		protected Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
			this.bundle = bundle;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.ResourceBundle#getKeys()
		 */
		public Enumeration<String> getKeys() {
			return bundle.getKeys();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
		 */
		protected Object handleGetObject(String key) {
			String value = (String) bundle.getString(key);
			if (value == null)
				return null;
			try {
				return new String(value.getBytes("ISO-8859-1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// Shouldn't fail - but should we still add logging message?
				return null;
			}
		}
	}
}
