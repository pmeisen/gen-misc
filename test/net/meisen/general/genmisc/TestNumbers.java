package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.meisen.general.genmisc.types.Numbers;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests the implementations of {@code Numbers}.
 * 
 * @author pmeisen
 * 
 */
public class TestNumbers {

	/**
	 * Rule to evaluate exceptions
	 */
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Tests the mapping to integers.
	 */
	@Test
	public void testIntegerMapping() {
		assertEquals(new Integer(1),
				Numbers.mapToDataType(new Integer(1), Integer.class));
		assertEquals(new Integer(Short.MAX_VALUE),
				Numbers.mapToDataType(Short.MAX_VALUE, Integer.class));

		assertEquals(new Integer(1),
				Numbers.mapToDataType(new Double(1), Integer.class));
		assertEquals(new Integer(1000),
				Numbers.mapToDataType(new Double(1000), Integer.class));

		assertEquals(new Integer(1),
				Numbers.mapToDataType(BigInteger.valueOf(1), Integer.class));

		assertEquals(new Integer(1),
				Numbers.mapToDataType(BigDecimal.valueOf(1.0), Integer.class));
		assertEquals(new Integer(1),
				Numbers.mapToDataType(BigDecimal.valueOf(1), Integer.class));
		assertEquals(new Integer(1),
				Numbers.mapToDataType(new BigDecimal(1.0), Integer.class));
		assertEquals(new Integer(1),
				Numbers.mapToDataType(new BigDecimal(1), Integer.class));

		assertNull(Numbers.mapToDataType(Long.MAX_VALUE, Integer.class));
	}

	/**
	 * Tests the mapping to doubles.
	 */
	@Test
	public void testDoubleMapping() {
		assertEquals(new Double(1),
				Numbers.mapToDataType(new Double(1), Double.class));
		assertEquals(new Double(1),
				Numbers.mapToDataType(BigInteger.valueOf(1), Double.class));
		assertEquals(new Double(1.567),
				Numbers.mapToDataType(new BigDecimal(1.567), Double.class));
		assertEquals(new Double(1.5),
				Numbers.mapToDataType(BigDecimal.valueOf(1.5), Double.class));
		assertEquals(new Double(1),
				Numbers.mapToDataType(BigDecimal.valueOf(1.0), Double.class));
		assertEquals(new Double(1),
				Numbers.mapToDataType(BigDecimal.valueOf(1), Double.class));
		assertEquals(new Double(1),
				Numbers.mapToDataType(new BigDecimal(1.0), Double.class));
		assertEquals(new Double(1),
				Numbers.mapToDataType(new BigDecimal(1), Double.class));
		assertEquals(new Double(Double.MAX_VALUE), Numbers.mapToDataType(
				new BigDecimal(Double.MAX_VALUE), Double.class));

		assertNull(Numbers.mapToDataType(new BigDecimal(Long.MAX_VALUE + "."
				+ Long.MAX_VALUE), Double.class));
	}

	/**
	 * Tests the conversion from {@code Byte}, {@code Short}, and {@code Long}
	 * values to {@code Integer}.
	 */
	@Test
	public void testToIntCasts() {

		// test from byte
		assertEquals(100, Numbers.castToInt(((byte) 100)));
		assertEquals(Byte.MAX_VALUE, Numbers.castToInt(Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToInt(Byte.MIN_VALUE));

		// test from short
		assertEquals(100, Numbers.castToInt(((short) 100)));
		assertEquals(Short.MAX_VALUE, Numbers.castToInt(Short.MAX_VALUE));
		assertEquals(Short.MIN_VALUE, Numbers.castToInt(Short.MIN_VALUE));

		// test from long
		assertEquals(100, Numbers.castToInt(100l));
		assertEquals(100000000, Numbers.castToInt(100000000l));
		assertEquals(Integer.MAX_VALUE,
				Numbers.castToInt((long) Integer.MAX_VALUE));
		assertEquals(Integer.MIN_VALUE,
				Numbers.castToInt((long) Integer.MIN_VALUE));
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into an {@code Integer}.
	 */
	@Test
	public void testToIntCastExceptions() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the long value '" + Long.MAX_VALUE
				+ "' to an integer.");

		Numbers.castToInt(Long.MAX_VALUE);
	}

