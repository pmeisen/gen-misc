package net.meisen.general.genmisc.exceptions.registry.stubs;

/**
 * Constructor which has all the three default constructors of an
 * <code>Exception</code>
 * 
 * @author pmeisen
 * 
 */
public class FullException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a message.
	 * 
	 * @param message
	 *          the message
	 */
	public FullException(final String message) {
		super(message);
	}

	/**
	 * Constructor with only a <code>Throwable</code>.
	 * 
	 * @param t
	 *          the reason of the exception
	 */
	public FullException(final Throwable t) {
		super(t);
	}

	/**
	 * Constructor with a message and a reason.
	 * 
	 * @param message
	 *          the message
	 * @param t
	 *          the reason of the exception
	 */
	public FullException(final String message, final Throwable t) {
		super(message, t);
	}
}
