package net.meisen.general.genmisc.exceptions.registry.stubs;

/**
 * Stub which has only a <code>String</code> and <code>Throwable</code>
 * constructor
 * 
 * @author pmeisen
 * 
 */
public class OnlyMessageAndThrowableException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Only constructor of this implementation
	 * 
	 * @param message
	 *          the message of the exception
	 * @param t
	 *          the reason for the exception
	 */
	public OnlyMessageAndThrowableException(final String message,
			final Throwable t) {
		super(message, t);
	}
}
