package net.meisen.general.genmisc.exceptions.registry;

import java.util.Locale;

/**
 * Abstract implementation of a <code>ExceptionRegistry</code> which implements
 * the most defined methods and reduced it to two needed concrete
 * implementations.
 * 
 * @author pmeisen
 * 
 */
public abstract class AbstractExceptionRegistry implements IExceptionRegistry {

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason) throws T {
		throwRuntimeException(exceptionClazz, number, null, reason, (Object[]) null);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number) throws T {
		throwRuntimeException(exceptionClazz, number, null, null, (Object[]) null);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Object... parameter) throws T {
		throwRuntimeException(exceptionClazz, number, null, null, parameter);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason, final Object... parameter) throws T {
		throwRuntimeException(exceptionClazz, number, null, reason, parameter);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale,
			final Throwable reason) throws T {
		throwRuntimeException(exceptionClazz, number, locale, reason,
				(Object[]) null);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale)
			throws T {
		throwRuntimeException(exceptionClazz, number, locale, null, (Object[]) null);
	}

	@Override
	public <T extends RuntimeException> void throwRuntimeException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale,
			final Object... parameter) throws T {
		throwRuntimeException(exceptionClazz, number, locale, null, parameter);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason) throws T {
		throwException(exceptionClazz, number, null, reason, (Object[]) null);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number) throws T {
		throwException(exceptionClazz, number, null, null, (Object[]) null);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Object... parameter) throws T {
		throwException(exceptionClazz, number, null, null, parameter);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number,
			final Throwable reason, final Object... parameter) throws T {
		throwException(exceptionClazz, number, null, reason, parameter);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale,
			final Throwable reason) throws T {
		throwException(exceptionClazz, number, locale, reason, (Object[]) null);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale)
			throws T {
		throwException(exceptionClazz, number, locale, null, (Object[]) null);
	}

	@Override
	public <T extends Exception> void throwException(
			final Class<T> exceptionClazz, final Integer number, final Locale locale,
			final Object... parameter) throws T {
		throwException(exceptionClazz, number, locale, null, parameter);
	}
}
