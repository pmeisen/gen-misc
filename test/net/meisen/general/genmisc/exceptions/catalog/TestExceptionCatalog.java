package net.meisen.general.genmisc.exceptions.catalog;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.InputStream;
import java.util.Properties;

import org.junit.Test;

/**
 * Tests the implementation of the <code>ExceptionCatalog</code>.
 * 
 * @author pmeisen
 * 
 */
public class TestExceptionCatalog {

	/**
	 * Tests the creation of an empty <code>ExceptionCatalog</code>.
	 */
	@Test
	public void testEmptyCatalog() {
		final DefaultExceptionCatalog catalog = new DefaultExceptionCatalog();
		assertEquals(0, catalog.size());
	}

	/**
	 * Tests the creation of an <code>ExceptionCatalog</code> based on
	 * <code>Properties</code>.
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if one of the defined properties is invalid (i.e. the key is not
	 *           an integer)
	 */
	@Test
	public void testCreationByProperties() throws InvalidCatalogEntryException {
		final Properties properties = new Properties();
		properties.setProperty("1000", "Invalid User");
		properties.setProperty("1001", "Invalid Password");
		properties.setProperty("1002", "Invalid Session");
		properties.setProperty("1003", "Special chars äöü");

		final DefaultExceptionCatalog catalog = new DefaultExceptionCatalog(properties, null);
		assertEquals("Invalid User", catalog.getMessage(1000));
		assertEquals("Invalid Password", catalog.getMessage(1001));
		assertEquals("Invalid Session", catalog.getMessage(1002));
		assertEquals("Special chars äöü", catalog.getMessage(1003));
		assertNull(catalog.getMessage(2000));
	}

	/**
	 * Tests the creation of an <code>ExceptionCatalog</code> based on
	 * <code>CatalogEntry</code> instances.
	 */
	@Test
	public void testCreationByEntries() {
		final DefaultExceptionCatalog catalog = new DefaultExceptionCatalog(new CatalogEntry(
				1000, "Invalid User"), new CatalogEntry(1001, "Invalid Password"));

		assertEquals("Invalid User", catalog.getMessage(1000));
		assertEquals("Invalid Password", catalog.getMessage(1001));
		assertNull(catalog.getMessage(2000));
	}

	/**
	 * Tests the creation of an <code>ExceptionCatalog</code> based on a
	 * <code>InputStream</code>, which is ISO encoded.
	 * 
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if the stream cannot be read or if a value in the stream is
	 *           invalid
	 */
	@Test
	public void testCreationByISOStream() throws InvalidCatalogEntryException {
		final InputStream stream = getClass().getResourceAsStream(
				"catalog-test-ISO.properties");
		final DefaultExceptionCatalog catalog = new DefaultExceptionCatalog(stream);

		assertEquals("Ungültiger Benutzer", catalog.getMessage(1000));
		assertEquals("Ungültiges Passwort", catalog.getMessage(1001));
		assertEquals("Ungültige Session", catalog.getMessage(1002));
		assertNull(catalog.getMessage(2000));
	}

	/**
	 * Tests the creation of an <code>ExceptionCatalog</code> based on a
	 * <code>InputStream</code>, which is UTF-8 encoded.
	 * 
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if the stream cannot be read or if a value in the stream is
	 *           invalid
	 */
	@Test
	public void testCreationByUTF8Stream() throws InvalidCatalogEntryException {
		final InputStream stream = getClass().getResourceAsStream(
				"catalog-test-UTF8.properties");
		final DefaultExceptionCatalog catalog = new DefaultExceptionCatalog(stream);

		assertEquals("Ungültiger Benutzer", catalog.getMessage(1000));
		assertEquals("Ungültiges Passwort", catalog.getMessage(1001));
		assertEquals("Ungültige Session", catalog.getMessage(1002));
		assertNull(catalog.getMessage(2000));
	}
}
