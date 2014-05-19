package net.meisen.general.genmisc.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for string manipulation
 * 
 * @author pmeisen
 * 
 */
public class Strings {
	private static final Pattern integerPattern;
	private static final Pattern doublePattern;

	static {
		// @formatter:off
		final String Digits = "(\\p{Digit}+)";
		final String HexDigits = "(\\p{XDigit}+)";

		/*
		 * an exponent is 'e' or 'E' followed by an optionally signed decimal
		 * integer.
		 */
		final String Exp = "[eE][+-]?" + Digits;
		final String fpRegex = ("[\\x00-\\x20]*" + // Optional leading
				                "[+-]?(" +         // Optional sign character
				                "NaN|" +           // "NaN" string
				                "Infinity|" +      // "Infinity" string

				/*
				 * A decimal floating-point string representing a finite
				 * positive number without a leading sign has at most five basic
				 * pieces: Digits . Digits ExponentPart FloatTypeSuffix
				 * 
				 * Since this method allows integer-only strings as input in
				 * addition to strings of floating-point literals, the two
				 * sub-patterns below are simplifications of the grammar
				 * productions from the Java Language Specification, 2nd
				 * edition, section 3.10.2.
				 */
				// Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
				"(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" +

		        // . Digits ExponentPart_opt FloatTypeSuffix_opt
				"(\\.(" + Digits + ")(" + Exp + ")?)|" +

				// Hexadecimal strings
				"((" +
				// 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "(\\.)?)|" +

				// 0[xX] HexDigits_opt . HexDigits BinaryExponent
				// FloatTypeSuffix_opt
				"(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" +

				")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");
		// @formatter:on

		doublePattern = Pattern.compile(fpRegex);
		integerPattern = Pattern.compile("(\\+|\\-)?\\d+");
	}

	/**
	 * Reverse the {@code input}.
	 * 
	 * @param input
	 *            the string to be reversed
	 * 
	 * @return the reversed string
	 */
	public static String reverse(final String input) {
		if (input == null) {
			return null;
		}

		return new StringBuilder(input).reverse().toString();
	}

	/**
	 * Reverses a string and reverses brackets in a way that open brackets are
	 * closed.
	 * 
	 * @param input
	 *            the string to be reversed
	 * 
	 * @return the reversed string
	 */
	public static String smartReverse(final String input) {
		if (input == null) {
			return null;
		}

		final int len = input.length();
		final StringBuffer dest = new StringBuffer(len);

		for (int i = len - 1; i >= 0; i--) {
			final char cur = input.charAt(i);

			if ('{' == cur) {
				dest.append('}');
			} else if ('(' == cur) {
				dest.append(')');
			} else if ('[' == cur) {
				dest.append(']');
			} else {
				dest.append(cur);
			}
		}
		return dest.toString();
	}

	/**
	 * Removes a specific pre- and suffix sequence from the {@code input}. The
	 * sequence is refersed at the end, that means that the string
	 * {@code %'Hallo'%} can be trimmed to {@code Hallo} by calling
	 * {@code removePreAndSuffixSequence("%'Hallo'%", "%'")}. If the string
	 * doesn't start witht the specified sequence nothing is done.
	 * 
	 * @param input
	 *            the input to be manipulated
	 * @param sequence
	 *            the sequence to be removed
	 * 
	 * @return the result
	 */
	public static String trimSequence(final String input, final String sequence) {
		if (input == null || sequence == null) {
			return null;
		}

		return trim(input, sequence, reverse(sequence));
	}

	/**
	 * The smart trimming replaces brackets within the sequence so that those
	 * are correctly removed. That means that {@code ['Hallo']} can be trimmed
	 * to {@code Hallo} by calling
	 * {@code removePreAndSuffixSequence(" '[Hallo]'}", "{'[")}
	 * 
	 * @param input
	 *            the input to be trimmed
	 * @param sequence
	 *            the sequence (prefix wise), brackets will be smartly reversed
	 * 
	 * @return the trimmed string
	 */
	public static String smartTrimSequence(final String input,
			final String sequence) {
		if (input == null || sequence == null) {
			return null;
		}

		return trim(input, sequence, smartReverse(sequence));
	}

