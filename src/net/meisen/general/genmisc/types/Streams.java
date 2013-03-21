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
}
