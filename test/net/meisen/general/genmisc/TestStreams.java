package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

import net.meisen.general.genmisc.types.Streams;

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
	 *           if the file cannot be accessed
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
		is = getClass().getResourceAsStream("encodedFiles/UCS-2_LittleEndian.txt");
		enc = Streams.guessEncoding(is, null);
		assertEquals("UTF-16LE", enc);
	}
}
