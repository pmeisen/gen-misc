package net.meisen.general.genmisc.types;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

import net.meisen.general.genmisc.unicode.UnicodeReader;

/**
 * Utility class to work with all kind of streams
 * 
 * @author pmeisen
 * 
 */
public class Streams {
	/**
	 * Array of classes, which have a special byte-representation (with
	 * exception of Object).
	 */
	public static final Class<?>[] BYTE_TYPES;
	static {
		BYTE_TYPES = new Class<?>[] { Object.class, Byte.class, Short.class,
				Integer.class, Long.class, String.class };

		// make sure the length is valid and can be held within a byte
		Numbers.castToByte(BYTE_TYPES.length);
	}

	/**
	 * The size of a byte within the byte representation
	 */
	public static final int SIZEOF_BYTE = Byte.SIZE / Byte.SIZE;
	/**
	 * The size of a short within the byte representation
	 */
	public static final int SIZEOF_SHORT = Short.SIZE / Byte.SIZE;
	/**
	 * The size of an integer within the byte representation
	 */
	public static final int SIZEOF_INT = Integer.SIZE / Byte.SIZE;
	/**
	 * The size of a long within the byte representation
	 */
	public static final int SIZEOF_LONG = Long.SIZE / Byte.SIZE;

	private static class EncodingResult {
		/**
		 * The encoding determined
		 */
		public String encoding;
		/**
		 * <code>true</code> if the encoding has to be validated against the
		 * content
		 */
		public boolean validateByContent;

		public EncodingResult(final String encoding,
				final boolean validateByContent) {
			this.encoding = encoding;
			this.validateByContent = validateByContent;
		}
	}

	/**
	 * Result of a byte-deserialization.
	 * 
	 * @author pmeisen
	 * 
	 */
	public static class ByteResult {
		/**
		 * The object read.
		 */
		public final Object object;
		/**
		 * The next position to read from
		 */
		public final int nextPos;

		/**
		 * Constructor to specify the object and the nextPos to read
		 * 
		 * @param object
		 * @param nextPos
		 */
		public ByteResult(final Object object, final int nextPos) {
			this.object = object;
			this.nextPos = nextPos;
		}
	}

	/**
	 * Gets an identifier for some specific (i.e. deserializable) classes.
	 * 
	 * @param clazz
	 *            the class to get the number for
	 * 
	 * @return the number of the class
	 */
	public static byte getByteTypeNr(final Class<?> clazz) {

		if (clazz == null) {
			return (byte) -1;
		} else {
			byte objectClazzPos = -1;
			for (byte i = 0; i < BYTE_TYPES.length; i++) {
				final Class<?> byteType = BYTE_TYPES[i];

				if (byteType.equals(clazz)) {
					return i;
				} else if (Object.class.equals(byteType)) {
					objectClazzPos = i;
				}
			}

			// if we didn't find one return the Object.class
			return objectClazzPos;
		}
	}

	/**
	 * Copies the {@link InputStream} to the {@link OutputStream}
	 * 
	 * @param input
	 *            the source
	 * @param output
	 *            the destination
	 * @throws IOException
	 *             if one of the streams cannot be read or written
	 */
	public static void copyStream(final InputStream input,
			final OutputStream output) throws IOException {
		byte[] buffer = new byte[8];

		int len = -1;
		while ((len = input.read(buffer)) != -1) {
			output.write(buffer, 0, len);
		}
	}

	/**
	 * Closes a {@link InputStream}, {@link OutputStream}, {@link Writer} or
	 * {@link Reader} without throwing an error if closing fails
	 * 
	 * @param obj
	 *            the IO-object to be closed
	 * @return the {@link IOException} which was thrown, or <code>null</code> if
	 *         none was thrown
	 */
	public static IOException closeIO(final Object obj) {
		if (obj instanceof OutputStream) {
			try {
				((OutputStream) obj).close();
			} catch (final IOException e) {
				return e;
			}
		} else if (obj instanceof InputStream) {
			try {
				((InputStream) obj).close();
			} catch (final IOException e) {
				return e;
			}
		} else if (obj instanceof Writer) {
			try {
				((Writer) obj).close();
			} catch (final IOException e) {
				return e;
			}
		} else if (obj instanceof Reader) {
			try {
				((Reader) obj).close();
			} catch (final IOException e) {
				return e;
			}
		}

		return null;
	}

	/**
	 * Creates a file based on a <code>InputStream</code>. The stream will be
	 * closed by this method and has to be recreated if used any further.
	 * 
	 * @param sourceStream
	 *            the input stream which should be copied
	 * @param destFile
	 *            the output file
	 * 
	 * @throws IOException
	 *             if the file cannot be accessed
	 * 
	 * @see Files#copyStreamToFile(InputStream, File)
	 */
	public static void copyStreamToFile(final InputStream sourceStream,
			final File destFile) throws IOException {
		Files.copyStreamToFile(sourceStream, destFile);
	}

