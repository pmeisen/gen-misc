package net.meisen.general.genmisc.types;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;
import java.util.Properties;

import net.meisen.general.genmisc.unicode.UnicodeReader;

/**
 * Utility class to work with all kind of streams
 * 
 * @author pmeisen
 * 
 */
public class Streams {

	private static class EncodingResult {
		/**
		 * The encoding determined
		 */
		public String encoding;
		/**
		 * <code>true</code> if the encoding has to be validated against the content
		 */
		public boolean validateByContent;

		public EncodingResult(final String encoding, final boolean validateByContent) {
			this.encoding = encoding;
			this.validateByContent = validateByContent;
		}
	}

	/**
	 * Copies the {@link InputStream} to the {@link OutputStream}
	 * 
	 * @param input
	 *          the source
	 * @param output
	 *          the destination
	 * @throws IOException
	 *           if one of the streams cannot be read or written
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
	 *          the IO-object to be closed
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
	 *          the input stream which should be copied
	 * @param destFile
	 *          the output file
	 * @throws IOException
	 *           if the file cannot be accessed
	 * 
	 * @see Files#copyStreamToFile(InputStream, File)
	 */
	public static void copyStreamToFile(final InputStream sourceStream,
			final File destFile) throws IOException {
		Files.copyStreamToFile(sourceStream, destFile);
	}

	/**
	 * Writes a string to the specified <code>OutputStream</code>.
	 * 
	 * @param input
	 *          the string to be written
	 * @param output
	 *          the <code>OutputStream</code> to write to
	 */
	public static void writeStringToStream(final String input,
			final OutputStream output) {
		final PrintStream printStream = new PrintStream(output);
		printStream.print(input);
		closeIO(printStream);
	}

	/**
	 * Reads a string form a stream
	 * 
	 * @param stream
	 *          the {@link InputStream} to read from, the stream is not closed
	 *          after reading (i.e. call {@link InputStream#close()}
	 * @return the {@link String} read from the {@link InputStream}
	 * 
	 * @throws IOException
	 *           if the {@link InputStream} throws such an exception
	 */
	public static String readFromStream(final InputStream stream)
			throws IOException {

		final InputStreamReader ir = new InputStreamReader(stream);
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
	 *          the <code>InputStream</code> to read the properties from
	 * 
	 * @return the read <code>Properties</code>
	 * 
	 * @throws IOException
	 *           if the data could not be read from the <code>InputStream</code>
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
	 *          the <code>InputStream</code> to be copied
	 * 
	 * @return the content of the <code>InputStream</code> as <code>byte[]</code>
	 * 
	 * @throws IOException
	 *           if the <code>inputStream</code> could not be read or the data not
	 *           be flushed
	 */
	public static byte[] copyStreamToByteArray(final InputStream inputStream)
			throws IOException {
		return copyStreamToByteArray(inputStream, 4096);
	}

	/**
	 * Copies the <code>inputStream</code> into a <code>byte[]</code>.
	 * 
	 * @param inputStream
	 *          the <code>InputStream</code> to be copied
	 * @param bufferSize
	 *          the size of the buffer to be used
	 * 
	 * @return the content of the <code>InputStream</code> as <code>byte[]</code>
	 * 
	 * @throws IOException
	 *           if the <code>inputStream</code> could not be read or the data not
	 *           be flushed
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

		return buffer.toByteArray();
	}

	/**
	 * Guesses the encoding of the specified <code>inStream</code>. If there are
	 * no indices to guess a encoding the default encoding will be returned, which
	 * can be <code>null</code>. If <code>null</code> is passed the system's
	 * default encoding ( <code>file.encoding</code>) will be used.
	 * 
	 * @param inStream
	 *          the stream to guess the encoding of
	 * @param defaultEncoding
	 *          the default encoding to use, can be <code>null</code>
	 * 
	 * @return the guessed encoding
	 * 
	 * @throws IOException
	 *           if the <code>InputStream</code> cannot be accessed
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

				// check if it might be UTF-8 without a BOM, otherwise use the default
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
	 *          the content to validate the <code>encoding</code> against
	 * @param encoding
	 *          the encoding to be validated
	 * 
	 * @return <code>true</code> if all the characters of the
	 *         <code>InputStream</code> could be decoded using the specified
	 *         <code>encoding</code>
	 * 
	 * @see Charset#newDecoder()
	 * 
	 * @throws IOException
	 *           if the <code>InputStream</code> cannot be accessed
	 */
	public static boolean validateEncoding(final byte[] content,
			final String encoding) throws IOException {

		// create a decoder for the specified encoding and check if it can decode
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
	 *          the content to be checked
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
	 * Determines the encoding based on the first four bytes. If not determinable
	 * the default will be returned, or US-ASCII will be returned so that a
	 * decoder will fail. More detailed the method will try to determine the
	 * encoding, if no bytes (i.e. the amount of read bytes is to small) are
	 * available the default will be returned, otherwise US-ASCII will be returned
	 * if the encoding couldn't be determined by the passed bytes.
	 * 
	 * @param b0
	 *          the first byte
	 * @param b1
	 *          the second byte
	 * @param b2
	 *          the third byte
	 * @param b3
	 *          the fourth byte
	 * @param read
	 *          the amount of bytes really read (might be less than 4)
	 * @param defEnc
	 *          the default encoding to be used if not determinable
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

				// we don't use the default here, because a US-ASCII decoder really
				// fails on invalid characters which might not be the case for any
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
	 *          the input to be checked for matching
	 * @param match
	 *          the bytes that are expected in the input, i.e. should be found for
	 *          a match
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
}
