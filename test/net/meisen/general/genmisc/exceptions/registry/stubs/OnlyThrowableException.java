package net.meisen.general.genmisc.exceptions.registry.stubs;

/**
 * Stub which has only a <code>Throwable</code> constructor
 * 
 * @author pmeisen
 * 
 */
public class OnlyThrowableException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * Only constructor of this implementation
	 * 
	 * @param t
	 *          the reason of the exception
	 */
	public OnlyThrowableException(final Throwable t) {
		super(t);
	}
}