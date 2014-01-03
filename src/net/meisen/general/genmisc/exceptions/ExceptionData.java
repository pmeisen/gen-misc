package net.meisen.general.genmisc.exceptions;

import java.util.Locale;

import net.meisen.general.genmisc.exceptions.registry.IExceptionRegistry;

/**
 * The {@code ExceptionData} is just a data-container class which wraps all the
 * data needed for a {@code IExceptionRegistry}, a {@code ForwardedException} or
 * a {@code ForwardedRuntimeException}.
 * 
 * @author pmeisen
 * 
 * @see IExceptionRegistry
 * @see ForwardedException
 * @see ForwardedRuntimeException
 * 
 * @param <T>
 *            the type of the {@code Exception} which data is wrapped
 */
public class ExceptionData<T extends Exception> implements
		IExceptionDataProvider {

	private final Class<? extends T> exceptionClazz;
	private final Integer number;
	private final Locale locale;
	private final Throwable reason;
	private final Object[] parameter;

	/**
	 * Default constructor which defines all the data, whereby all of the data
	 * might be {@code null}, except the {@code exceptionClazz}.
	 * 
	 * @param exceptionClazz
	 *            the {@code Class} of the exception which data is wrapped by
	 *            this instance
	 * @param number
	 *            the error number to look up the exception in a registry
	 * @param locale
	 *            the locale to be used
	 * @param reason
	 *            a reason which might have caused the exception
	 * @param parameter
	 *            additional parameters
	 */
	public ExceptionData(final Class<? extends T> exceptionClazz,
			final Integer number, final Locale locale, final Throwable reason,
			final Object[] parameter) {
		this.exceptionClazz = exceptionClazz;
		this.number = number;
		this.locale = locale;
		this.reason = reason;
		this.parameter = parameter;
	}

	@Override
	public Integer getNumber() {
		return number;
	}

	@Override
	public Locale getLocale() {
		return locale;
	}

	@Override
	public Throwable getReason() {
		return reason;
	}

	@Override
	public Object[] getParameter() {
		return parameter;
	}

	@Override
	public Class<? extends T> getExceptionClass() {
		return exceptionClazz;
	}

	@Override
	public String toString() {
		return "Exception '" + exceptionClazz.getName()
				+ "' (Number: '" + number + "', Locale: '" + locale
				+ "', Reason: '" + reason + "')";
	}
}
