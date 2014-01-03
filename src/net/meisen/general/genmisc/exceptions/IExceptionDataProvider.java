package net.meisen.general.genmisc.exceptions;

import java.util.Locale;

/**
 * An interface which marks a class to be a provider of specific data regarding
 * an exception.
 * 
 * @author pmeisen
 * 
 */
public interface IExceptionDataProvider {

	/**
	 * Gets the class of the exception.
	 * 
	 * @return the class of the exception
	 */
	public Class<? extends Exception> getExceptionClass();

	/**
	 * Gets the number of the exception
	 * 
	 * @return the number of the exception
	 */
	public Integer getNumber();

	/**
	 * Gets the {@code Locale} of the message of the exception.
	 * 
	 * @return the {@code Locale} of the message of the exception
	 * 
	 * @see Locale
	 */
	public Locale getLocale();

	/**
	 * Gets the reason or cause of the exception.
	 * 
	 * @return the reason or cause of the exception
	 */
	public Throwable getReason();

	/**
	 * Gets the parameters for the exception's message.
	 * 
	 * @return the parameters for the exception's message
	 */
	public Object[] getParameter();
}
