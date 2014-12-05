package net.meisen.general.genmisc.exceptions.registry;

import java.util.Locale;

import net.meisen.general.genmisc.exceptions.ForwardedException;
import net.meisen.general.genmisc.exceptions.ForwardedRuntimeException;

/**
 * Registry and central location of all the available <code>Exceptions</code>.
 * 
 * @author pmeisen
 * 
 */
public interface IExceptionRegistry {

	/**
	 * Checks if the specified class is already registered.
	 * 
	 * @param exceptionClass
	 *            the class to be checked
	 *            
	 * @return {@code true} if the class is registered, otherwise {@code false}
	 */
	public boolean isRegistered(final Class<? extends Exception> exceptionClass);

	/**
	 * Throws a {@code RuntimeException} based on the specified
	 * {@code ForwardedException}. It might be necessary to cast the exception
	 * thrown to the one encapsulated by the {@code ForwardedRuntimeException},
	 * i.e. use
	 * {@code registry.<MyRuntimeException> throwRuntimeException(fwdEx);}.
	 * 
	 * @param exception
	 *            the {@code ForwardedRuntimeException} to base the exception to
	 *            be thrown on
	 * 
	 * @throws T
	 *             the type of the exception thrown
	 * 
	 * @see ForwardedRuntimeException
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final ForwardedRuntimeException exception) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message of the specified number and the specified reason, latter
	 * might be <code>null</code>.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param reason
	 *            the reason for the exception, might be <code>null</code>
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message of the specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message of the specified number, whereby the passed
	 * <code>parameter<code>s are replaced.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @throws T
	 *             the exception of the class specified by <code>exceptionClazz
	 *             </code>
	 * 
	 * @see RuntimeException
	 * @see String#format(String, Object...)
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Object... parameter) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message (with the replaced <code>parameter</code>s) of the
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
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 * @see String#format(String, Object...)
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason, final Object... parameter) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message (in the passed <code>Locale</code>) of the specified number
	 * and the specified reason, latter might be <code>null</code>.
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
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Throwable reason) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message (in the passed <code>Locale</code>) of the specified
	 * number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message (in the passed <code>Locale</code> and with replaced
	 * <code>parameter</code>s) of the specified number.
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
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 * @see String#format(Locale, String, Object...)
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Object... parameter) throws T;

	/**
	 * Throws a <code>RuntimeException</code> of the specified class having the
	 * error message (in the passed <code>Locale</code> and with replaced
	 * <code>parameter</code>s) of the specified number and the specified
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
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see RuntimeException
	 * @see String#format(Locale, String, Object...)
	 */
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Throwable reason,
			final Object... parameter) throws T;

	/**
	 * Throws an {@code Exception} based on the specified
	 * {@code ForwardedException}. It might be necessary to cast the exception
	 * thrown to the one encapsulated by the {@code ForwardedException},
	 * otherwise all {@code Exception} instances might be thrown, i.e. use
	 * {@code registry.<MyException> throwException(fwdEx);}.
	 * 
	 * @param exception
	 *            the {@code ForwardedException} to base the exception to be
	 *            thrown on
	 * 
	 * @throws T
	 *             the type of the exception thrown
	 * 
	 * @see ForwardedException
	 */
	public <T extends Exception> void throwException(
			final ForwardedException exception) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message of the specified number and the specified reason, latter might be
	 * <code>null</code>.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param reason
	 *            the reason for the exception, might be <code>null</code>
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message of the specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message of the specified number, whereby the passed
	 * <code>parameter</code>s are replaced.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @throws T
	 *             the exception of the class specified by <code>exceptionClazz
	 *           </code>
	 * 
	 * @see Exception
	 * @see String#format(String, Object...)
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Object... parameter) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message (with the replaced <code>parameter</code>s) of the specified
	 * number and the specified reason, latter might be <code>null</code>.
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
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 * @see String#format(String, Object...)
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason, final Object... parameter) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message (in the passed <code>Locale</code>) of the specified number and
	 * the specified reason, latter might be <code>null</code>.
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
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Throwable reason) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message (in the passed <code>Locale</code>) of the specified number.
	 * 
	 * @param exceptionClazz
	 *            the class of the exception to be thrown
	 * @param number
	 *            the number of the error, to look up the message within the
	 *            registry's catalogs
	 * @param locale
	 *            the <code>Locale</code> to be used to get the error message
	 *            and formats
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message (in the passed <code>Locale</code> and with replaced
	 * <code>parameter</code>s) of the specified number.
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
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 * @see String#format(Locale, String, Object...)
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Object... parameter) throws T;

	/**
	 * Throws an <code>Exception</code> of the specified class having the error
	 * message (in the passed <code>Locale</code> and with replaced
	 * <code>parameter</code>s) of the specified number and the specified
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
	 * @param parameter
	 *            the parameters to be replaced within the message
	 * 
	 * @throws T
	 *             the exception of the class specified by
	 *             <code>exceptionClazz</code>
	 * 
	 * @see Exception
	 * @see String#format(Locale, String, Object...)
	 */
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Locale locale, final Throwable reason,
			final Object... parameter) throws T;
}