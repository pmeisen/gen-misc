package net.meisen.general.genmisc.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java.lang.IndexOutOfBoundsException;

import net.meisen.general.genmisc.types.Objects;
import net.meisen.general.genmisc.types.Strings;

/**
 * Utility class for collections
 * 
 * @author pmeisen
 * 
 */
public class Collections {

	/**
	 * A <code>Filter</code> is used to filter a collection
	 * 
	 * @see Collections#filter(Collection, Filter)
	 * 
	 * @author pmeisen
	 * 
	 * @param <T>
	 */
	public interface Filter<T> {

		/**
		 * Checking method to check if a value should be added (
		 * <code>true</code>) or not (<code>false</code>).
		 * 
		 * @param object
		 *            the object to be checked
		 * @return <code>true</code> if the object should be added, otherwise
		 *         <code>false</code>
		 */
		public boolean check(final T object);
	};

	/**
	 * This method concatenates all value of a {@link Collection} with the
	 * passed separator. You can use {@link String#split(String)} to reverse the
	 * operation. If the {@link Collection} contains <code>null</code> values,
	 * those values will be skipped.
	 * 
	 * @param separator
	 *            the separator used for separation
	 * @param list
	 *            the {@link Collection} to serialize or represent as string
	 * 
	 * @return a {@link String} with the entries of the {@link Collection}
	 *         separated by the passed <code>separator</code>
	 */
	public static String concate(final String separator,
			final Collection<? extends Object> list) {
		return Strings.concate(separator, list);
	}

	/**
	 * Determines the 0-based position of the <code>v</code> within the
	 * {@link Collection}
	 * 
	 * @param c
	 *            the {@link Collection} to look in
	 * @param v
	 *            the value to look for
	 * @return the 0-based position of the <code>v</code> within the collection,
	 *         or <code>-1</code> if the value could not be found
	 * 
	 * @see Objects#equals(Object, Object)
	 */
	public static <X> int getPosition(final Collection<X> c, X v) {
		int i = 0;
		for (final X item : c) {
			if (Objects.equals(item, v)) {
				return i;
			}
			i++;
		}

		return -1;
	}

	/**
	 * Is used to retrieve the element on a specific position within a
	 * {@link Collection}
	 * 
	 * @param position
	 *            the position to retrieve the value from
	 * @param c
	 *            the {@link Collection} to get the value from
	 * @return the value found on the specified position
	 * @throws IndexOutOfBoundsException
	 *             if the specified position is not within the range of the
	 *             {@link Collection}
	 */
	public static <X> X get(final int position, final Collection<X> c)
			throws IndexOutOfBoundsException {

		if (c.size() <= position || position < 0) {
			throw new IndexOutOfBoundsException("The position '" + position
					+ "' is invalid for a Collection of size '" + c.size()
					+ "'");
		} else if (c instanceof List) {
			return (X) ((List<X>) c).get(position);
		} else {
			int i = 0;
			for (final X item : c) {
				if (i == position) {
					return item;
				}
				i++;
			}

			// not found - should never happen
			return null;
		}
	}

	/**
	 * Creates the powerset of a set
	 * 
	 * @param set
	 *            the set to create the powerset for
	 * @return the powerset of the passed set
	 */
	public static <T> Set<Set<T>> createPowerset(final Set<T> set) {
		final Set<Set<T>> powerset = new HashSet<Set<T>>();

		// the empty set has only the empty set as powerset
		if (set.isEmpty()) {
			powerset.add(new HashSet<T>());

			return powerset;
		}

		// get a list for easier access
		final List<T> list = new ArrayList<T>(set);

		T head = list.get(0);
		Set<T> rest = new HashSet<T>(list.subList(1, list.size()));

		for (Set<T> subSet : createPowerset(rest)) {
			final Set<T> newSet = new HashSet<T>();
			newSet.add(head);
			newSet.addAll(subSet);
			powerset.add(newSet);
			powerset.add(subSet);
		}

		return powerset;
	}

	/**
	 * This method is used to filter a <code>Collection</code>.
	 * 
	 * @param c
	 *            the <code>Collection</code> to be filtered
	 * @param filter
	 *            the <code>Filter</code> to use, if <code>null</code> all
	 *            values will be returned
	 * @return the filtered <code>Collection</code>
	 */
	public static <T> Collection<T> filter(final Collection<T> c,
			final Filter<T> filter) {
		final Collection<T> filtered = new ArrayList<T>();

		// if no filter is specified we return all
		if (filter == null) {
			filtered.addAll(c);
		} else {

			// check the filter for each item
			for (final T item : c) {
				if (filter.check(item)) {
					filtered.add(item);
				}
			}
		}

		return filtered;
	}

	/**
	 * Generates a hashCode for different objects based on their hashCode
	 * implementation
	 * 
	 * @param objects
	 *            the objects to generate one hashCode for
	 * @param starter
	 *            the starter to vary among different hashCodes
	 * @param multiplier
	 *            the multiplier to vary among different hashCodes
	 * @return a hashCode
	 * 
	 * @see Objects#generateHashCode(int, int, Object...)
	 */
	public static int generateHashCode(final Collection<?> objects,
			final int starter, final int multiplier) {

		if (objects == null) {
			return 0;
		} else {
			return Objects.generateHashCode(starter, multiplier,
					objects.toArray());
		}
	}

	/**
	 * Compares two <code>Collection</code> instances
	 * 
	 * @param col1
	 *            the first <code>Collection</code> to compare with
	 * @param col2
	 *            the second <code>Collection</code> to compare with
	 * @param ignoreOrder
	 *            <code>true</code> to ignore any order within the
	 *            <code>Collection</code> instances - if there is one, otherwise
	 *            <code>false</code>
	 * @return <code>true</code> if the <code>Collection</code> instances are
	 *         equal, otherwise <code>false</code>
	 */
	public static boolean checkEqual(final Collection<?> col1,
			final Collection<?> col2, final boolean ignoreOrder) {

		if (col1 == null && col2 == null)
			return true;
		else if (col1 == null)
			return false;
		else if (col2 == null)
			return false;
		else if (col1.size() != col2.size())
			return false;

		boolean found;
		final List<Object> copyList1 = new ArrayList<Object>(col1);
		for (final Object compareValue : col2) {

			// look for the value
			found = false;
			if (ignoreOrder) {
				for (final Object value : copyList1) {
					if (Objects.equals(compareValue, value)) {
						copyList1.remove(value);
						found = true;
						break;
					}
				}
			} else if (Objects.equals(compareValue, copyList1.get(0))) {
				found = true;

				// remove the entry so that the first item will be there
				copyList1.remove(0);
			}

			if (!found)
				return false;
		}

		return true;
	}
}
