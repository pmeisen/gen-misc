package net.meisen.general.genmisc.exceptions.registry.stubs;

/**
 * Constructor which has all the three default constructors of an
 * <code>RuntimeException</code>
 * 
 * @author pmeisen
 * 
 */
public class FullRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor with a message.
	 * 
	 * @param message
	 *          the message
	 */
	public FullRuntimeException(final String message) {
		super(message);
	}

	/**
	 * Constructor with only a <code>Throwable</code>.
	 * 
	 * @param t
	 *          the reason of the exception
	 */
	public FullRuntimeException(final Throwable t) {
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
	public FullRuntimeException(final String message, final Throwable t) {
		super(message, t);
	}
}