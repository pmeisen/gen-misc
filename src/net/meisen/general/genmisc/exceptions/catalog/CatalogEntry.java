package net.meisen.general.genmisc.exceptions.catalog;

import java.util.Map.Entry;

/**
 * An entry for a <code>ExceptionCatalog</code>.
 * 
 * @see DefaultExceptionCatalog
 * 
 * @author pmeisen
 * 
 */
public class CatalogEntry implements Entry<Integer, String> {

	private Integer number;
	private String message;

	/**
	 * Constructor to create an entry for a <code>ExceptionCatalog</code>, which
	 * specifies the number, but no message.
	 * 
	 * @param number
	 *          the number of the entry
	 */
	public CatalogEntry(final Integer number) {
		this(number, null);
	}

	/**
	 * Constructor to create an entry for a <code>ExceptionCatalog</code>, which
	 * specifies the number and the message.
	 * 
	 * @param number
	 *          the number of the entry
	 * @param message
	 *          the message of the entry
	 */
	public CatalogEntry(final Integer number, final String message) {
		this.number = number;
		this.message = message;
	}

	@Override
	public Integer getKey() {
		return number;
	}

	@Override
	public String getValue() {
		return message;
	}

	@Override
	public String setValue(final String value) {
		final String oldMessage = message;

		message = value;

		return oldMessage;
	}
}
