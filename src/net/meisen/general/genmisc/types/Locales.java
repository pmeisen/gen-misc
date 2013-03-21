package net.meisen.general.genmisc.types;

import java.util.Locale;

/**
 * Utility class for localizations
 * 
 * @author pmeisen
 * 
 */
public class Locales {

	/**
	 * 
	 * @param locale
	 *            the locale {@link String} as 2 by 2 code, language_country (or
	 *            only language)
	 * @return the {@link Locale} for this code
	 */
	public static Locale getLocale(final String locale) {
		if (locale != null) {
			final String[] split = locale.split("_");
			final String lang = (split.length > 0 ? split[0] : "");
			final String country = (split.length > 1 ? split[1] : "");

			return new Locale(lang, country, "");
		}
		return null;
	}
}
