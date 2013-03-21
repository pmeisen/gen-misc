package net.meisen.general.genmisc.types;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Utility class for string manipulation
 * 
 * @author pmeisen
 * 
 */
public class Strings {

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
}
