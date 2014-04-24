package net.meisen.general.genmisc.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

/**
 * Helper methods to deal with numbers.
 */
public class Numbers {
	private static Map<Class<? extends Number>, Integer> intHierarchy = new HashMap<Class<? extends Number>, Integer>();
	private static Map<Class<? extends Number>, Integer> floatHierarchy = new HashMap<Class<? extends Number>, Integer>();

	static {
		int intFirst = 1;
		intHierarchy.put(Byte.class, intFirst++);
		intHierarchy.put(Short.class, intFirst++);
		intHierarchy.put(Integer.class, intFirst++);
		intHierarchy.put(Long.class, intFirst++);

		int floatFirst = -1;
		floatHierarchy.put(Float.class, floatFirst--);
		floatHierarchy.put(Double.class, floatFirst--);
	}

	/**
	 * Maps the specified value to the number specified by the {@code clazz}.
	 * 
	 * @param value
	 *            the value to be mapped
	 * @param clazz
	 *            the class to map the specified value to
	 * 
	 * @return the mapped value or {@code null} if it cannot be mapped to the
	 *         number
	 */
	@SuppressWarnings("unchecked")
	public static <D> D mapToDataType(final Object value, final Class<D> clazz) {
		if (value == null) {
			return null;
		}

		if (clazz.equals(value.getClass())) {
			return (D) value;
		} else if (Number.class.isAssignableFrom(clazz)) {
			final Class<? extends Number> numClazz = (Class<? extends Number>) clazz;
			final Number number = (Number) value;
			final Number result = Numbers.castToNumber(number, numClazz);

			// check the result
			if (result != null) {
				final Class<? extends Number> srcClazz = number.getClass();
				final Number cmpNumber = Numbers.castToNumber(result, srcClazz);

				if (cmpNumber.equals(number)) {
					return (D) result;
				}
				/*
				 * There is a problem with the BigDecimal the equality depends
				 * on how it is created, i.e. using new BigDecimal(...) or
				 * BigDecimal.valueOf(...). Therefore we check all of them, if
				 * one is assumed to be equal we assume it to be equal.
				 */
				else if (BigDecimal.class.equals(srcClazz)) {

					if (intHierarchy.containsKey(number.getClass())) {
						final long res = result.longValue();

						if (BigDecimal.valueOf(res).equals(number)) {
							return (D) result;
						} else if (new BigDecimal(res).equals(number)) {
							return (D) result;
						}
					} else {
						final double res = result.doubleValue();

						if (BigDecimal.valueOf(res).equals(number)) {
							return (D) result;
						} else if (new BigDecimal(res).equals(number)) {
							return (D) result;
						}
					}
				}
			}
		}

		// if we came so far there is no hope
		return null;
	}

	/**
	 * Method which maps a {@code number} to the specified {@code clazz}. The
	 * specified {@code clazz} is another {@code Number}.
	 * 
	 * @param number
	 *            the number to be casted
	 * @param clazz
	 *            the {@code Number}-class to cast the {@code number} to
	 * 
	 * @return the casted {@code number} or {@code null} if a cast wasn't
	 *         possible
	 */
	public static Number castToNumber(final Number number,
			final Class<? extends Number> clazz) {
		final Number result;

		if (number == null) {
			return null;
		} else if (number.getClass().equals(clazz)) {
			return number;
		} else if (Byte.class.equals(clazz)) {
			result = number.byteValue();
		} else if (Short.class.equals(clazz)) {
			result = number.shortValue();
		} else if (Integer.class.equals(clazz)) {
			result = number.intValue();
		} else if (Long.class.equals(clazz)) {
			result = number.longValue();
		} else if (Float.class.equals(clazz)) {
			result = number.floatValue();
		} else if (Double.class.equals(clazz)) {
			result = number.doubleValue();
		} else if (BigInteger.class.equals(clazz)) {
			result = BigInteger.valueOf(number.longValue());
		} else if (BigDecimal.class.equals(clazz)) {
			if (intHierarchy.containsKey(number.getClass())) {
				result = BigDecimal.valueOf(number.longValue());
			} else {
				result = BigDecimal.valueOf(number.doubleValue());
			}
		} else {
			return null;
		}

		return result;
	}

	/**
	 * Cast the {@code nr} to an {@code long}.
	 * 
	 * @param nr
	 *            the {@code Number} to be casted
	 * 
	 * @return the long value
	 */
	public static long castToLong(final Number nr) {
		if (nr == null) {
			throw new NullPointerException(
					"The number to be casted cannot be null.");
		}

		return nr.longValue();
	}

