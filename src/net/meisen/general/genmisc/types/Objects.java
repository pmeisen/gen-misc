package net.meisen.general.genmisc.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;

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
}
