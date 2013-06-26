package net.meisen.general.genmisc.exceptions.catalog;

import java.util.Set;

/**
 * An interface which stores exception in form of a catalog. The
 * <code>ExceptionCatalog</code> is a catalog which stores messages for
 * exceptions associated to a specific integer value, i.e. 1000 is associated
 * with the message "Invalid user".
 * 
 * @author pmeisen
 * 
 */
public interface IExceptionCatalog {

	/**
	 * Gets the message associated to the specified <code>number</code>.
	 * 
	 * @param number
	 *          the number to get the message for
	 * 
	 * @return the message to be associated to the specified <code>number</code>
	 */
	public String getMessage(final Integer number);

	/**
	 * Gets a <code>Set</code> of all the available (i.e. defined) numbers, to
	 * which <code>Exception</code>-instances are associated to.
	 * 
	 * @return a <code>Set</code> of all the available numbers in the catalog
	 */
	public Set<Integer> getAvailableExceptions();
}