	/**
	 * Cast the {@code nr} to an {@code int}.
	 * 
	 * @param nr
	 *            the {@code Number} to be casted
	 * 
	 * @return the int value
	 */
	public static int castToInt(final Number nr) {
		if (nr == null) {
			throw new NullPointerException(
					"The number to be casted cannot be null.");
		}

		// convert the type
		final Class<?> clazz = nr.getClass();
		if (Short.class.equals(clazz)) {
			return castToInt(nr.shortValue());
		} else if (Byte.class.equals(clazz)) {
			return castToInt(nr.byteValue());
		} else if (Integer.class.equals(clazz)) {
			return castToInt(nr.intValue());
		} else if (Long.class.equals(clazz)) {
			return castToInt(nr.longValue());
		} else {
			return castToInt(nr.longValue());
		}
	}

	/**
	 * Cast the value to an integer.
	 * 
	 * @param b
	 *            the value to be casted
	 * @return the result
	 */
	public static int castToInt(final byte b) {
		return (int) b;
	}

	/**
	 * Cast the value to an integer.
	 * 
	 * @param s
	 *            the value to be casted
	 * 
	 * @return the result
	 */
	public static int castToInt(final short s) {
		return (short) s;
	}

	/**
	 * Cast the value to an integer.
	 * 
	 * @param l
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into an integer
	 */
	public static int castToInt(final long l) {
		if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the long value '" + l
					+ "' to an integer.");
		} else {
			return (int) l;
		}
	}

	/**
	 * Cast the {@code nr} to an {@code short}.
	 * 
	 * @param nr
	 *            the {@code Number} to be casted
	 * 
	 * @return the short value
	 */
	public static short castToShort(final Number nr) {
		if (nr == null) {
			throw new NullPointerException(
					"The number to be casted cannot be null.");
		}

		// convert the type
		final Class<?> clazz = nr.getClass();
		if (Short.class.equals(clazz)) {
			return castToShort(nr.shortValue());
		} else if (Byte.class.equals(clazz)) {
			return castToShort(nr.byteValue());
		} else if (Integer.class.equals(clazz)) {
			return castToShort(nr.intValue());
		} else if (Long.class.equals(clazz)) {
			return castToShort(nr.longValue());
		} else {
			return castToShort(nr.longValue());
		}
	}

	/**
	 * Cast the value to a short.
	 * 
	 * @param b
	 *            the value to be casted
	 * @return the result
	 */
	public static short castToShort(final byte b) {
		return (short) b;
	}

	/**
	 * Cast the value to a short.
	 * 
	 * @param i
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into a short
	 */
	public static short castToShort(final int i) {
		if (i < Short.MIN_VALUE || i > Short.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the integer value '"
					+ i + "' to a short.");
		} else {
			return (short) i;
		}
	}

	/**
	 * Cast the value to a short.
	 * 
	 * @param l
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into a short
	 */
	public static short castToShort(final long l) {
		if (l < Short.MIN_VALUE || l > Short.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the long value '" + l
					+ "' to a short.");
		} else {
			return (short) l;
		}
	}

	/**
	 * Cast the {@code nr} to an {@code byte}.
	 * 
	 * @param nr
	 *            the {@code Number} to be casted
	 * 
	 * @return the byte value
	 */
	public static byte castToByte(final Number nr) {
		if (nr == null) {
			throw new NullPointerException(
					"The number to be casted cannot be null.");
		}

		// convert the type
		final Class<?> clazz = nr.getClass();
		if (Short.class.equals(clazz)) {
			return castToByte(nr.shortValue());
		} else if (Byte.class.equals(clazz)) {
			return castToByte(nr.byteValue());
		} else if (Integer.class.equals(clazz)) {
			return castToByte(nr.intValue());
		} else if (Long.class.equals(clazz)) {
			return castToByte(nr.longValue());
		} else {
			return castToByte(nr.longValue());
		}
	}

	/**
	 * Cast the value to a byte.
	 * 
	 * @param s
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into a byte
	 */
	public static byte castToByte(final short s) {
		if (s < Byte.MIN_VALUE || s > Byte.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the short value '"
					+ s + "' to a byte.");
		} else {
			return (byte) s;
		}
	}

	/**
	 * Cast the value to a byte.
	 * 
	 * @param i
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into a byte
	 */
	public static byte castToByte(final int i) {
		if (i < Byte.MIN_VALUE || i > Byte.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the integer value '"
					+ i + "' to a byte.");
		} else {
			return (byte) i;
		}
	}

	/**
	 * Cast the value to a byte.
	 * 
	 * @param l
	 *            the value to be casted
	 * @return the result
	 * 
	 * @throws ArithmeticException
	 *             if the value doesn't fit into a byte
	 */
	public static byte castToByte(final long l) {
		if (l < Byte.MIN_VALUE || l > Byte.MAX_VALUE) {
			throw new ArithmeticException("Cannot convert the long value '" + l
					+ "' to a byte.");
		} else {
			return (byte) l;
		}
	}

	/**
	 * Casts the array to a byte array.
	 * 
	 * @param objects
	 *            the array to be casted
	 * 
	 * @return the converted array
	 */
	public static byte[] castArrayToByte(final Object[] objects) {
		final byte[] casted = new byte[objects.length];

		// cast the object to the type specified
		for (int i = 0; i < objects.length; i++) {
			final Object castedObject = objects[i];

			if (castedObject instanceof Number) {
				casted[i] = Numbers.castToByte((Number) castedObject);
			}
		}

		return casted;
	}

	/**
	 * Casts the array to a short array.
	 * 
	 * @param objects
	 *            the array to be casted
	 * 
	 * @return the converted array
	 */
	public static short[] castArrayToShort(final Object[] objects) {
		final short[] casted = new short[objects.length];

		// cast the object to the type specified
		for (int i = 0; i < objects.length; i++) {
			final Object castedObject = objects[i];

			if (castedObject instanceof Number) {
				casted[i] = Numbers.castToShort((Number) castedObject);
			}
		}

		return casted;
	}

	/**
	 * Casts the array to an int array.
	 * 
	 * @param objects
	 *            the array to be casted
	 * 
	 * @return the converted array
	 */
	public static int[] castArrayToInt(final Object[] objects) {
		final int[] casted = new int[objects.length];

		// cast the object to the type specified
		for (int i = 0; i < objects.length; i++) {
			final Object castedObject = objects[i];

			if (castedObject instanceof Number) {
				casted[i] = Numbers.castToInt((Number) castedObject);
			}
		}

		return casted;
	}

	/**
	 * Casts the array to a long array.
	 * 
	 * @param objects
	 *            the array to be casted
	 * 
	 * @return the converted array
	 */
	public static long[] castArrayToLong(final Object[] objects) {
		final long[] casted = new long[objects.length];

		// cast the object to the type specified
		for (int i = 0; i < objects.length; i++) {
			final Object castedObject = objects[i];

			if (castedObject instanceof Number) {
				casted[i] = Numbers.castToLong((Number) castedObject);
			}
		}

		return casted;
	}

	/**
	 * Determines the common representation of the passed {@code Number}
	 * instances.
	 * 
	 * @param n
	 *            the numbers to determine the common type for
	 * 
	 * @return the common type of the numbers
	 */
	public static Class<? extends Number> determineCommonType(final Number... n) {

		int weight = -1;
		int signumWeight = 0;
		Class<? extends Number> res = null;
		for (final Number nr : n) {
			if (nr == null) {
				continue;
			} else if (res == null) {
				res = nr.getClass();
				weight = determineWeight(nr);
				signumWeight = (int) Math.signum(weight);
			} else {
				final int curWeight = determineWeight(nr);

				/*
				 * same type i.e. we stay within floats or integers
				 */
				if (signumWeight == Math.signum(curWeight)) {
					if (Math.abs(curWeight) > Math.abs(weight)) {
						res = nr.getClass();
						weight = curWeight;
					}
				}
				/*
				 * a value > 0 means we are in the integers world and have to
				 * change to the reals world
				 */
				else if (signumWeight > 0) {
					if (weight == Integer.MAX_VALUE) {
						// floating numbers and BigIntegers => BigDecimal
						return BigDecimal.class;
					} else {
						res = nr.getClass();
						weight = curWeight;
					}
				}
				/*
				 * reals world, and some integers values
				 */
				else {
					if (curWeight == Integer.MAX_VALUE) {
						// floating numbers and BigIntegers => BigDecimal
						return BigDecimal.class;
					}
				}
			}
		}

		return res;
	}

	private static int determineWeight(final Number nr) {

		Integer weight = intHierarchy.get(nr.getClass());
		if (weight == null) {
			weight = floatHierarchy.get(nr.getClass());

			if (weight == null) {
				if (nr instanceof BigInteger) {
					return Integer.MAX_VALUE;
				} else if (nr instanceof BigDecimal) {
					return Integer.MIN_VALUE;
				} else {
					throw new IllegalArgumentException("The class '"
							+ nr.getClass() + "' is not supported.");
				}
			} else {
				return weight.intValue();
			}
		} else {
			return weight.intValue();
		}
	}
}