	/**
	 * Creates a file based on a <code>InputStream</code>.
	 * 
	 * @param sourceStream
	 *            the input stream which should be copied
	 * @param destFile
	 *            the output file
	 * @param closeStream
	 *            defines if the {@code sourceStream} should be closed after it
	 *            is successfully copied; {@code true} to close the
	 *            {@code sourceStream}, otherwise {@code false}
	 * 
	 * @throws IOException
	 *             if the file cannot be accessed
	 * 
	 * @see Files#copyStreamToFile(InputStream, File, boolean)
	 */
	public static void copyStreamToFile(final InputStream sourceStream,
			final File destFile, final boolean closeStream) throws IOException {
		Files.copyStreamToFile(sourceStream, destFile, closeStream);
	}

	/**
	 * Writes a string to the specified <code>OutputStream</code>.
	 * 
	 * @param input
	 *            the string to be written
	 * @param output
	 *            the <code>OutputStream</code> to write to
	 */
	public static void writeStringToStream(final String input,
			final OutputStream output) {
		final PrintStream printStream = new PrintStream(output);
		printStream.print(input);
		closeIO(printStream);
	}

	/**
	 * Reads a string form a stream using the system's default encoding (i.e.
	 * charset).
	 * 
	 * @param stream
	 *            the {@link InputStream} to read from, the stream is not closed
	 *            after reading (i.e. call {@link InputStream#close()}
	 * 
	 * @return the {@link String} read from the {@link InputStream}
	 * 
	 * @throws IOException
	 *             if the {@link InputStream} throws such an exception
	 */
	public static String readFromStream(final InputStream stream)
			throws IOException {
		return readFromStream(stream, null);
	}

	/**
	 * Reads a string form a stream using the specified encoding (i.e. charset).
	 * If {@code encoding} is {@code null} the default encoding is used.
	 * 
	 * @param stream
	 *            the {@link InputStream} to read from, the stream is not closed
	 *            after reading (i.e. call {@link InputStream#close()}
	 * @param encoding
	 *            the encoding of the stream
	 * 
	 * @return the {@link String} read from the {@link InputStream}
	 * 
	 * @throws IOException
	 *             if the {@link InputStream} throws such an exception
	 */
	public static String readFromStream(final InputStream stream,
			final String encoding) throws IOException {
		final InputStreamReader ir = encoding == null ? new InputStreamReader(
				stream) : new InputStreamReader(stream, encoding);
		final BufferedReader reader = new BufferedReader(ir);

		try {
			final StringBuffer data = new StringBuffer(1024);

			char[] buf = new char[1024];
			int numRead = 0;
			while ((numRead = reader.read(buf)) != -1) {
				final String readData = String.valueOf(buf, 0, numRead);
				data.append(readData);
				buf = new char[1024];
			}

			if (encoding == null) {

			}
			return data.toString();
		} catch (final IOException e) {
			throw e;
		} finally {
			closeIO(reader);
			closeIO(ir);
		}
	}

	/**
	 * Reads <code>Properties</code> from the passed <code>InputStream</code>.
	 * Make sure that the properties do not contain any UTF-8 characters,
	 * otherwise have a look at the see section.
	 * 
	 * @param in
	 *            the <code>InputStream</code> to read the properties from
	 * 
	 * @return the read <code>Properties</code>
	 * 
	 * @throws IOException
	 *             if the data could not be read from the
	 *             <code>InputStream</code>
	 * 
	 * @see UnicodeReader
	 */
	public static Properties readPropertiesFromStream(final InputStream in)
			throws IOException {

		// load the properties
		final Properties prop = new Properties();
		prop.load(in);

		// close the stream
		Streams.closeIO(in);

		return prop;
	}

	/**
	 * Copies the <code>inputStream</code> into a <code>byte[]</code>. Uses a
	 * buffer of size 4096 to copy the stream.
	 * 
	 * @param inputStream
	 *            the <code>InputStream</code> to be copied
	 * 
	 * @return the content of the <code>InputStream</code> as
	 *         <code>byte[]</code>
	 * 
	 * @throws IOException
	 *             if the <code>inputStream</code> could not be read or the data
	 *             not be flushed
	 */
	public static byte[] copyStreamToByteArray(final InputStream inputStream)
			throws IOException {
		return copyStreamToByteArray(inputStream, 4096);
	}

	/**
	 * Copies the <code>inputStream</code> into a <code>byte[]</code>.
	 * 
	 * @param inputStream
	 *            the <code>InputStream</code> to be copied
	 * @param bufferSize
	 *            the size of the buffer to be used
	 * 
	 * @return the content of the <code>InputStream</code> as
	 *         <code>byte[]</code>
	 * 
	 * @throws IOException
	 *             if the <code>inputStream</code> could not be read or the data
	 *             not be flushed
	 */
	public static byte[] copyStreamToByteArray(final InputStream inputStream,
			final int bufferSize) throws IOException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		int nRead;
		byte[] data = new byte[bufferSize];

