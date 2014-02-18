package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import net.meisen.general.genmisc.types.Objects;

import org.junit.Test;

/**
 * Test the implementation of {@code Objects}.
 * 
 * @author pmeisen
 * 
 */
public class TestObjects {

	/**
	 * Tests the cast.
	 */
	@Test
	public void testCast() {
		Object[] array;

		// test the exact cast
		array = new Integer[] { 1, 2, 3, 4, 5 };
		final Integer[] res1 = Objects.castArray(array, Integer.class);
		assertEquals(5, res1.length);

		array = new String[] { "Hi", "Du", "Ei" };
		final String[] res2 = Objects.castArray(array, String.class);
		assertEquals(3, res2.length);

		// test interface casts
		array = new Integer[] { 1, 2, 3, 4, 5 };
		final Number[] res3 = Objects.castArray(array, Number.class);
		assertEquals(5, res3.length);

		// test an exception
		array = new Integer[] { 1, 2, 3, 4, 5 };
		try {
			Objects.castArray(array, Double.class);
			fail("Exception not thrown.");
		} catch (final Exception e) {
			assertTrue(ArrayStoreException.class.equals(e.getClass()));
		}
	}
}
