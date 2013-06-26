package net.meisen.general.genmisc.exceptions.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

/**
 * Tests the implementation of a <code>LocalizedExceptionCatalog</code>.
 * 
 * @author pmeisen
 * 
 */
public class TestLocalizedExceptionCatalog {

	/**
	 * Tests the loading of a <code>LocalizedExceptionCatalog</code> using the
	 * <code>.properties</code> suffix and just the bundles name (i.e.
	 * <code>testExceptions.properties</code>).
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if an entry of the properties file is invalid
	 */
	@Test
	public void testNamingWithProperties() throws InvalidCatalogEntryException {
		final DefaultLocalizedExceptionCatalog exceptionCatalog = new DefaultLocalizedExceptionCatalog(
				"testExceptions.properties");

		assertNotNull(exceptionCatalog);
		checkCatalog(exceptionCatalog);
	}

	/**
	 * Tests the loading of a <code>LocalizedExceptionCatalog</code> using just
	 * the bundles name (i.e. <code>testExceptions.properties</code>).
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if an entry of the properties file is invalid
	 */
	@Test
	public void testNamingWithoutProperties() throws InvalidCatalogEntryException {
		final DefaultLocalizedExceptionCatalog exceptionCatalog = new DefaultLocalizedExceptionCatalog(
				"testExceptions");
		assertNotNull(exceptionCatalog);
		checkCatalog(exceptionCatalog);
	}

	/**
	 * Tests the loading of a <code>LocalizedExceptionCatalog</code> using just
	 * the bundles name (i.e. <code>testExceptions.properties</code>).
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if an entry of the properties file is invalid
	 */
	@Test
	public void testNamingWithFullPath() throws InvalidCatalogEntryException {
		final DefaultLocalizedExceptionCatalog exceptionCatalog = new DefaultLocalizedExceptionCatalog(
				"net/meisen/general/genmisc/exceptions/catalog/localizedCatalog/testExceptions");
		assertNotNull(exceptionCatalog);
		checkCatalog(exceptionCatalog);
	}

	/**
	 * Helper method which checks the passed <code>catalog</code> and the values
	 * of it against the expected values of the test-bundle.
	 * 
	 * @param catalog
	 *          the <code>LocalizedExceptionCatalog</code> to be tested
	 */
	public void checkCatalog(final DefaultLocalizedExceptionCatalog catalog) {
		Locale l;

		final Locale locale_de = new Locale("de");
		final Locale locale_de_DE = new Locale("de", "DE");
		final Locale locale_de_AT = new Locale("de", "AT");
		final Locale locale_en_US = new Locale("en", "US");
		final Locale locale_en = new Locale("en");
		final Locale locale_fr = new Locale("fr");

		// test the German dictionary
		l = locale_de;
		assertEquals("Dies ist der erste Testwert", catalog.getMessage(1000, l));
		assertEquals("Dies ist die zweite Nachricht", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));

		// test the German - Germany dictionary
		l = locale_de_DE;
		assertEquals("Dies ist der erste Testwert", catalog.getMessage(1000, l));
		assertEquals("Dies ist die zweite Nachricht", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));

		// test the German - Austria dictionary
		l = locale_de_AT;
		assertEquals("Jo mi leckst am Oasch!", catalog.getMessage(1000, l));
		assertEquals("Dies ist die zweite Nachricht", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));

		// test the English dictionary
		l = locale_en;
		assertEquals("This is the first sample value", catalog.getMessage(1000, l));
		assertEquals("This is the second value", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));

		// test the English - USA dictionary
		l = locale_en_US;
		assertEquals("This is the first sample value", catalog.getMessage(1000, l));
		assertEquals("This is the second value", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));

		// test the French dictionary
		l = locale_fr;
		assertEquals("Ca c'est le premier valeur", catalog.getMessage(1000, l));
		assertEquals("This is the second value", catalog.getMessage(1001, l));
		assertEquals("This is the last value", catalog.getMessage(1002, l));
		assertNull(catalog.getMessage(1003, l));
	}
}
