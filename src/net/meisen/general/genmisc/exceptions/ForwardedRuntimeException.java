package net.meisen.general.genmisc.exceptions;

import java.util.Locale;

import net.meisen.general.genmisc.exceptions.registry.IExceptionRegistry;

/**
 * A {@code ForwardedRuntimeException} is by sub-classes which have no access to
 * the {@code ExceptionRegistry}. The class which has access catches the
 * {@code ForwardedRuntimeException} and uses
 * {@link IExceptionRegistry#throwRuntimeException(ForwardedRuntimeException)}
 * to throw the forwarded exception.
 * 
 * @author pmeisen
 * 
 * @see IExceptionRegistry
 * 
 */
public class ForwardedRuntimeException extends RuntimeException implements
		IExceptionDataProvider {
	private static final long serialVersionUID = 2064142914267986473L;

	private final ExceptionData<RuntimeException> data;

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message of the
	 * specified number and the specified reason, latter might be
	 * <code>null</code>.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param reason
	 *            the reason for the exception, might be <code>null</code>
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Throwable reason) {
		this(exceptionClazz, number, null, reason, (Object[]) null);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message of the
	 * specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number) {
		this(exceptionClazz, number, null, null, (Object[]) null);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message of the
	 * specified number, whereby the passed <code>parameter</code>s are
	 * replaced.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @see String#format(String, Object...)
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Object... parameter) {
		this(exceptionClazz, number, null, null, parameter);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message (with the
	 * replaced <code>parameter</code>s) of the specified number and the
	 * specified reason, latter might be <code>null</code>.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param reason
	 *            the reason for the exception, might be <code>null</code>
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @see String#format(String, Object...)
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Throwable reason,
			final Object... parameter) {
		this(exceptionClazz, number, null, reason, parameter);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message (in the
	 * passed <code>Locale</code>) of the specified number and the specified
	 * reason, latter might be <code>null</code>.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 * @param reason
	 *            the reason for the exception, might be <code>null</code>
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Locale locale, final Throwable reason) {
		this(exceptionClazz, number, locale, reason, (Object[]) null);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message (in the
	 * passed <code>Locale</code>) of the specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Locale locale) {
		this(exceptionClazz, number, locale, null, (Object[]) null);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message (in the
	 * passed <code>Locale</code> and with replaced <code>parameter</code>s) of
	 * the specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @see String#format(Locale, String, Object...)
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Locale locale,
			final Object... parameter) {
		this(exceptionClazz, number, locale, null, parameter);
	}

	/**
	 * Creates a {@code ForwardedRuntimeException} forwarding an exception of
	 * the specified {@code exceptionClazz} having the error message (in the
	 * passed <code>Locale</code> and with replaced <code>parameter</code>s) of
	 * the specified number and the specified reason.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 * @param reason
	 *            the reason for the exception
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @see String#format(Locale, String, Object...)
	 */
	public ForwardedRuntimeException(
			final Class<? extends RuntimeException> exceptionClazz,
			final Integer number, final Locale locale, final Throwable reason,
			final Object... parameter) {
		super("Please use a ExceptionRegistry to resolve this error.");
		this.data = new ExceptionData<RuntimeException>(exceptionClazz, number,
				locale, reason, parameter);
	}

	@Override
	public Class<? extends RuntimeException> getExceptionClass() {
		return data.getExceptionClass();
	}

	@Override
	public Integer getNumber() {
		return data.getNumber();
	}

	@Override
	public Locale getLocale() {
		return data.getLocale();
	}

	@Override
	public Throwable getReason() {
		return data.getReason();
	}

	@Override
	public Object[] getParameter() {
		return data.getParameter();
	}
	
	@Override
	public String toString() {
		return data.toString();
	}
}