	/**
	 * Tests the conversion from {@code Byte}, {@code Integer}, and {@code Long}
	 * values to {@code Short}.
	 */
	@Test
	public void testToShortCasts() {

		// test from byte
		assertEquals(100, Numbers.castToShort(((byte) 100)));
		assertEquals(Byte.MAX_VALUE, Numbers.castToShort(Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToShort(Byte.MIN_VALUE));

		// test from short
		assertEquals(100, Numbers.castToShort(((int) 100)));
		assertEquals(Byte.MAX_VALUE, Numbers.castToShort((int) Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToShort((int) Byte.MIN_VALUE));

		// test from long
		assertEquals(100, Numbers.castToShort(100l));
		assertEquals(30000, Numbers.castToShort(30000l));
		assertEquals(Short.MAX_VALUE,
				Numbers.castToShort((long) Short.MAX_VALUE));
		assertEquals(Short.MIN_VALUE,
				Numbers.castToShort((long) Short.MIN_VALUE));
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into a {@code Short}.
	 */
	@Test
	public void testToShortCastExceptions1() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the long value '" + Long.MAX_VALUE
				+ "' to a short.");

		Numbers.castToShort(Long.MAX_VALUE);
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into a {@code Short}.
	 */
	@Test
	public void testToShortCastExceptions2() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the integer value '"
				+ Integer.MAX_VALUE + "' to a short.");

		Numbers.castToShort(Integer.MAX_VALUE);
	}

	/**
	 * Tests the conversion from {@code Short}, {@code Integer}, and
	 * {@code Long} values to {@code Byte}.
	 */
	@Test
	public void testToByteCasts() {

		// test from short
		assertEquals(50, Numbers.castToByte(((short) 50)));
		assertEquals(Byte.MAX_VALUE, Numbers.castToByte((short) Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToByte((short) Byte.MIN_VALUE));

		// test from integer
		assertEquals(100, Numbers.castToByte((100)));
		assertEquals(Byte.MAX_VALUE, Numbers.castToByte((int) Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToByte((int) Byte.MIN_VALUE));

		// test from long
		assertEquals(100, Numbers.castToByte(100l));
		assertEquals(Byte.MAX_VALUE, Numbers.castToByte((long) Byte.MAX_VALUE));
		assertEquals(Byte.MIN_VALUE, Numbers.castToByte((long) Byte.MIN_VALUE));
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into a {@code Byte}.
	 */
	@Test
	public void testToByteCastExceptions1() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the long value '" + Long.MAX_VALUE
				+ "' to a byte.");

		Numbers.castToByte(Long.MAX_VALUE);
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into a {@code Byte}.
	 */
	@Test
	public void testToByteCastExceptions2() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the integer value '"
				+ Integer.MAX_VALUE + "' to a byte.");

		Numbers.castToByte(Integer.MAX_VALUE);
	}

	/**
	 * Tests the {@code ArithmeticException} to be thrown when the value doesn't
	 * fit into a {@code Byte}.
	 */
	@Test
	public void testToByteCastExceptions3() {
		thrown.expect(ArithmeticException.class);
		thrown.expectMessage("Cannot convert the short value '"
				+ Short.MAX_VALUE + "' to a byte.");

		Numbers.castToByte(Short.MAX_VALUE);
	}

	/**
	 * Tests the primitive cast.
	 */
	@Test
	public void testPrimitiveCast() {
		Object[] array;

		// test the exact cast
		array = new Integer[] { 1, 2, 3, 4, 5 };
		final int[] res1 = Numbers.castArrayToInt(array);
		assertEquals(5, res1.length);

		array = new Short[] { 1, 2, 3 };
		final int[] res2 = Numbers.castArrayToInt(array);
		assertEquals(3, res2.length);
		final short[] res3 = Numbers.castArrayToShort(array);
		assertEquals(3, res3.length);
	}

	/**
	 * Tests the implementation of
	 * {@code Numbers#determineCommonType(Number...)}.
	 */
	@Test
	public void testDetermineCommonType() {
		assertEquals(Short.class,
				Numbers.determineCommonType((short) 5, (byte) 2));
		assertEquals(Byte.class,
				Numbers.determineCommonType((byte) 5, (byte) 2));
		assertEquals(Long.class, Numbers.determineCommonType(5l, 1));
		assertEquals(Integer.class, Numbers.determineCommonType(5, 1));
		assertEquals(BigInteger.class,
				Numbers.determineCommonType(BigInteger.valueOf(100l), 1));
		assertEquals(BigDecimal.class,
				Numbers.determineCommonType(BigInteger.valueOf(100l), 1d));
		assertEquals(BigInteger.class, Numbers.determineCommonType(
				BigInteger.valueOf(100l), BigInteger.valueOf(200l)));
		assertEquals(Float.class, Numbers.determineCommonType(100.0f, 200.0f));
		assertEquals(Float.class, Numbers.determineCommonType(5, 200.0f));
		assertEquals(Float.class, Numbers.determineCommonType(200.0f, 5));
	}
}
