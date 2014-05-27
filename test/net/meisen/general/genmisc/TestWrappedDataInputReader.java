package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import net.meisen.general.genmisc.resources.WrappedDataInputReader;

import org.junit.Test;

/**
 * Tests the implementation of a {@code WrappedDataInputReader}.
 * 
 * @author pmeisen
 * 
 */
public class TestWrappedDataInputReader {

	/**
	 * Tests the reading of an empty stream.
	 * 
	 * @throws IOException
	 *             if the stream cannot be created
	 */
	@Test
	public void testReadingOfEmpty() throws IOException {

		// create the input
		final DataInputStream input = new DataInputStream(
				new ByteArrayInputStream(new byte[] {}));
		final WrappedDataInputReader buffer = new WrappedDataInputReader(input);

		assertFalse(buffer.hasRemaining());
		buffer.close();
	}

	/**
	 * Tests the reading of some bytes
	 * @throws IOException
	 */
	@Test
	public void testReadingOfSomeBytes() throws IOException {

		// create the input
		final DataInputStream input = new DataInputStream(
				new ByteArrayInputStream(new byte[] { 0, 1, 2, 3, 4, 5 }));
		final WrappedDataInputReader buffer = new WrappedDataInputReader(input);

		byte i = 0;
		while (buffer.hasRemaining()) {
			assertEquals(i, buffer.get());
			i++;
		}

		// check the amount of read values
		assertEquals(6, i);
		buffer.close();
	}
}
