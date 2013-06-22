package net.meisen.general.genmisc.exceptions.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

public class TestExceptionCatalog {

	@Test
	public void testEmptyCatalog() {
		final ExceptionCatalog catalog = new ExceptionCatalog();
		assertEquals(0, catalog.size());
	}

	@Test
	public void testCreationByProperties() throws InvalidCatalogEntryException {
		final Properties properties = new Properties();
		properties.setProperty("1000", "Invalid User");
		properties.setProperty("1001", "Invalid Password");
		properties.setProperty("1002", "Invalid Session");
		properties.setProperty("1003", "Special chars äöü");

		final ExceptionCatalog catalog = new ExceptionCatalog(properties, null);
		assertEquals("Invalid User", catalog.getMessage(1000));
		assertEquals("Invalid Password", catalog.getMessage(1001));
		assertEquals("Invalid Session", catalog.getMessage(1002));
		assertEquals("Special chars äöü", catalog.getMessage(1003));
		assertNull(catalog.getMessage(2000));
	}

	@Test
	public void testCreationByEntries() throws InvalidCatalogEntryException {
		final ExceptionCatalog catalog = new ExceptionCatalog(new CatalogEntry(
				1000, "Invalid User"), new CatalogEntry(1001, "Invalid Password"));

		assertEquals("Invalid User", catalog.getMessage(1000));
		assertEquals("Invalid Password", catalog.getMessage(1001));
		assertNull(catalog.getMessage(2000));
	}

	@Test
	public void testCreationByISOStream() throws InvalidCatalogEntryException {
		final InputStream stream = getClass().getResourceAsStream(
				"catalog-test-ISO.properties");
		final ExceptionCatalog catalog = new ExceptionCatalog(stream);

		assertEquals("Ungültiger Benutzer", catalog.getMessage(1000));
		assertEquals("Ungültiges Passwort", catalog.getMessage(1001));
		assertEquals("Ungültige Session", catalog.getMessage(1002));
		assertNull(catalog.getMessage(2000));
	}

	@Test
	public void testCreationByUTF8Stream() throws InvalidCatalogEntryException {
		final InputStream stream = getClass().getResourceAsStream(
				"catalog-test-UTF8.properties");
		final ExceptionCatalog catalog = new ExceptionCatalog(stream);

		assertEquals("Ungültiger Benutzer", catalog.getMessage(1000));
		assertEquals("Ungültiges Passwort", catalog.getMessage(1001));
		assertEquals("Ungültige Session", catalog.getMessage(1002));
		assertNull(catalog.getMessage(2000));
	}
}
