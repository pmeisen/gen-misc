package net.meisen.general.genmisc.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;

/**
 * Utility class for objects
 * 
 * @author pmeisen
 * 
 */
public class Objects {

	/**
	 * Checks if two objects are equal to each other, whereby to
	 * <code>null</code> objects are also assumed to be equal
	 * 
	 * @param o1
	 *            the first object to compare
	 * @param o2
	 *            the second object to compare
	 * @return <code>true</code> if both objects are equal (according to their
	 *         equal method or if both objects are <code>null</code>, otherwise
	 *         <code>false</code>
	 */
	public static boolean equals(final Object o1, final Object o2) {
		if (o1 == null && o2 == null) {
			return true;
		} else if (o1 == null || o2 == null) {
			return false;
		} else if (o1 == o2) {
			return true;
		} else {
			return o1.equals(o2);
		}
	}

	/**
	 * Generates a hashCode for different objects based on their hashCode
	 * implementation
	 * 
	 * @param starter
	 *            the starter to vary among different hashCodes
	 * @param multiplier
	 *            the multiplier to vary among different hashCodes
	 * @param values
	 *            the objects to generate one hashCode for
	 * @return a hashCode
	 */
	public static int generateHashCode(final int starter, final int multiplier,
			final Object... values) {

		// the hashCode is implemented as defined in
		// http://java.sun.com/docs/books/effective/index.html, whereby the
		// implementation is simplified a little (see also
		// http://commons.apache.org/lang/api-release/org/apache/commons/lang3/builder/HashCodeBuilder.html)
		int hashCode = values == null || values.length == 0 ? 0 : starter;
		for (final Object value : values) {
			if (value != null) {
				hashCode += multiplier * value.hashCode();
			}
		}

		return hashCode;
	}

	/**
	 * Checks if a value is empty and returns the default value if so, otherwise
	 * the value is returned.
	 * 
	 * @param value
	 *            the value to check if empty
	 * @param defaultValue
	 *            the value to return if the value is empty
	 * @return the defaultValue if the value is empty (using
	 *         {@link Objects#empty(Object)}), otherwise the value
	 */
	public static <T> T defaultIfEmpty(final T value, T defaultValue) {
		return (empty(value) ? defaultValue : value);
	}

	/**
	 * Checks if an Object is empty (i.e. <code>null</code> or if a
	 * {@link String} equal to "").
	 * 
	 * @param value
	 *            The String to be checked
	 * @return <code>true</code> if the String is empty, otherwise
	 *         <code>false</code>
	 */
	public static boolean empty(final Object value) {
		if (value == null)
			return true;
		else if (value instanceof String) {
			final String string = value.toString().trim();

			if ("".equals(string))
				return true;
		}

		return false;
	}

	/**
	 * Copies an <code>Object</code> using serialization
	 * 
	 * @param orig
	 *            the <code>Object</code> to be copied
	 * 
	 * @return the copy of the <code>Object</code>
	 * 
	 * @throws IOException
	 *             if the <code>Object</code> cannot be copied
	 */
	@SuppressWarnings("unchecked")
	public static <T> T copy(final T orig) throws IOException {

		// Write the object out to a byte array
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		final ObjectOutputStream out = new ObjectOutputStream(bos);
		out.writeObject(orig);
		out.flush();
		Streams.closeIO(out);
		Streams.closeIO(bos);

		// Make an input stream from the byte array and read
		// a copy of the object back in.
		final ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream(bos.toByteArray()));

		// read the new object
		Object obj = null;
		try {
			obj = in.readObject();
		} catch (final ClassNotFoundException e) {
			// cannot happen
		}
		Streams.closeIO(in);

