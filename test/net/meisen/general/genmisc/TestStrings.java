package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;

import net.meisen.general.genmisc.resources.Resource;
import net.meisen.general.genmisc.types.Strings;

import org.junit.Test;

/**
 * Tests used to test the implemented functionality of the {@link Resource}
 * class.
 * 
 * @author pmeisen
 * 
 */
public class TestStrings {

	/**
	 * Test the chunking functionality
	 * 
	 * @see Strings#chunk(String, int)
	 */
	@Test
	public void testChunk() {
		String s = null;
		List<String> chunks = null;

		// Test the empty chunking
		s = "";
		assertEquals(0, Strings.chunk(s, 1).size());

		// Test the chunking of the exact size of the input
		s = "abc";
		chunks = Strings.chunk(s, s.length());
		assertEquals(s, chunks.get(0));
		assertEquals(1, chunks.size());

		// Tests chunking if the first is smaller than the size
		s = "abc";
		chunks = Strings.chunk(s, s.length() + 1);
		assertEquals(s, chunks.get(0));
		assertEquals(1, chunks.size());

		// Tests real chunking, i.e. a string will be partitioned
		s = "abc";
		chunks = Strings.chunk(s, 2);
		assertEquals("ab", chunks.get(0));
		assertEquals("c", chunks.get(1));
		assertEquals(2, chunks.size());

		// Tests chunking when two chunks fit completely
		s = "abcd";
		chunks = Strings.chunk(s, 2);
		assertEquals("ab", chunks.get(0));
		assertEquals("cd", chunks.get(1));
	}

	/**
	 * Test the concatenation functionality
	 * 
	 * @see Strings#concate(String, Collection)
	 * @see Strings#concate(String, Object...)
	 */
	@Test
	public void testConcate() {
		String s = null;

		s = Strings.concate("-");
		assertEquals(s, "");

		// concatenate some integer
		s = Strings.concate("-", 1, 2, 3, 4);
		assertEquals(s, "1-2-3-4");

		// concatenate some Strings
		s = Strings.concate(";", "A", "B");
		assertEquals(s, "A;B");
	}

	/**
	 * Tests the implementation of {@ode Strings#isInteger(String)}.
	 */
	@Test
	public void testIsInteger() {
		assertNull(Strings.isInteger(null));
		assertNull(Strings.isInteger(""));
		assertNull(Strings.isInteger("1" + Integer.MAX_VALUE));
		assertNull(Strings.isInteger("" + Integer.MIN_VALUE + "1"));
		assertNull(Strings.isInteger(".12"));

		assertNotNull(Strings.isInteger("+12312"));
		assertNotNull(Strings.isInteger("-12"));
		assertNotNull(Strings.isInteger("-12312"));
		assertNotNull(Strings.isInteger("" + Integer.MAX_VALUE));
		assertNotNull(Strings.isInteger("" + Integer.MIN_VALUE));

		assertEquals(21312312, (int) Strings.isInteger("+21312312"));
		assertEquals(-12, (int) Strings.isInteger("-12"));
		assertEquals(12123, (int) Strings.isInteger("12123"));
	}

	/**
	 * Tests the implementation of {@ode Strings#isLong(String)}.
	 */
	@Test
	public void testIsLong() {
		assertNull(Strings.isLong(null));
		assertNull(Strings.isLong(""));
		assertNull(Strings.isLong("1" + Long.MAX_VALUE));
		assertNull(Strings.isLong("" + Long.MIN_VALUE + "1"));
		assertNull(Strings.isLong(".12"));

		assertNotNull(Strings.isLong("+12312"));
		assertNotNull(Strings.isLong("-12"));
		assertNotNull(Strings.isLong("-12312"));
		assertNotNull(Strings.isLong("" + Integer.MAX_VALUE));
		assertNotNull(Strings.isLong("" + Integer.MIN_VALUE));
		assertNotNull(Strings.isLong("" + Long.MAX_VALUE));
		assertNotNull(Strings.isLong("" + Long.MIN_VALUE));

		assertEquals(21312312l, (long) Strings.isLong("+21312312"));
		assertEquals(-12l, (long) Strings.isLong("-12"));
		assertEquals(12123l, (long) Strings.isLong("12123"));
	}