	/**
	 * Removes the specified prefix and suffix from the string.
	 * 
	 * @param input
	 *            the string to be trimmed
	 * @param prefix
	 *            the prefix to be removed (can be {@code null} if just a suffix
	 *            should be removed)
	 * @param suffix
	 *            the suffix to be removed (can be {@code null} if just a suffix
	 *            should be removed)
	 * 
	 * @return the trimmed string
	 */
	public static String trim(final String input, final String prefix,
			final String suffix) {
		if (input == null) {
			return null;
		}

		String result = input;
		if (prefix != null && input.startsWith(prefix)) {
			result = result.replaceAll("^" + Pattern.quote(prefix), "");
		}
		if (suffix != null && input.endsWith(suffix)) {
			result = result.replaceAll(Pattern.quote(suffix) + "$", "");
		}

		return result;
	}

	/**
	 * Synonym for the concate method, see
	 * {@link Strings#concate(String, Object...)}.
	 * 
	 * @param separator
	 *            the separator to be used to separate the entries
	 * @param array
	 *            the <code>Array</code> to serialize or represent as string
	 * 
	 * @return a {@link String} with the entries of the {@link Collection}
	 *         separated by the passed <code>separator</code>
	 * 
	 * @see Strings#concate(String, Object...)
	 */
	public static <T> String join(final String separator, final T... array) {
		return concate(separator, array);
	}

	/**
	 * Synonym for the concate method, see
	 * {@link Strings#concate(String, Collection)}.
	 * 
	 * @param separator
	 *            the separator used for separation
	 * @param list
	 *            the {@link Collection} to serialize or represent as string
	 * 
	 * @return a {@link String} with the entries of the {@link Collection}
	 *         separated by the passed <code>separator</code>
	 * 
	 * @see Strings#concate(String, Collection)
	 */
	public static String join(final String separator,
			final Collection<? extends Object> list) {
		return concate(separator, list);
	}

	/**
	 * This method concatenates all value of an <code>Array</code> with the
	 * passed separator. You can use {@link String#split(String)} to reverse the
	 * operation. If the {@link Collection} contains <code>null</code> values,
	 * those values will be skipped.
	 * 
	 * @param separator
	 *            the separator to be used to separate the entries
	 * @param array
	 *            the <code>Array</code> to serialize or represent as string
	 * 
	 * @return a {@link String} with the entries of the {@link Collection}
	 *         separated by the passed <code>separator</code>
	 */
	public static <T> String concate(final String separator, final T... array) {
		final String concatedList;

		if (array == null) {
			concatedList = "";
		} else {
			final List<T> list = Arrays.asList(array);
			concatedList = concate(separator, list);
		}

		return concatedList;
	}

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

		// if the list is null there is nothing to concate
		if (list == null) {
			return "";
		}

		final StringBuilder sb = new StringBuilder();
		boolean first = true;
		for (Object obj : list) {
			if (obj == null) {
				continue;
			}

			if (!first) {
				sb.append(separator);
			} else {
				first = false;
			}
			sb.append(obj.toString());
		}