		return (T) obj;
	}

	/**
	 * Cast array to the specified type.
	 * 
	 * @param objects
	 *            the object-array to be casted
	 * @param type
	 *            the type to cast the objects to
	 * 
	 * @return the casted array
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] castArray(final Object[] objects, final Class<T> type) {
		final T[] castedObjects = (T[]) Array.newInstance(type, objects.length);

		// cast the object to the type specified
		for (int i = 0; i < objects.length; i++) {
			final T castedObject = (T) objects[i];
			castedObjects[i] = castedObject;
		}

		return castedObjects;
	}

	/**
	 * An implementation which tries to compare every two objects, i.e. bringing
	 * them into an intuitive order. The implementation uses several techniques:
	 * <ol>
	 * <li>use object specific comparisons, i.e. {@code o1 == o2} and
	 * {@code equals}</li>
	 * <li>check for the {@code Comparable} interface and use it if there is an
	 * inheritance-relation (i.e. superclass or equal) between the objects</li>
	 * <li>compare the classes of the objects, if those are unequal we have an
	 * order</li>
	 * <li>use the string representation to find an order</li>
	 * <li>use the hashCode to determine an order</li>
	 * </ol>
	 * <b>Note:</b><br/>
	 * Numbers are only compared if those are of the same type, use
	 * {@link #compare(Object, Object, boolean)} to enable comparison of
	 * {@code Number} instances across different number implementations.
	 * 
	 * 
	 * @param o1
	 *            the object to compare to {@code o2}
	 * @param o2
	 *            the object to compare to {@code o1}
	 * 
	 * @return {@code -1} if {@code o1 < o2}, {@code 0} if {@code o1 == o2} and
	 *         {@code 1} if {@code o1 > o2}
	 * 
	 * @see Comparable
	 */
	public static int compare(final Object o1, final Object o2) {
		return compare(o1, o2, false);
	}

	/**
	 * An implementation which tries to compare every two objects, i.e. bringing
	 * them into an intuitive order. The implementation uses several techniques:
	 * <ol>
	 * <li>use object specific comparisons, i.e. {@code o1 == o2} and
	 * {@code equals}</li>
	 * <li>check for the {@code Comparable} interface and use it if there is an
	 * inheritance-relation (i.e. superclass or equal) between the objects</li>
	 * <li>if {@code numberAware} is set to {@code true} and the classes are
	 * {@code Comparable} and {@code Number} instances compare the numbers
	 * across the different types</li>
	 * <li>compare the classes of the objects, if those are unequal we have an
	 * order</li>
	 * <li>use the string representation to find an order</li>
	 * <li>use the hashCode to determine an order</li>
	 * </ol>
	 * 
	 * @param o1
	 *            the object to compare to {@code o2}
	 * @param o2
	 *            the object to compare to {@code o1}
	 * @param numberAware
	 *            {@code true} if {@code Number} instances should be compared
	 *            according to their values, otherwise {@code false}
	 * 
	 * @return {@code -1} if {@code o1 < o2}, {@code 0} if {@code o1 == o2} and
	 *         {@code 1} if {@code o1 > o2}
	 * 
	 * @see Comparable
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static int compare(final Object o1, final Object o2,
			final boolean numberAware) {

		if (equals(o1, o2)) {
			return 0;
		} else if (o1 == null) {
			return -1;
		} else if (o2 == null) {
			return 1;
		} else {

			// get the classes
			final Class<?> o1Class = o1.getClass();
			final Class<?> o2Class = o2.getClass();
			if (o1 instanceof Comparable && o2 instanceof Comparable) {

				// check if we have numbers here
				final Comparable cmp1;
				final Comparable cmp2;
				if (numberAware && o1 instanceof Number && o2 instanceof Number) {
					final Class<? extends Number> ct = Numbers
							.determineCommonType((Number) o1, (Number) o2);

					final Number n1 = Numbers.mapToDataType(o1, ct);
					final Number n2 = Numbers.mapToDataType(o2, ct);
					if (n1 != null && n2 != null) {
						cmp1 = (Comparable) n1;
						cmp2 = (Comparable) n2;
					} else {
						cmp1 = Numbers.mapToDataType(o1, BigDecimal.class);
						cmp2 = Numbers.mapToDataType(o2, BigDecimal.class);
					}
				} else if (o1Class.equals(o2Class)) {
					cmp1 = (Comparable) o1;
					cmp2 = (Comparable) o2;
				} else {
					cmp1 = null;
					cmp2 = null;
				}

				// if we found something comparable let's use it
				if (cmp1 != null && cmp2 != null) {
					return cmp1.compareTo(cmp2) < 0 ? -1 : 1;
				}
			}

			// so the values aren't null and not comparable, so we have to do
			// some other comparison, which cannot lead to be equal
			final int cmpClass = o1Class.getName().compareTo(o2Class.getName());
			if (cmpClass != 0) {
				return cmpClass < 0 ? -1 : 1;
			}

			// both classes are of the same type but still they aren't equal
			// so let's use the string comparison of both
			final String o1String = o1.toString();
			final String o2String = o2.toString();
			final int cmpString = o1String.compareTo(o2String);
			if (cmpString != 0) {
				return cmpString < 0 ? -1 : 1;
			}

			// so last but not least, we just cannot do a lot and this has
			// probably side effects
			return o1.hashCode() < o2.hashCode() ? -1 : 1;
		}
	}
}