	/**
	 * Tests the implementation of {@ode Strings#isBigInteger(String)}.
	 */
	@Test
	public void testIsBigInteger() {
		assertNull(Strings.isBigInteger(null));
		assertNull(Strings.isBigInteger(""));
		assertNull(Strings.isBigInteger(".12"));

		assertNotNull(Strings.isBigInteger("1" + Integer.MAX_VALUE));
		assertNotNull(Strings.isBigInteger("" + Integer.MIN_VALUE + "1"));
		assertNotNull(Strings.isBigInteger("+12312"));
		assertNotNull(Strings.isBigInteger("-12"));
		assertNotNull(Strings.isBigInteger("-12312"));
		assertNotNull(Strings.isBigInteger("" + Integer.MAX_VALUE));
		assertNotNull(Strings.isBigInteger("" + Integer.MIN_VALUE));
		assertNotNull(Strings
				.isBigInteger("124564897619849846165498949844651684984124564897619849846165498949844651684984124564897619849846165498949844651684984"));

		assertEquals(new BigInteger("1" + Integer.MAX_VALUE),
				Strings.isBigInteger("1" + Integer.MAX_VALUE));
		assertEquals(new BigInteger("-12"), Strings.isBigInteger("-12"));
		assertEquals(
				new BigInteger(
						"124564897619849846165498949844651684984124564897619849846165498949844651684984124564897619849846165498949844651684984"),
				Strings.isBigInteger("124564897619849846165498949844651684984124564897619849846165498949844651684984124564897619849846165498949844651684984"));
	}

	/**
	 * Tests the implementation of {@ode Strings#isDouble(String)}.
	 */
	@Test
	public void testIsDouble() {
		assertNull(Strings.isDouble(null));
		assertNull(Strings.isDouble(""));
		assertNull(Strings.isDouble("1" + Double.MAX_VALUE));
		assertNull(Strings.isDouble("" + Double.MAX_VALUE + "104"));

		assertNotNull(Strings.isDouble(".12"));
		assertNotNull(Strings.isDouble("-.12"));
		assertNotNull(Strings.isDouble("12312"));
		assertNotNull(Strings.isDouble("-.12"));
		assertNotNull(Strings.isDouble("-12312"));
		assertNotNull(Strings.isDouble("" + Double.MAX_VALUE));
		assertNotNull(Strings.isDouble("" + Double.MIN_VALUE));

		assertEquals(Double.MAX_VALUE,
				(double) Strings.isDouble("" + Double.MAX_VALUE), 0.0d);
		assertEquals(Double.MIN_VALUE,
				(double) Strings.isDouble("" + Double.MIN_VALUE), 0.0d);
	}

	/**
	 * Tests the implementation of {@ode Strings#isBigDecimal(String)}.
	 */
	@Test
	public void testIsBigDecimal() {
		assertNull(Strings.isBigDecimal(null));
		assertNull(Strings.isBigDecimal(""));

		assertNotNull(Strings.isBigDecimal("1" + Double.MAX_VALUE));
		assertNotNull(Strings.isBigDecimal("" + Double.MAX_VALUE + "10421"));
		assertNotNull(Strings.isBigDecimal(".12"));
		assertNotNull(Strings.isBigDecimal("-.12"));
		assertNotNull(Strings.isBigDecimal("12312"));
		assertNotNull(Strings.isBigDecimal("-.12"));
		assertNotNull(Strings.isBigDecimal("-12312"));
		assertNotNull(Strings.isBigDecimal("" + Double.MAX_VALUE));
		assertNotNull(Strings.isBigDecimal("" + Double.MIN_VALUE));
	}
}
