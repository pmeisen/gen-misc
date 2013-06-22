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
import java.util.Properties;

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
public class ExceptionCatalog extends HashMap<Integer, String> {
	private static final long serialVersionUID = -199470873362845992L;

	/**
	 * Default constructor creates an empty catalog.
	 */
	public ExceptionCatalog() {
		// creates an empty catalog
	}

	/**
	 * Constructor to create the catalog based on <code>Properties</code>.
	 * 
	 * @param properties
	 *          the <code>Properties</code> to be read
	 * @param encoding
	 *          the encoding of the properties, if not set the default is used
	 * 
	 * @throws InvalidCatalogEntryException
	 *           if an entry of the <code>properties</code> is invalid, i.e.
	 *           mostly means that one of the keys cannot be interpreted as an
	 *           integer
	 * 
	 * @see Properties
	 */
	public ExceptionCatalog(final Properties properties, final String encoding)
			throws InvalidCatalogEntryException {
		addEntries(properties, encoding);
	}

	public ExceptionCatalog(final CatalogEntry... entries) {
		addEntries(entries);
	}

	public ExceptionCatalog(final InputStream stream)
			throws InvalidCatalogEntryException {
		addEntries(stream);
	}

	public void addEntries(final File propertyFile)
			throws InvalidCatalogEntryException {
		try {
			addEntries(new FileInputStream(propertyFile));
		} catch (final FileNotFoundException e) {
			throw new InvalidCatalogEntryException("The specified propertyFile '"
					+ Files.getCanonicalPath(propertyFile) + "' could not be found.");
		}
	}

	public void addEntries(final InputStream stream)
			throws InvalidCatalogEntryException {
		final Properties properties = new Properties();

		try {
			// we have to read the stream multiple tymes so get it into a byte-array
			final byte[] bytes = Streams.copyStreamToByteArray(stream);

			// load the properties
			properties.load(new ByteArrayInputStream(bytes));

			// guess the encoding of the stream
			final String encoding = Streams.guessEncoding(new ByteArrayInputStream(
					bytes), null);

			// add the properties with the encoding we determined
			addEntries(properties, encoding);
		} catch (final IOException e) {
			throw new InvalidCatalogEntryException(
					"Could not read the entries from InputStream.");
		}
	}

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
					final String msg = new String(value.getBytes(), usedEncoding);
					addEntry((Integer) key, msg);
				} catch (final UnsupportedEncodingException e) {
					// will never happen
				}
			}
		}
	}

	private Integer getInt(final Object key) throws InvalidCatalogEntryException {
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

	public void addEntry(final CatalogEntry entry) {
		addEntry(entry.getKey(), entry.getValue());
	}

	public void addEntry(final Integer number, final String message) {
		put(number, message);
	}

	public String getMessage(final Integer number) {
		return get(number);
	}
}
