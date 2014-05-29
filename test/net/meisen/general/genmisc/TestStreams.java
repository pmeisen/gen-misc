package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import net.meisen.general.genmisc.resources.IByteBufferReader;
import net.meisen.general.genmisc.resources.WrappedByteBufferReader;
import net.meisen.general.genmisc.types.Numbers;
import net.meisen.general.genmisc.types.Streams;
import net.meisen.general.genmisc.types.Streams.ByteResult;
import net.meisen.general.genmisc.types.Strings;

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
	 * {@link Streams#doubleToByte(double)} and
	 * {@link Streams#byteToDouble(byte[])}.
	 */
	@Test
	public void testDoubleByte() {
		byte[] byteArray;
		double value;

		byteArray = Streams.doubleToByte(Double.MAX_VALUE);
		value = Streams.byteToDouble(byteArray);
		assertEquals(Double.MAX_VALUE, value, 0.0);

		byteArray = Streams.doubleToByte(Double.MIN_VALUE);
		value = Streams.byteToDouble(byteArray);
		assertEquals(Double.MIN_VALUE, value, 0.0);

		byteArray = Streams.doubleToByte(Double.NaN);
		value = Streams.byteToDouble(byteArray);
		assertEquals(Double.NaN, value, 0.0);

		byteArray = Streams.doubleToByte(Double.NEGATIVE_INFINITY);
		value = Streams.byteToDouble(byteArray);
		assertEquals(Double.NEGATIVE_INFINITY, value, 0.0);

		byteArray = Streams.doubleToByte(500.120312803921);
		value = Streams.byteToDouble(byteArray);
		assertEquals(500.120312803921, value, 0.0);
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

		// test some objects
		org = new Date();
		byteArray = Streams.objectToByte(org);
		value = Streams.byteToObject(byteArray);
		assertEquals(org, value.object);
		assertEquals(byteArray.length, value.nextPos);
		assertEquals(Streams.serializeObject(org).length + 1
				+ Streams.SIZEOF_INT, byteArray.length);

		org = UUID.randomUUID();
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

		// write some objects in an array and read those
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

	/**
	 * Tests the implementation of {@link Streams#writeAllObjects(Object...)}
	 * and {@link Streams#readAllObjects(byte[])}.
	 */
	@Test
	public void testWriteAndReadAll() {

		final List<Object> objects = new ArrayList<Object>();
		objects.add(5l);
		objects.add(new Date());
		objects.add("äüößÄÖÜ+\"*");
		objects.add((byte) 5);
		objects.add(21000);
		objects.add(null);
		objects.add(UUID.randomUUID());
		objects.add(Strings.repeat('A', Short.MAX_VALUE));

		// test reading all
		final byte[] res = Streams.writeAllObjects(objects);
		assertEquals(objects, Streams.readAllObjects(res));

		// test the offset
		assertEquals(objects.subList(1, objects.size()),
				Streams.readAllObjects(res, Streams.objectSize(Long.class)));
	}

	/**
	 * 
	 */
	@Test
	public void testReadNextObject() {

		// test the reading of three sample objects
		final byte[] sampleFile = Streams
				.combineBytes(Streams.objectToByte(5),
						Streams.objectToByte(500l),
						Streams.objectToByte("Hello World"));
		final WrappedByteBufferReader ret = new WrappedByteBufferReader(
				ByteBuffer.wrap(sampleFile));

		final Object o1 = Streams.readNextObject(ret);
		assertEquals(new Integer(5), o1);
		final Object o2 = Streams.readNextObject(ret);
		assertEquals(new Long(500), o2);
		final Object o3 = Streams.readNextObject(ret);
		assertEquals(new String("Hello World"), o3);

		boolean exception = false;
		try {
			Streams.readNextObject(new WrappedByteBufferReader(ByteBuffer
					.wrap(new byte[0])));
		} catch (final IllegalArgumentException e) {
			assertTrue(e.getMessage().contains(
					"buffer does not contain any object"));
			exception = true;
		}
		assertTrue(exception);
	}

	/**
	 * Tests an instance of each {@link Streams#BYTE_TYPES}.
	 * 
	 * @throws Exception
	 *             if an exception occurred
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void testAllTypes() throws Exception {
		for (final Class<?> clazz : Streams.BYTE_TYPES) {
			final Object o;

			// generate the needed values
			if (Object.class.equals(clazz)) {
				continue;
			} else if (Number.class.isAssignableFrom(clazz)) {
				o = Numbers.castToNumber(100, (Class<? extends Number>) clazz);
			} else if (Boolean.class.equals(clazz)) {
				o = true;
			} else if (String.class.equals(clazz)) {
				o = "Hello World";
			} else {
				o = clazz.newInstance();
			}

			final byte[] rep = Streams.writeAllObjects(o);
			final ArrayList<Object> res = Streams.readAllObjects(rep);
			assertEquals(1, res.size());
			assertEquals(o, res.get(0));

			/*
			 * all the representative should be smaller than the typical object
			 * representation
			 */
			final byte[] oRep = Streams.serializeObject(o);
			assertTrue(oRep.length + " > " + rep.length + " (" + clazz + ")",
					oRep.length > rep.length);

			// Strings have a dynamic overhead
			if (String.class.equals(clazz)) {
				continue;
			}

			/*
			 * check the overhead and the size, only String and Object are
			 * dynamic
			 */
			assertEquals(Streams.classOverhead(), Streams.objectOverhead(clazz));
			assertTrue(Streams.objectSize(clazz) > 0);
		}
	}

	/**
	 * Tests the reading of a file using the {@code FileByteBufferReader} and
	 * the {@code Streams#readNextObject(IByteBufferReader)} and
	 * {@code Streams#objectToByte(Object)}.
	 * 
	 * @throws IOException
	 *             if the file cannot be created
	 */
	@Test
	public void testStreamsWithByteBufferReader() throws IOException {

		// create a tmpFile
		final File tmpFile = new File(System.getProperty("java.io.tmpdir"),
				UUID.randomUUID().toString());
		tmpFile.createNewFile();
		final FileOutputStream fos = new FileOutputStream(tmpFile);
		for (int i = 0; i < 50000; i++) {
			fos.write(Streams.objectToByte(i));
		}
		fos.flush();
		fos.close();

		final IByteBufferReader reader = Streams
				.createByteBufferReader(tmpFile);
		for (int i = 0; i < 50000; i++) {
			assertEquals(i, Streams.readNextObject(reader));
		}
		reader.close();

		assertTrue(tmpFile.delete());
	}
}