		while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		buffer.flush();
		Streams.closeIO(buffer);

		return buffer.toByteArray();
	}

	/**
	 * Guesses the encoding of the specified <code>inStream</code>. If there are
	 * no indices to guess a encoding the default encoding will be returned,
	 * which can be <code>null</code>. If <code>null</code> is passed the
	 * system's default encoding ( <code>file.encoding</code>) will be used.
	 * 
	 * @param inStream
	 *            the stream to guess the encoding of
	 * @param defaultEncoding
	 *            the default encoding to use, can be <code>null</code>
	 * 
	 * @return the guessed encoding
	 * 
	 * @throws IOException
	 *             if the <code>InputStream</code> cannot be accessed
	 */
	public static String guessEncoding(final InputStream inStream,
			final String defaultEncoding) throws IOException {
		final String defEnc = defaultEncoding == null ? System
				.getProperty("file.encoding") : defaultEncoding;

		// if we don't have any stream return the default directly
		if (inStream == null) {
			return defEnc;
		}

		// we read the complete file into a byte-array
		final byte[] content = copyStreamToByteArray(inStream);

		// get the first four bytes
		final byte[] head = new byte[4];
		int read = -1;
		for (int i = 0; i < 4; i++) {
			if (content.length > i) {
				head[i] = content[i];
				read = i + 1;
			} else {
				head[i] = 0x00;
			}
		}

		// try to map the first bytes
		final EncodingResult encResult = determineEncoding(head[0], head[1],
				head[2], head[3], read, defEnc);

		// check if we should read the content just to be sure
		if (encResult.validateByContent) {

			// validate the content by reading the buffer
			if (!validateEncoding(content, encResult.encoding)) {
				encResult.encoding = Charset.forName("UTF-8").toString();

				// check if it might be UTF-8 without a BOM, otherwise use the
				// default
				if (!validUTF8(content)
						|| !validateEncoding(content, encResult.encoding)) {
					encResult.encoding = defEnc;
				}
			}
		}

		// if we still don't know set the default
		if (encResult.encoding == null) {
			encResult.encoding = defEnc;
		}

		// close the stream
		closeIO(inStream);

		return encResult.encoding;
	}

	/**
	 * Validates the <code>InputStream</code> against the passed
	 * <code>encoding</code>. Validation is done by decoding each byte of the
	 * <code>InputStream</code> using the specified <code>encoding</code>.
	 * 
	 * @param content
	 *            the content to validate the <code>encoding</code> against
	 * @param encoding
	 *            the encoding to be validated
	 * 
	 * @return <code>true</code> if all the characters of the
	 *         <code>InputStream</code> could be decoded using the specified
	 *         <code>encoding</code>
	 * 
	 * @see Charset#newDecoder()
	 * 
	 * @throws IOException
	 *             if the <code>InputStream</code> cannot be accessed
	 */
	public static boolean validateEncoding(final byte[] content,
			final String encoding) throws IOException {

		// create a decoder for the specified encoding and check if it can
		// decode
		final CharsetDecoder d = Charset.forName(encoding).newDecoder();

		// decode the content we read
		try {
			d.decode(ByteBuffer.wrap(content));
		} catch (final MalformedInputException e) {
			return false;
		}

		// it worked we could decode
		return true;
	}

	/**
	 * Checks if the content contains only valid UTF-8 characters.
	 * 
	 * @param content
	 *            the content to be checked
	 * 
	 * @return <code>true</code> if the content is a valid UTF-8 content,
	 *         otherwise <code>false</code>
	 */
	protected static boolean validUTF8(final byte[] content) {
		int i = 0, end;
		for (int j = content.length; i < j; i++) {
			int octet = content[i];
			if ((octet & 0x80) == 0) {
				continue; // ASCII
			}

			// Check for UTF-8 leading byte
			if ((octet & 0xE0) == 0xC0) {
				end = i + 1;
			} else if ((octet & 0xF0) == 0xE0) {
				end = i + 2;
			} else if ((octet & 0xF8) == 0xF0) {
				end = i + 3;
			} else {
				// Java only supports BMP so 3 is max
				return false;
			}

			while (i < end) {
				i++;
				octet = content[i];
				if ((octet & 0xC0) != 0x80) {
					// Not a valid trailing byte
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Determines the encoding based on the first four bytes. If not
	 * determinable the default will be returned, or US-ASCII will be returned
	 * so that a decoder will fail. More detailed the method will try to
	 * determine the encoding, if no bytes (i.e. the amount of read bytes is to
	 * small) are available the default will be returned, otherwise US-ASCII
	 * will be returned if the encoding couldn't be determined by the passed
	 * bytes.
	 * 
	 * @param b0
	 *            the first byte
	 * @param b1
	 *            the second byte
	 * @param b2
	 *            the third byte
	 * @param b3
	 *            the fourth byte
	 * @param read
	 *            the amount of bytes really read (might be less than 4)
	 * @param defEnc
	 *            the default encoding to be used if not determinable
	 * 
	 * @return the determined encoding
	 */
	protected static EncodingResult determineEncoding(final byte b0,
			final byte b1, final byte b2, final byte b3, final int read,
			final String defEnc) {
		String encoding = null;
		boolean validateByContent = false;

		// create an array of the read bytes (of exactly that size)
		final byte[] bytes;
		if (read <= 0) {
			encoding = defEnc;
			validateByContent = false;
			bytes = null;
		} else if (read == 1) {
			bytes = new byte[] { b0 };
		} else if (read == 2) {
			bytes = new byte[] { b0, b1 };
		} else if (read == 3) {
			bytes = new byte[] { b0, b1, b2 };
		} else if (read == 4) {
			bytes = new byte[] { b0, b1, b2, b3 };
		} else {
			encoding = defEnc;
			validateByContent = true;
			bytes = null;
		}

		// check the bytes if we have any
		if (bytes != null) {
			if (doByteMatch(bytes, 0x00, 0x00, 0xFE, 0xFF)) {
				encoding = Charset.forName("UTF_32BE").toString();
			} else if (doByteMatch(bytes, 0x00, 0x00, 0x00, 0x3C)) {
				encoding = Charset.forName("UTF_32BE").toString();
				validateByContent = true;
			} else if (doByteMatch(bytes, 0xFF, 0xFE, 0x00, 0x00)) {
				encoding = Charset.forName("UTF_32LE").toString();
			} else if (doByteMatch(bytes, 0x3C, 0x00, 0x00, 0x00)) {
				encoding = Charset.forName("UTF_32LE").toString();
				validateByContent = true;
			} else if (doByteMatch(bytes, 0xFE, 0xFF)) {
				encoding = Charset.forName("UnicodeBigUnmarked").toString();
			} else if (doByteMatch(bytes, 0x00, 0x3C, 0x00, 0x3F)) {
				encoding = Charset.forName("UnicodeBigUnmarked").toString();
				validateByContent = true;
			} else if (doByteMatch(bytes, 0xFF, 0xFE)) {
				encoding = Charset.forName("UnicodeLittleUnmarked").toString();
			} else if (doByteMatch(bytes, 0x3C, 0x00, 0xEF, 0x00)) {
				encoding = Charset.forName("UnicodeLittleUnmarked").toString();
				validateByContent = true;
			} else if (doByteMatch(bytes, 0xEF, 0xBB, 0xBF)) {
				encoding = Charset.forName("UTF-8").toString();
			} else if (doByteMatch(bytes, 0x4C, 0x6F, 0xA7, 94)) {
				encoding = Charset.forName("CP037").toString();
			} else if (doByteMatch(bytes, 0x3C, 0x3F, 0x78, 0x6D)) {
				encoding = Charset.forName("US-ASCII").toString();
				validateByContent = true;
			} else {

				// we don't use the default here, because a US-ASCII decoder
				// really
				// fails on invalid characters which might not be the case for
				// any
				// default encoding
				encoding = Charset.forName("US-ASCII").toString();
				validateByContent = true;
			}
		}

		return new EncodingResult(encoding, validateByContent);
	}

	/**
	 * Helper method to compare the passed <code>input</code> bytes with the
	 * specified matches. The integers are casted to bytes for comparision.
	 * Furthermore the method checks if the <code>input</code> is of the size of
	 * the specified matches (and does not assume 0x00).
	 * 
	 * @param input
	 *            the input to be checked for matching
	 * @param match
	 *            the bytes that are expected in the input, i.e. should be found
	 *            for a match
	 * 
	 * @return <code>true</code> if the <code>input</code> matches the
	 *         <code>match</code>, otherwise <code>false</code>
	 */
	protected static boolean doByteMatch(final byte[] input, final int... match) {

		// make sure we have enough input for the match
		if (input.length < match.length) {
			return false;
		}

		for (int i = 0; i < match.length; i++) {
			if (input[i] != (byte) match[i]) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Transforms a short to a byte array (little endian).
	 * 
	 * @param val
	 *            the short to be transformed
	 * 
	 * @return the byte array
	 */
	public static byte[] shortToByte(short val) {
		final byte[] b = new byte[SIZEOF_SHORT];

		for (int i = SIZEOF_SHORT - 1; i > 0; i--) {
			b[i] = (byte) val;
			val >>= 8;
		}
		b[0] = (byte) val;

		return b;
	}

	/**
	 * Transforms the {@code bytes} to the short. The bytes should have been
	 * generated using {@link #shortToByte(short)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed to a short
	 * 
	 * @return the short represented by the bytes
	 */
	public static short byteToShort(final byte[] bytes) {
		short n = 0;
		n ^= bytes[0] & 0xFF;
		n <<= 8;
		n ^= bytes[1] & 0xFF;
		return n;
	}

	/**
	 * Transforms an integer to a byte array.
	 * 
	 * @param val
	 *            the integer to be transformed
	 * 
	 * @return the byte array
	 */
	public static byte[] intToByte(int val) {
		final byte[] b = new byte[SIZEOF_INT];

		for (int i = SIZEOF_INT - 1; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;

		return b;

	}

	/**
	 * Transforms the {@code bytes} to the int. The bytes should have been
	 * generated using {@link #intToByte(int)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed to an integer
	 * 
	 * @return the int represented by the bytes
	 */
	public static int byteToInt(final byte[] bytes) {
		int n = 0;
		for (int i = 0; i < SIZEOF_INT; i++) {
			n <<= 8;
			n ^= bytes[i] & 0xFF;
		}

		return n;
	}

	/**
	 * Transforms a long to a byte array.
	 * 
	 * @param val
	 *            the long to be transformed
	 * 
	 * @return the byte array
	 */
	public static byte[] longToByte(long val) {
		final byte[] b = new byte[SIZEOF_LONG];

		for (int i = SIZEOF_LONG - 1; i > 0; i--) {
			b[i] = (byte) val;
			val >>>= 8;
		}
		b[0] = (byte) val;

		return b;
	}

	/**
	 * Transforms the {@code bytes} to the long. The bytes should have been
	 * generated using {@link #longToByte(long)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed to a long
	 * 
	 * @return the long represented by the bytes
	 */
	public static long byteToLong(final byte[] bytes) {
		long l = 0;

		for (int i = 0; i < SIZEOF_LONG; i++) {
			l <<= 8;
			l ^= bytes[i] & 0xFF;
		}

		return l;
	}

	/**
	 * Transforms a string to a byte array using a UTF-8 charset.
	 * 
	 * @param s
	 *            the string to be transformed
	 * 
	 * @return the byte array
	 */
	public static byte[] stringToByte(final String s) {
		return stringToByte(s, "UTF-8");
	}

	/**
	 * Transforms a string to a byte array using the specified {@code charset}.
	 * 
	 * @param s
	 *            the string to be transformed
	 * @param charset
	 *            the {@code Charset} to be used
	 * 
	 * @return the byte array
	 * 
	 * @see Charset
	 */
	public static byte[] stringToByte(final String s, final String charset) {
		return stringToByte(s, Charset.forName(charset));
	}

	/**
	 * Transforms a string to a byte array using the specified {@code charset}.
	 * 
	 * @param s
	 *            the string to be transformed
	 * @param charset
	 *            the {@code Charset} to be used
	 * 
	 * @return the byte array
	 * 
	 * @see Charset
	 */
	public static byte[] stringToByte(final String s, final Charset charset) {
		return s.getBytes(charset);
	}

	/**
	 * Transforms the {@code bytes} to a string. The bytes should have been
	 * generated using {@link #stringToByte(String)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed
	 * 
	 * @return the string represented by the bytes
	 */
	public static String byteToString(final byte[] bytes) {
		return byteToString(bytes, "UTF-8");
	}

	/**
	 * Transforms the {@code bytes} to a string. The bytes should have been
	 * generated using {@link #stringToByte(String)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed
	 * @param charset
	 *            the charset of the bytes
	 * 
	 * @return the string represented by the bytes
	 */
	public static String byteToString(final byte[] bytes, final String charset) {
		return byteToString(bytes, Charset.forName(charset));
	}

	/**
	 * Transforms the {@code bytes} to a string. The bytes should have been
	 * generated using {@link #stringToByte(String)}.
	 * 
	 * @param bytes
	 *            the bytes to be transformed
	 * @param charset
	 *            the charset of the bytes
	 * 
	 * @return the string represented by the bytes
	 */
	public static String byteToString(final byte[] bytes, final Charset charset) {
		return new String(bytes, charset);
	}

	/**
	 * Transforms a serializable object to a byte array.
	 * 
	 * @param o
	 *            the object to be transformed
	 * 
	 * @return the byte array or {@code null} if the object could not be
	 *         serialized
	 */
	public static byte[] objectToByte(final Object o) {
		final Class<?> clazz = o == null ? null : o.getClass();
		final byte nr = getByteTypeNr(clazz);

		// get the identifier as array
		final byte[] bytesType = new byte[] { nr };

		// get the value as array
		final byte[] bytesRepresentation;
		if (clazz == null) {
			bytesRepresentation = new byte[] {};
		} else if (Byte.class.equals(clazz)) {
			bytesRepresentation = new byte[] { (Byte) o };
		} else if (Short.class.equals(clazz)) {
			bytesRepresentation = Streams.shortToByte((Short) o);
		} else if (Integer.class.equals(clazz)) {
			bytesRepresentation = Streams.intToByte((Integer) o);
		} else if (Long.class.equals(clazz)) {
			bytesRepresentation = Streams.longToByte((Long) o);
		} else if (String.class.equals(clazz)) {
			final byte[] tmpBytes = Streams.stringToByte((String) o);
			final byte[] tmpBytesLength = Streams.intToByte(tmpBytes.length);

			bytesRepresentation = Streams
					.combineBytes(tmpBytesLength, tmpBytes);
		} else {
			final byte[] tmpBytes = serializeObject(o);
			final byte[] tmpBytesLength = Streams.intToByte(tmpBytes.length);

			bytesRepresentation = Streams
					.combineBytes(tmpBytesLength, tmpBytes);
		}

		return Streams.combineBytes(bytesType, bytesRepresentation);
	}

	/**
	 * Deserializes an object from the specified byte array. If the array
	 * represents several objects, all objects can be read using:
	 * 
	 * <pre>
	 * final ArrayList&lt;Object&gt; objects = new ArrayList&lt;Object&gt;();
	 * int offset = 0;
	 * while (offset &lt; byteArray.length) {
	 * 	value = Streams.byteToObject(byteArray, offset);
	 * 	offset = value.nextPos;
	 * 
	 * 	objects.add(value.object);
	 * }
	 * </pre>
	 * 
	 * @param bytes
	 *            the byte array to read the object from
	 * 
	 * @return the read object or {@code null} if an error occurred
	 * 
	 * @see #readAllObjects(byte[])
	 */
	public static ByteResult byteToObject(final byte[] bytes) {
		return byteToObject(bytes, 0);
	}

	/**
	 * Deserializes an object from the specified byte array. If the array
	 * represents several objects, all objects can be read using:
	 * 
	 * <pre>
	 * final ArrayList&lt;Object&gt; objects = new ArrayList&lt;Object&gt;();
	 * int offset = 0;
	 * while (offset &lt; byteArray.length) {
	 * 	value = Streams.byteToObject(byteArray, offset);
	 * 	offset = value.nextPos;
	 * 
	 * 	objects.add(value.object);
	 * }
	 * </pre>
	 * 
	 * @param bytes
	 *            the byte array to read the object from
	 * @param offset
	 *            the first position to read the object from
	 * 
	 * @return the read object or {@code null} if an error occurred
	 * 
	 * @see #readAllObjects(byte[])
	 */
	public static ByteResult byteToObject(final byte[] bytes, final int offset) {
		int off = offset;

		final byte pos = bytes[off];
		if (pos < 0) {
			return new ByteResult(null, offset + SIZEOF_BYTE);
		} else {
			off += SIZEOF_BYTE;
		}

		// get the first byte to get the representation type
		final Class<?> clazz = BYTE_TYPES[pos];

		// read the object
		return byteToObject(clazz, bytes, off);
	}

	/**
	 * Deserializes an object from the specified byte array.
	 * 
	 * @param clazz
	 *            the {@code Class} of the object to read
	 * @param bytes
	 *            the byte array to read the object from
	 * @param offset
	 *            the first position to read the object from
	 * 
	 * @return the read object or {@code null} if an error occurred
	 * 
	 * @see #readAllObjects(byte[])
	 */
	public static ByteResult byteToObject(final Class<?> clazz,
			final byte[] bytes, final int offset) {
		return byteToObject(clazz, -1, bytes, offset);
	}

	/**
	 * Deserializes an object from the specified byte array.
	 * 
	 * @param clazz
	 *            the {@code Class} of the object to read
	 * @param dynLength
	 *            the dynamic length of the object to be retrieved, if not known
	 *            pass {@code -1}
	 * @param bytes
	 *            the byte array to read the object from
	 * @param offset
	 *            the first position to read the object from
	 * 
	 * @return the read object or {@code null} if an error occurred
	 * 
	 * @see #readAllObjects(byte[])
	 */
	public static ByteResult byteToObject(final Class<?> clazz,
			final int dynLength, final byte[] bytes, final int offset) {
		int off = offset;

		final Object res;
		if (Byte.class.equals(clazz)) {
			res = bytes[off];
			off += SIZEOF_BYTE;
		} else if (Short.class.equals(clazz)) {
			res = Streams.byteToShort(Arrays.copyOfRange(bytes, off, off = off
					+ SIZEOF_SHORT));
		} else if (Integer.class.equals(clazz)) {
			res = Streams.byteToInt(Arrays.copyOfRange(bytes, off, off = off
					+ SIZEOF_INT));
		} else if (Long.class.equals(clazz)) {
			res = Streams.byteToLong(Arrays.copyOfRange(bytes, off, off = off
					+ SIZEOF_LONG));
		}
		// we have a dynamically sizing object
		else {

			// get the length
			final int length;
			if (dynLength < 0) {
				length = Streams.byteToInt(Arrays.copyOfRange(bytes, off,
						off = off + SIZEOF_INT));
			} else {
				length = dynLength;
			}

			// check if we have a string
			if (String.class.equals(clazz)) {
				res = Streams.byteToString(Arrays.copyOfRange(bytes, off,
						off = off + length));
			} else {
				res = deserializeObject(Arrays.copyOfRange(bytes, off,
						off = off + length));
			}
		}
		return new ByteResult(res, off);
	}

	/**
	 * Determines the overhead (in bytes) to the typical byte-representation
	 * needed when represented as general object.
	 * 
	 * @param o
	 *            the object to determine the overhead for
	 * 
	 * @return the overhead needed to represent the object
	 */
	public static int objectOverhead(final Object o) {
		if (o == null) {
			return objectOverhead((Class<?>) null);
		} else {
			return objectOverhead(o.getClass());
		}
	}

	/**
	 * Gets the overhead needed to represent the type of an object (this is done
	 * using the first byte to represent a position in the class-array
	 * {@link #BYTE_TYPES}).
	 * 
	 * @return the overhead needed to represent the type of an object
	 */
	public static int classOverhead() {
		return SIZEOF_BYTE;
	}

	/**
	 * Determines the overhead (in bytes) to the typical byte-representation
	 * needed when represented as general object. The overhead-size includes the
	 * size to represent the {@code clazz} as well (see {@link #classOverhead()}
	 * ).
	 * 
	 * @param clazz
	 *            the clazz to determine the overhead for
	 * 
	 * @return the overhead needed to represent the object
	 */
	public static int objectOverhead(final Class<?> clazz) {
		if (clazz == null) {
			return classOverhead();
		} else if (Short.class.equals(clazz)) {
			return classOverhead();
		} else if (Integer.class.equals(clazz)) {
			return classOverhead();
		} else if (Long.class.equals(clazz)) {
			return classOverhead();
		} else if (String.class.equals(clazz)) {
			return classOverhead() + SIZEOF_INT;
		} else {
			return classOverhead() + SIZEOF_INT;
		}
	}

	/**
	 * Returns the size in bytes of the byte-representation of an object. If the
	 * value is negative, the size is dynamic and cannot be determined without
	 * creating the presentation. The absolute value of a negative value is the
	 * minimum size.
	 * 
	 * @param o
	 *            the object to determine the size for
	 * 
	 * @return the size of an object presented as byte-array
	 */
	public static int objectSize(final Object o) {
		if (o == null) {
			return objectSize((Class<?>) null);
		} else {
			return objectSize(o.getClass());
		}
	}

	/**
	 * Returns the size in bytes of the byte-representation of an object of the
	 * specified {@code clazz}. If the value is negative, the size is dynamic
	 * and cannot be determined without creating the presentation. The absolute
	 * value of a negative value is the minimum size.
	 * 
	 * @param clazz
	 *            the type to determine the size for
	 * 
	 * @return the size of an object presented as byte-array
	 */
	public static int objectSize(final Class<?> clazz) {
		final int overhead = objectOverhead(clazz);

		if (clazz == null) {
			return overhead + 0;
		} else if (Short.class.equals(clazz)) {
			return overhead + SIZEOF_SHORT;
		} else if (Integer.class.equals(clazz)) {
			return overhead + SIZEOF_INT;
		} else if (Long.class.equals(clazz)) {
			return overhead + SIZEOF_LONG;
		} else if (String.class.equals(clazz)) {
			return -1 * overhead;
		} else {
			return -1 * overhead;
		}
	}

	/**
	 * Reads all the objects represented by the specified {@code byteArray}.
	 * 
	 * @param byteArray
	 *            the array to read the objects from
	 * 
	 * @return the list of read objects
	 */
	public static ArrayList<Object> readAllObjects(final byte[] byteArray) {
		return readAllObjects(byteArray, 0);
	}

	/**
	 * Reads all the objects represented by the specified {@code byteArray}.
	 * 
	 * @param byteArray
	 *            the array to read the objects from
	 * @param offset
	 *            the zero-based position to start reading at
	 * 
	 * @return the list of read objects
	 */
	public static ArrayList<Object> readAllObjects(final byte[] byteArray,
			final int offset) {

		// the list of objects read
		final ArrayList<Object> objects = new ArrayList<Object>();

		// read all the instances
		int curPos = offset;
		while (curPos < byteArray.length) {
			final ByteResult value = Streams.byteToObject(byteArray, curPos);

			curPos = value.nextPos;
			objects.add(value.object);
		}

		return objects;
	}

	/**
	 * Writes all the object so that those are represented within the result.
	 * 
	 * @param objects
	 *            the objects to be written
	 * 
	 * @return the result
	 */
	public static byte[] writeAllObjects(final Collection<Object> objects) {
		return writeAllObjects(objects == null ? null : objects.toArray());
	}

	/**
	 * Writes all the object so that those are represented within the result.
	 * 
	 * @param objects
	 *            the objects to be written
	 * 
	 * @return the result
	 */
	public static byte[] writeAllObjects(final Object... objects) {
		byte[] result = new byte[] {};
		if (objects != null) {
			byte[][] intermediateResults = new byte[objects.length][];
			for (int i = 0; i < objects.length; i++) {
				final Object object = objects[i];
				intermediateResults[i] = objectToByte(object);
			}

			result = Streams.combineBytes(intermediateResults);
		}

		return result;
	}

	/**
	 * Serializes the specified object using a {@code ObjectOutputStream}.
	 * 
	 * @param o
	 *            the object to be serialized
	 * 
	 * @return the serialized version of the object
	 * 
	 * @see ObjectOutputStream
	 */
	public static byte[] serializeObject(final Object o) {
		final ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(o);

			return bos.toByteArray();
		} catch (final IOException e) {
			return null;
		} finally {
			Streams.closeIO(out);
			Streams.closeIO(bos);
		}
	}

	/**
	 * Deserializes the specified byte-array using a {@code ObjectInputStream}.
	 * 
	 * @param rep
	 *            the byte array to be deserialized
	 * 
	 * @return the deserialized version of the object
	 * 
	 * @see ObjectInputStream
	 */
	public static Object deserializeObject(final byte[] rep) {
		final ByteArrayInputStream bis = new ByteArrayInputStream(rep);
		ObjectInput in = null;
		try {
			in = new ObjectInputStream(bis);
			return in.readObject();
		} catch (final Exception e) {
			return null;
		} finally {
			Streams.closeIO(bis);
			Streams.closeIO(in);
		}
	}

	/**
	 * Combines the specified {@code bytes}. The method returns {@code null} if
	 * and only if {@code null} is passed. If one of the byte-arrays is
	 * {@code null} it is handled as empty array, i.e {@code new byte[0]]}.
	 * 
	 * @param bytes
	 *            the arrays to be combined
	 * 
	 * @return the combined array
	 */
	public static byte[] combineBytes(final byte[]... bytes) {
		if (bytes == null) {
			return null;
		}

		// get the amount of bytes to be joined
		final int length = bytes.length;

		if (length == 0) {
			return new byte[0];
		} else {
			int newSize = 0;
			for (int i = 0; i < length; i++) {
				if (bytes[i] == null) {
					continue;
				}

				newSize += bytes[i].length;
			}

			// create the new array
			final byte[] result = new byte[newSize];

			// add the values
			int nextPos = 0;
			for (int i = 0; i < length; i++) {
				if (bytes[i] == null) {
					continue;
				}

				System.arraycopy(bytes[i], 0, result, nextPos, bytes[i].length);
				nextPos += bytes[i].length;
			}

			return result;
		}
	}

	/**
	 * Reads the next object from the buffer. It should be ensured that there is
	 * at least the possibility of a next object by calling
	 * {@link ByteBuffer#hasRemaining()} prior to calling this method.
	 * 
	 * @param buffer
	 *            the buffer to read from
	 * 
	 * @return the object read
	 * 
	 * @throws IllegalArgumentException
	 *             if there is no object in the buffer
	 */
	public static Object readNextObject(final ByteBuffer buffer)
			throws IllegalArgumentException {

		try {
			// determine the type of the object we are looking for
			final Class<?> clazz;
			if (buffer.hasRemaining()) {
				final byte pos = buffer.get();
				if (pos < 0) {
					return null;
				} else {
					clazz = BYTE_TYPES[pos];
				}
			} else {
				throw new IllegalArgumentException(
						"The buffer does not contain any object.");
			}

			/*
			 * determine the size of the object, so that we can read the bytes
			 * needed
			 */
			final int objSize = objectSize(clazz);

			// get the amount of bytes to be read for the object
			final int readSize;
			final int dynSize;

			// we have a dynamic object-size
			if (objSize < 0) {

				// the size is encoded in the first bytes as integer
				final byte[] sizeBytes = new byte[SIZEOF_INT];
				buffer.get(sizeBytes);
				readSize = Streams.byteToInt(sizeBytes);
				dynSize = readSize;
			}
			// we have a static object-size
			else {

				// get the size of the object
				readSize = objectSize(clazz) - classOverhead();
				dynSize = -1;
			}

			// get the bytes from the buffer
			final byte[] bytes = new byte[readSize];
			buffer.get(bytes);

			// get the result and return the object
			final ByteResult res = byteToObject(clazz, dynSize, bytes, 0);
			return res.object;
		} catch (final BufferUnderflowException e) {
			throw new IllegalArgumentException(
					"The buffer does not contain any valid object", e);
		}
	}
}
