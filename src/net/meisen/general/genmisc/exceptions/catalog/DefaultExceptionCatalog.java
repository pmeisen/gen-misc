package net.meisen.general.genmisc.exceptions.catalog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Collections;
import java.util.Properties;
import java.util.Set;

import net.meisen.general.genmisc.types.Files;
import net.meisen.general.genmisc.types.Streams;

/**
 * An <code>ExceptionCatalog</code> is a catalog which stores messages for
 * exceptions associated to a specific integer value, i.e. 1000 is associated
 * with the message "Invalid user".
 * 
 * @author pmeisen
 * 
 */
public class DefaultExceptionCatalog extends HashMap<Integer, String> implements
		IExceptionCatalog {
	private static final long serialVersionUID = -199470873362845992L;

	/**
	 * Default constructor creates an empty catalog.
	 */
	public DefaultExceptionCatalog() {
		// creates an empty catalog
	}

	/**
	 * Constructor to create the catalog based on <code>Properties</code>.
	 * 
	 * @param properties
	 *            the <code>Properties</code> to be read
	 * @param encoding
	 *            the encoding of the properties, if not set the default is used
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if an entry of the <code>properties</code> is invalid, i.e.
	 *             mostly means that one of the keys cannot be interpreted as an
	 *             integer
	 * 
	 * @see Properties
	 * @see #addEntries(Properties, String)
	 */
	public DefaultExceptionCatalog(final Properties properties,
			final String encoding) throws InvalidCatalogEntryException {
		addEntries(properties, encoding);
	}

	/**
	 * Creates a catalog based on the specified <code>entries</code>.
	 * 
	 * @param entries
	 *            an array of <code>CatalogEntry</code> instances, which should
	 *            be added to the catalog
	 * 
	 * @see CatalogEntry
	 * @see #addEntries(CatalogEntry...)
	 */
	public DefaultExceptionCatalog(final CatalogEntry... entries) {
		addEntries(entries);
	}

	/**
	 * Creates a catalog based on the passed <code>stream</code>.
	 * 
	 * @param stream
	 *            the stream to retrieve the entries from
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if the stream cannot be read or contains invalid entries
	 * 
	 * @see #addEntries(InputStream)
	 */
	public DefaultExceptionCatalog(final InputStream stream)
			throws InvalidCatalogEntryException {
		addEntries(stream);
	}

	/**
	 * Creates a catalog based on the passed <code>File</code> (which has to be
	 * a property file, i.e. a file with key-value-pairs).
	 * 
	 * @param propertyFile
	 *            the <code>File</code> to read the properties from
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if the file contains invalid entries, i.e. if the key of a
	 *             property cannot be interpreted as an integer
	 */
	public void addEntries(final File propertyFile)
			throws InvalidCatalogEntryException {
		try {
			final FileInputStream fis = new FileInputStream(propertyFile);
			addEntries(fis);
			Streams.closeIO(fis);
		} catch (final FileNotFoundException e) {
			throw new InvalidCatalogEntryException(
					"The specified propertyFile '"
							+ Files.getCanonicalPath(propertyFile)
							+ "' could not be found.");
		}
	}

	/**
	 * Adds the entries of the passed <code>stream</code> to the catalog.
	 * 
	 * @param stream
	 *            the stream to retrieve the entries from
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if the stream cannot be read or contains invalid entries
	 */
	public void addEntries(final InputStream stream)
			throws InvalidCatalogEntryException {
		final Properties properties = new Properties();

		try {
			// we have to read the stream multiple tymes so get it into a
			// byte-array
			final byte[] bytes = Streams.copyStreamToByteArray(stream);

			// load the properties
			properties.load(new ByteArrayInputStream(bytes));

			// guess the encoding of the stream
			final String encoding = Streams.guessEncoding(
					new ByteArrayInputStream(bytes), null);

			// add the properties with the encoding we determined
			addEntries(properties, encoding);
		} catch (final IOException e) {
			throw new InvalidCatalogEntryException(
					"Could not read the entries from InputStream.");
		}
	}

	/**
	 * Adds the entries of the passed <code>properties</code> to the catalog.
	 * The <code>encoding</code> of the properties might be specified, if not
	 * the default encoding is used.
	 * 
	 * @param properties
	 *            the <code>properties</code> to be added, cannot be
	 *            <code>null</code>
	 * @param encoding
	 *            the encoding used to read the properties, can be
	 *            <code>null</code>
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if one of the properties' key cannot be interpreted as
	 *             integer
	 */
	public void addEntries(final Properties properties, final String encoding)
			throws InvalidCatalogEntryException {
		final String usedEncoding = encoding == null ? System
				.getProperty("file.encoding") : encoding;

		for (final Entry<Object, Object> entry : properties.entrySet()) {
			final Integer key = getInt(entry.getKey());

			if (key == null) {
				continue;
			} else if (entry.getValue() == null) {
				addEntry((Integer) key, null);
			} else {
				final String value = entry.getValue().toString();
				try {
					final String msg = new String(value.getBytes(),
							usedEncoding);
					addEntry((Integer) key, msg);
				} catch (final UnsupportedEncodingException e) {
					// will never happen
				}
			}
		}
	}

	/**
	 * Method to transform the passed key into a valid integer
	 * 
	 * @param key
	 *            the key to be transformed
	 * 
	 * @return the integer retrieved from the key
	 * 
	 * @throws InvalidCatalogEntryException
	 *             if the key cannot be transformed
	 */
	protected Integer getInt(final Object key)
			throws InvalidCatalogEntryException {
		if (key == null) {
			return null;
		} else if (key instanceof String) {
			try {
				return Integer.parseInt((String) key);
			} catch (final NumberFormatException e) {
				throw new InvalidCatalogEntryException(
						"The key '"
								+ key
								+ "' cannot be interpreted as integer and therefore cannot be used as key for the catalog.");
			}
		} else if (key instanceof Integer) {
			return (Integer) key;
		} else {
			throw new InvalidCatalogEntryException(
					"The key '"
							+ key
							+ "' cannot be interpreted as integer and therefore cannot be used as key for the catalog  ('"
							+ key.getClass() + "').");
		}
	}

	/**
	 * Adds the passed <code>entries</code> to the catalog.
	 * 
	 * @param entries
	 *            the <code>CatalogEntry</code> instances to be added
	 */
	public void addEntries(final CatalogEntry... entries) {
		if (entries == null) {
			// nothing to do
		} else {

			// add all the entries
			for (final CatalogEntry entry : entries) {
				addEntry(entry);
			}
		}
	}

	/**
	 * Adds the passed <code>entry</code> to the catalog.
	 * 
	 * @param entry
	 *            the <code>CatalogEntry</code> instance to be added
	 */
	public void addEntry(final CatalogEntry entry) {
		addEntry(entry.getKey(), entry.getValue());
	}

	/**
	 * Adds the specified entry to the catalog.
	 * 
	 * @param number
	 *            the number of the entry
	 * @param message
	 *            the message associated to the passed <code>number</code>
	 */
	public void addEntry(final Integer number, final String message) {
		put(number, message);
	}

	@Override
	public String getMessage(final Integer number) {
		return get(number);
	}

	@Override
	public Set<Integer> getAvailableExceptions() {
		return Collections.unmodifiableSet(keySet());
	}
}
