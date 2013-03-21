package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;

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
}
