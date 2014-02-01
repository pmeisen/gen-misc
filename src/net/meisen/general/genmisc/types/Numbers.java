package net.meisen.general.genmisc.types;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Helper methods to deal with numbers.
 */
public class Numbers {

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
				final Class<?> srcClazz = number.getClass();
				final Number cmpNumber = Numbers.castToNumber(result,
						number.getClass());

				if (cmpNumber.equals(number)) {
					return (D) result;
				}
				/*
				 * There is a problem with the BigDecimal the equality depends
				 * on how it is created, i.e. using new BigDecimal(...) or
				 * BigDecimal.valueOf(...). The castToNumber method uses the
				 * valueOf, therefore here we check the constructor.
				 */
				else if (BigDecimal.class.equals(srcClazz)
						&& new BigDecimal(result.doubleValue()).equals(number)) {
					return (D) result;
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
			result = BigDecimal.valueOf(number.doubleValue());
		} else {
			return null;
		}

		return result;
	}
}
