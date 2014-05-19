package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Arrays;
import java.util.Random;

import net.meisen.general.genmisc.types.Streams;
import net.meisen.general.genmisc.types.Streams.ByteResult;

import org.junit.Test;

/**
 * The test specified for the <code>Streams</code>
 * 
 * @author pmeisen
 * 
 */
public class TestStreams {

	/**
	 * Tests the implementation of the
	 * {@link Streams#writeStringToStream(String, java.io.OutputStream)} method
	 */
	@Test
	public void testWriteStringToStream() {
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		final String content = "This is a äöüß Test÷";

		// write it
		Streams.writeStringToStream(content, baos);

		// test it
		assertEquals(baos.toString(), content);
	}

	/**
	 * Tests the implementation of the encoding guesses for some files.
	 * 
	 * @throws IOException
	 *             if the file cannot be accessed
	 */
	@Test
	public void testGuessEncoding() throws IOException {
		InputStream is;
		String enc;

		// EMPTY
		is = getClass().getResourceAsStream("encodedFiles/Empty.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals(System.getProperty("file.encoding"), enc);

		// ASCII
		is = getClass().getResourceAsStream("encodedFiles/ASCII.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("US-ASCII", enc);

		// Cp1252
		is = getClass().getResourceAsStream("encodedFiles/Cp1252.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("Cp1252", enc);

		// UTF8 with BOM
		is = getClass().getResourceAsStream("encodedFiles/UTF8_BOM.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("UTF-8", enc);

		// UTF8 without BOM
		is = getClass().getResourceAsStream("encodedFiles/UTF8_noBOM.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("UTF-8", enc);

		// UTF16 BigEndian
		is = getClass().getResourceAsStream("encodedFiles/UCS-2_BigEndian.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("UTF-16BE", enc);

		// UTF16 LittleEndian
		is = getClass().getResourceAsStream(
				"encodedFiles/UCS-2_LittleEndian.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("UTF-16LE", enc);
	}

	/**
	 * Tests the implementation of {@link Streams#combineBytes(byte[][])}.
	 */
	@Test
	public void testCombineBytes() {

		// null if null is passed
		assertNull(Streams.combineBytes((byte[][]) null));

		// if a null array is passed it is ignored
		assertTrue(Arrays.equals(new byte[0],
				Streams.combineBytes((byte[]) null)));

		// test the combination
		byte[] input;

		input = new byte[] { 0x3, 0x2c };
		assertTrue(Arrays.equals(input,
				Streams.combineBytes((byte[]) null, input)));

		assertTrue(Arrays.equals(
				new byte[] { 0x3, 0x2c, 0x3, 0x2c },
				Streams.combineBytes(new byte[] { 0x3, 0x2c }, new byte[] {
						0x3, 0x2c })));
	}

	/**
	 * Tests the implementation of the short mappers, i.e.
	 * {@link Streams#shortToByte(short)} and
	 * {@link Streams#byteToShort(byte[])}.
	 */
	@Test
	public void testShortByte() {
		byte[] byteArray;
		short value;

		for (short i = Short.MIN_VALUE;; i++) {
			byteArray = Streams.shortToByte((short) i);
			value = Streams.byteToShort(byteArray);
			assertEquals((short) i, value);

			if (i == Short.MAX_VALUE) {
				break;
			}
		}
	}

	/**
	 * Tests the implementation of the int mappers, i.e.
	 * {@link Streams#intToByte(int)} and {@link Streams#byteToInt(byte[])}.
	 */
	@Test
	public void testIntByte() {
		byte[] byteArray;
		int value;

		for (int i = Integer.MIN_VALUE;; i++) {
			byteArray = Streams.intToByte(i);
			value = Streams.byteToInt(byteArray);

			assertEquals(i, value);

			if (i == Integer.MAX_VALUE) {
				break;
			}
		}
	}

	/**
	 * Tests the implementation of the long mappers, i.e.
	 * {@link Streams#longToByte(long)} and {@link Streams#byteToLong(byte[])}.
	 */
	@Test
	public void testLongByte() {
		byte[] byteArray;
		long value;

		// test the boundaries
		byteArray = Streams.longToByte(Long.MIN_VALUE);
		value = Streams.byteToLong(byteArray);
		assertEquals(Long.MIN_VALUE, value);

		byteArray = Streams.longToByte(Long.MAX_VALUE);
		value = Streams.byteToLong(byteArray);
		assertEquals(Long.MAX_VALUE, value);

		byteArray = Streams.longToByte(0);
		value = Streams.byteToLong(byteArray);
		assertEquals(0, value);

		// pick some random numbers
		final Random rnd = new Random();
		for (int i = 0; i < Integer.MAX_VALUE; i++) {
			final long rndValue = rnd.nextLong();

			byteArray = Streams.longToByte(rndValue);
			value = Streams.byteToLong(byteArray);

			assertEquals(rndValue, value);
		}
	}

	/**
	 * Tests the implementation of the string mappers, i.e.
	 * {@link Streams#stringToByte(String)} and
	 * {@link Streams#byteToString(byte[])}.
	 */
	@Test
	public void testStringByte() {
		byte[] byteArray;
		String value;

		byteArray = Streams.stringToByte("");
		value = Streams.byteToString(byteArray);
		assertEquals("", value);

		byteArray = Streams.stringToByte("Hello");
		value = Streams.byteToString(byteArray);
		assertEquals("Hello", value);

		byteArray = Streams.stringToByte("äüöüöäüß?\"");
		value = Streams.byteToString(byteArray);
		assertEquals("äüöüöäüß?\"", value);
	}

	/**
	 * Tests the implementation of the object mappers, i.e.
	 * {@link Streams#objectToByte(Object)} and
	 * {@link Streams#byteToObject(byte[])}.
	 */
	@Test
	public void testObjectByte() {
		byte[] byteArray;
		Object org;
		ByteResult value;

		// test an empty string
		org = "";
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertTrue(Streams.serializeObject(org).length > byteArray.length);

		// test a string
		org = "äüößAÜÖ!?  ";
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertTrue(Streams.serializeObject(org).length > byteArray.length);

		// test a byte
		org = Byte.MIN_VALUE;
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertTrue(Streams.serializeObject(org).length > byteArray.length);

		// test a short
		org = Short.MAX_VALUE;
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertTrue(Streams.serializeObject(org).length > byteArray.length);

		// test null
		org = null;
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertEquals(1, byteArray.length);

		// test an object
		org = new Date();
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertEquals(Streams.serializeObject(org).length + 1
				+ Streams.SIZEOF_INT, byteArray.length);

		// test some combination
		org = new Date();
		byteArray = Streams.combineBytes(Streams.objectToByte(org),
				Streams.objectToByte(Byte.MIN_VALUE),
				Streams.objectToByte("This is a test!"),
				Streams.objectToByte(Integer.MIN_VALUE),
				Streams.objectToByte(Long.MAX_VALUE));

		final Object[] objects = new Object[5];
		int counter = 0;
		int offset = 0;
		while (offset < byteArray.length) {
			value = Streams.byteToObject(byteArray, offset);
			offset = value.nextPos;

			objects[counter] = value.object;
			counter++;
		}

		assertEquals(org, objects[0]);
		assertEquals(Byte.MIN_VALUE, objects[1]);
		assertEquals("This is a test!", objects[2]);
		assertEquals(Integer.MIN_VALUE, objects[3]);
		assertEquals(Long.MAX_VALUE, objects[4]);
	}
}
