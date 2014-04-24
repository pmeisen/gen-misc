package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Random;

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

	/**
	 * Tests the comparison
	 */
	@Test
	public void testComparison() {
		assertEquals(0, Objects.compare(1, 1));
		assertEquals(0, Objects.compare("HALLO", "HALLO"));
		assertEquals(0, Objects.compare(this, this));

		// integers
		assertEquals(-1, Objects.compare(1, 2));
		assertEquals(1, Objects.compare(2, 1));

		// strings
		assertEquals(-1, Objects.compare("A", "B"));
		assertEquals(-1, Objects.compare("D", "F"));

		// mixed numbers
		assertEquals(-1, Objects.compare(5, 100l));
		assertEquals(1, Objects.compare(100, 5l));
		assertEquals(1, Objects.compare(1.05, 1));
		assertEquals(1, Objects.compare(1.00000005, 1.0f));
		final Random rnd = new Random();
		for (int i = 0; i < 100000; i++) {
			float nf = rnd.nextFloat();
			int ni = rnd.nextInt();
			double nd = rnd.nextDouble();
			long nl = rnd.nextLong();

			assertEquals(nf + "<" + ni, nf < ni, Objects.compare(nf, ni) == -1);
			assertEquals(nf + "<" + nd, nf < nd, Objects.compare(nf, nd) == -1);
			assertEquals(nf + "<" + nl, nf < nl, Objects.compare(nf, nl) == -1);

			assertEquals(ni + "<" + nd, ni < nd, Objects.compare(ni, nd) == -1);
			assertEquals(ni + "<" + nl, ni < nl, Objects.compare(ni, nl) == -1);

			assertEquals(nd + "<" + nl, nd < nl, Objects.compare(nd, nl) == -1);
		}

		// check if the comparison is total, we do this by mass-testing
		for (int i = 0; i < 100000; i++) {
			final Object o1 = new Object();
			final Object o2 = new Object();
			final Object o3 = new Object();

			// make sure objects are assumed to be equal
			assertEquals(0, Objects.compare(o1, o1));
			assertEquals(0, Objects.compare(o2, o2));
			assertEquals(0, Objects.compare(o3, o3));

			// make sure each comparison is total
			assertEquals(0, Objects.compare(o1, o2) + Objects.compare(o2, o1));
			assertEquals(0, Objects.compare(o1, o3) + Objects.compare(o3, o1));
			assertEquals(0, Objects.compare(o2, o3) + Objects.compare(o3, o2));

			// o1 < o2 && o2 < o3 ==> o1 < o3
			if (Objects.compare(o1, o2) == -1 && Objects.compare(o2, o3) == -1) {
				assertEquals(-1, Objects.compare(o1, o3));
				assertEquals(1, Objects.compare(o3, o1));
			}
			// o1 < o3 && o3 < o2 ==> o3 < o2
			else if (Objects.compare(o1, o3) == -1
					&& Objects.compare(o3, o2) == -1) {
				assertEquals(-1, Objects.compare(o3, o2));
				assertEquals(1, Objects.compare(o2, o3));
			}
			// o2 < o1 && o1 < o3 ==> o2 < o3
			else if (Objects.compare(o2, o1) == -1
					&& Objects.compare(o1, o3) == -1) {
				assertEquals(-1, Objects.compare(o2, o3));
				assertEquals(1, Objects.compare(o3, o2));
			}
			// o2 < o3 && o3 < o1 ==> o2 < o1
			else if (Objects.compare(o2, o3) == -1
					&& Objects.compare(o3, o1) == -1) {
				assertEquals(-1, Objects.compare(o2, o1));
				assertEquals(1, Objects.compare(o1, o2));
			}
			// o3 < o1 && o1 < o2 ==> o3 < o2
			else if (Objects.compare(o3, o1) == -1
					&& Objects.compare(o1, o2) == -1) {
				assertEquals(-1, Objects.compare(o3, o2));
				assertEquals(1, Objects.compare(o2, o3));
			} // o3 < o2 && o2 < o1 ==> o3 < o1
			else if (Objects.compare(o3, o2) == -1
					&& Objects.compare(o2, o1) == -1) {
				assertEquals(-1, Objects.compare(o3, o1));
				assertEquals(1, Objects.compare(o1, o3));
			} else {
				fail("Unable to determine any order: o1-o2 ("
						+ Objects.compare(o1, o2) + "), o1-o3 ("
						+ Objects.compare(o1, o3) + "), o2-o3 ("
						+ Objects.compare(o2, o3) + ") + .");
			}
		}

	}
}
