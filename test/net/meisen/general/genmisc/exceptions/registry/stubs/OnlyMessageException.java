package net.meisen.general.genmisc.exceptions.registry.stubs;

/**
 * Stub which has only a <code>String</code> constructor
 * 
 * @author pmeisen
 * 
 */
public class OnlyMessageException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Only constructor of this implementation
	 * 
	 * @param message
	 *          the message of the exception
	 */
	public OnlyMessageException(final String message) {
		super(message);
	}
}
