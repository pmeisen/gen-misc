package net.meisen.general.genmisc.exceptions.catalog;

/**
 * This exception is thrown whenever an entry is added to the
 * <code>ExceptionCatalog</code> which is invalid.
 * 
 * @author pmeisen
 * 
 */
public class InvalidCatalogEntryException extends Exception {
	private static final long serialVersionUID = -3914750135973754520L;

	/**
	 * Default constructor with message to be shown for the reason why the entry
	 * cannot be added.
	 * 
	 * @param message
	 *          the message of the exception
	 */
	public InvalidCatalogEntryException(final String message) {
		super(message);
	}
}
