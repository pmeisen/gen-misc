package net.meisen.general.genmisc.unicode;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Generic unicode textreader, which will use BOM mark to identify the encoding
 * to be used. If BOM is not found then use a given default encoding. System
 * default is used if: BOM mark is not found and defaultEnc is NULL<br />
 * <br />
 * Usage pattern:<br />
 * String defaultEnc = "ISO-8859-1"; // or NULL to use system<br />
 * default FileInputStream fis = new FileInputStream(file); Reader in = new
 * UnicodeReader(fis, defaultEnc);
 * 
 * http://www.unicode.org/unicode/faq/utf_bom.html<br />
 * BOMs:<br />
 * 00 00 FE FF = UTF-32, big-endian<br />
 * FF FE 00 00 = UTF-32, little-endian<br />
 * FE FF = UTF-16, big-endian<br />
 * FF FE = UTF-16, little-endian<br />
 * EF BB BF = UTF-8<br />
 */
public class UnicodeReader extends Reader {
	private static final int BOM_SIZE = 4;

	private PushbackInputStream internalPushBackIS;
	private BufferedReader bufferReader = null;
	private String defaultEnc = null;

	private String encoding;

	/**
	 * @param in
	 *            the input stream to the file, using the default encoding of
	 *            the system
	 */
	public UnicodeReader(final InputStream in) {
		this(in, null);
	}

	/**
	 * @param in
	 *            the input stream to the file
	 * @param defaultEnc
	 *            the default encoding to be used, if it cannot be read from the
	 *            file; if set to <code>null</code> the system default is used
	 */
	public UnicodeReader(final InputStream in, final String defaultEnc) {
		internalPushBackIS = new PushbackInputStream(in, BOM_SIZE);
		this.defaultEnc = defaultEnc;
	}

	/**
	 * @return the default encoding to be used, if <code>null</code> the system
	 *         encoding will be used
	 */
	public String getDefaultEncoding() {
		return defaultEnc;
	}

	/**
	 * @return the encoding of the file
	 */
	public String getEncoding() {
		return this.encoding;
	}

	/**
	 * Read-ahead four bytes and check for BOM marks. Extra bytes are unread
	 * back to the stream, only BOM bytes are skipped.
	 * 
	 * @throws IOException
	 *             if the source code not be read
	 */
	protected void init() throws IOException {
		if (bufferReader != null)
			return;

		final String encoding;
		final byte bom[] = new byte[BOM_SIZE];
		final int n = internalPushBackIS.read(bom, 0, bom.length);
		final int unread;

		if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB)
				&& (bom[2] == (byte) 0xBF)) {
			encoding = "UTF-8";
			unread = n - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = "UTF-16BE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = "UTF-16LE";
			unread = n - 2;
		} else if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00)
				&& (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encoding = "UTF-32BE";
			unread = n - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)
				&& (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encoding = "UTF-32LE";
			unread = n - 4;
		} else {
			// Unicode BOM mark not found, unread all bytes
			encoding = defaultEnc;
			unread = n;
		}

		if (unread > 0)
			internalPushBackIS.unread(bom, (n - unread), unread);

		// Use given encoding
		if (encoding == null) {
			bufferReader = new BufferedReader(new InputStreamReader(
					internalPushBackIS));
		} else {
			bufferReader = new BufferedReader(new InputStreamReader(
					internalPushBackIS, encoding));
		}
		this.encoding = encoding;
	}

	/**
	 * closes the file, if not closed yet
	 */
	public void close() throws IOException {
		if (bufferReader != null)
			bufferReader.close();
	}

	/**
	 * This function reads a line off the file. If the end is reached or an
	 * error occurs the file is closed.
	 * 
	 * @return A line of text of the file. A line is specified by any one of a
	 *         line feed ('\n'), a carriage return ('\r'), or a carriage return
	 *         followed immediately by a linefeed.
	 * 
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public String readLine() throws IOException {
		init();

		if (bufferReader == null)
			return null;
		else if (!bufferReader.ready()) {
			bufferReader.close();
			return null;
		} else {
			String line = null;

			// try to read another line
			int i = 0;
			while (i == 0 || line.endsWith("\\")) {

				// remove the slash
				if (i > 0) {
					line = line.substring(0, line.length() - 1);
				}

				// read the next line
				final String returnValue = bufferReader.readLine();

				// close the file if the end is reached
				if (returnValue == null) {
					bufferReader.close();

					// do not modify the line (can be null)
					// make sure we exit here
					break;
				} else {
					// count further
					i++;

					// append the new value
					if (line != null) {
						line += returnValue;
					} else {
						line = returnValue;
					}
				}
			}

			return line;
		}
	}

	@Override
	public int read(char[] cbuf, int off, int len) throws IOException {
		init();

		if (bufferReader == null)
			return -1;
		else
			return bufferReader.read(cbuf, off, len);
	}

	/**
	 * @param in
	 *            the input stream of the property file
	 * @return the properties read from the file as {@link Map}
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static Map<String, String> readPropertyFile(final InputStream in)
			throws IOException {
		return readPropertyFile(in, null);
	}

	/**
	 * @param in
	 *            the input stream of the property file
	 * @param defaultProperties
	 *            which will be used if not set within the input stream,
	 *            otherwise those will be overwritten
	 * @return the properties read from the file as {@link Map}
	 * @throws IOException
	 *             If an I/O error occurs
	 */
	public static Map<String, String> readPropertyFile(final InputStream in,
			final Map<String, String> defaultProperties) throws IOException {
		final UnicodeReader u = new UnicodeReader(in);

		final Map<String, String> properties = new HashMap<String, String>();

		if (defaultProperties != null)
			properties.putAll(defaultProperties);

		String line = "";
		while ((line = u.readLine()) != null) {

			if (!line.startsWith("#") && line.indexOf('=') != -1) {
				final String key = line.substring(0, line.indexOf('=')).trim();
				final String value = line.substring(line.indexOf('=') + 1)
						.trim();

				properties.put(key, value);
			}
		}

		return properties;
	}

	/**
	 * Reads the whole context of the passed {@link InputStream} into a
	 * {@link String}
	 * 
	 * @param in
	 *            the {@link InputStream} to read from
	 * @return the content of the {@link InputStream}
	 * @throws IOException
	 *             if the passed {@link InputStream} could not be read
	 */
	public static String readFile(final InputStream in) throws IOException {
		final UnicodeReader u = new UnicodeReader(in);

		String file = "";
		String line = "";
		while ((line = u.readLine()) != null) {
			file += line;
		}

		return new String(file.getBytes(), u.getEncoding());
	}
}
