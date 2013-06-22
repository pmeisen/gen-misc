package net.meisen.general.genmisc.exceptions.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Locale;

import org.junit.Test;

public class TestLocalizedExceptionCatalog {

	@Test
	public void testNamingWithProperties() throws InvalidCatalogEntryException {
		final LocalizedExceptionCatalog exceptionCatalog = new LocalizedExceptionCatalog(
				"testExceptions.properties");

		assertNotNull(exceptionCatalog);
		checkCatalog(exceptionCatalog);
	}

	@Test
	public void testNamingWithoutProperties() throws InvalidCatalogEntryException {
		final LocalizedExceptionCatalog exceptionCatalog = new LocalizedExceptionCatalog(
				"testExceptions");
		assertNotNull(exceptionCatalog);
		checkCatalog(exceptionCatalog);
	}

	public void checkCatalog(final LocalizedExceptionCatalog catalog) {
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