		return sb.toString();
	}

	/**
	 * Chunks a string into several substrings of same size
	 * 
	 * @param input
	 *            the input string to be chunked
	 * @param chunkSize
	 *            the size of one chunk
	 * @return the {@link List} of all the chunks for the <code>input</code>
	 */
	public static List<String> chunk(final String input, final int chunkSize) {

		// get the size of the text
		final int inputLength = input.length();

		// get the possible minimal amount of chunks
		final int minNumChunks = inputLength / chunkSize;

		// the amount of chunks really necessary
		final int numChunks = minNumChunks
				+ ((inputLength % chunkSize) == 0 ? 0 : 1);

		// create the list and add all the chunks
		final List<String> chunks = new ArrayList<String>(numChunks);
		for (int startIndex = 0; startIndex < inputLength; startIndex += chunkSize) {
			final int endIndex = Math.min(inputLength, startIndex + chunkSize);
			chunks.add(input.substring(startIndex, endIndex));
		}
		return chunks;
	}

	/**
	 * Checks if the specified string can be transformed to an integer and does
	 * so if possible. If impossible a {@code null} will be returned. The method
	 * also allows a leading {@code +} sign.
	 * 
	 * @param i
	 *            the string to be transformed
	 * 
	 * @return the integer represented by the string, otherwise {@code null}
	 */
	public static Integer isInteger(final String i) {
		if (i == null) {
			return null;
		}

		String modInt = i.trim();
		if ("".equals(modInt)) {
			return null;
		} else {
			final Matcher m = integerPattern.matcher(modInt);

			if (m.matches()) {
				if ("+".equals(m.group(1))) {
					modInt = modInt.replace("+", "");
				}

				final Integer parsedInt;
				try {
					parsedInt = Integer.valueOf(modInt);
					return parsedInt;
				} catch (final NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Checks if the specified string can be transformed to a long and does so
	 * if possible. If impossible a {@code null} will be returned.
	 * 
	 * @param l
	 *            the string to be transformed
	 * 
	 * @return the long represented by the string, otherwise {@code null}
	 */
	public static Long isLong(final String l) {
		if (l == null) {
			return null;
		}

		String modLong = l.trim();
		if ("".equals(modLong)) {
			return null;
		} else {
			final Matcher m = integerPattern.matcher(modLong);

			if (m.matches()) {
				if ("+".equals(m.group(1))) {
					modLong = modLong.replace("+", "");
				}

				final Long parsedLong;
				try {
					parsedLong = Long.valueOf(modLong);
					return parsedLong;
				} catch (final NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Checks if the specified string can be transformed to a {@code BigInteger}
	 * and does so if possible. If impossible a {@code null} will be returned.
	 * 
	 * @param bigInt
	 *            the string to be transformed
	 * 
	 * @return the {@code BigInteger} represented by the string, otherwise
	 *         {@code null}
	 */
	public static BigInteger isBigInteger(final String bigInt) {
		if (bigInt == null) {
			return null;
		}

		String modBigInt = bigInt.trim();
		if ("".equals(modBigInt)) {
			return null;
		} else {
			final Matcher m = integerPattern.matcher(modBigInt);

			if (m.matches()) {
				if ("+".equals(m.group(1))) {
					modBigInt = modBigInt.replace("+", "");
				}

				try {
					return new BigInteger(modBigInt);
				} catch (final NumberFormatException e) {
					return null;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Checks if the specified string can be transformed to a double and does so
	 * if possible. If impossible a {@code null} will be returned.
	 * 
	 * @param d
	 *            the string to be transformed
	 * 
	 * @return the double represented by the string, otherwise {@code null}
	 */
	public static Double isDouble(final String d) {
		if (d == null) {
			return null;
		}

		String modDouble = d.trim();
		if ("".equals(modDouble)) {
			return null;
		} else {
			final Matcher m = doublePattern.matcher(modDouble);

			if (m.matches()) {
				if ("+".equals(m.group(1))) {
					modDouble = modDouble.replace("+", "");
				}

				final Double pD;
				try {
					pD = Double.valueOf(modDouble);
				} catch (final NumberFormatException e) {
					return null;
				}
				if (pD.isInfinite() || pD.isNaN()) {
					return null;
				} else {
					return pD;
				}
			} else {
				return null;
			}
		}
	}

	/**
	 * Checks if the specified string can be transformed to a {@code BigDecimal}
	 * and does so if possible. If impossible a {@code null} will be returned.
	 * 
	 * @param bigDec
	 *            the string to be transformed
	 * 
	 * @return the {@code BigDecimal} represented by the string, otherwise
	 *         {@code null}
	 */
	public static BigDecimal isBigDecimal(final String bigDec) {
		if (bigDec == null) {
			return null;
		}

		String modBigDec = bigDec.trim();
		if ("".equals(modBigDec)) {
			return null;
		} else {

			if ('+' == modBigDec.charAt(0)) {
				modBigDec = modBigDec.replace("+", "");
			}

			try {
				return new BigDecimal(modBigDec);
			} catch (final NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * Synonym for {@link Dates#isDate(String)}.
	 * 
	 * @param d
	 *            the text to be checked
	 * 
	 * @return the parsed {@code Date} or {@code null} if parsing wasn't
	 *         possible
	 * 
	 * @see Dates#isDate(String)
	 * @see Dates#isDate(String, String[])
	 */
	public static Date isDate(final String d) {
		return Dates.isDate(d);
	}

	/**
	 * Repeats the character {@code c} {@code count}-times.
	 * 
	 * @param c
	 *            the character to repeat
	 * @param count
	 *            the amount of repetitions
	 * 
	 * @return the created string
	 */
	public static String repeat(final char c, final int count) {

		final char[] array = new char[count];
		for (int i = 0; i < count; i++) {
			array[i] = c;
		}
		return new String(array);
	}
}
