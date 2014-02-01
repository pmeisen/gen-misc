package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.math.BigInteger;

import net.meisen.general.genmisc.types.Numbers;

import org.junit.Test;

/**
 * Tests the implementations of {@code Numbers}.
 * 
 * @author pmeisen
 * 
 */
public class TestNumbers {

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
}
