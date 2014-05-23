package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.UUID;

import net.meisen.general.genmisc.resources.FileByteBufferReader;
import net.meisen.general.genmisc.types.Streams;

import org.junit.After;
import org.junit.Test;

public class TestFileByteBufferReader {

	private FileByteBufferReader reader = null;
	private File file = null;

	protected File createFile(final long sizeInBytes, final boolean upcount) {
		final File file = new File(System.getProperty("java.io.tmpdir"), UUID
				.randomUUID().toString());

		if (upcount) {
			FileOutputStream fOutput = null;
			try {
				fOutput = new FileOutputStream(file);
				final OutputStream output = new BufferedOutputStream(fOutput);

				for (long p = 0; p < sizeInBytes; p++) {
					final byte b = (byte) (p % Byte.MAX_VALUE);
					output.write(b);
				}

				output.flush();
				output.close();
			} catch (final IOException e) {
				fail(e.getMessage());
			} finally {
				Streams.closeIO(fOutput);
			}
		} else {
			RandomAccessFile f = null;
			try {
				f = new RandomAccessFile(file, "rws");
				f.setLength(sizeInBytes);
				f.close();
			} catch (final IOException e) {
				fail(e.getMessage());
			} finally {
				Streams.closeIO(f);
			}
		}
		assertTrue(file.exists());

		return file;
	}

	protected void setReader(final long fileSizeInBytes,
			final int arraySizeInBytes, final boolean upcount) {
		file = createFile(fileSizeInBytes, upcount);
		try {
			reader = new FileByteBufferReader(file, arraySizeInBytes);
		} catch (final FileNotFoundException e) {
			fail(e.getMessage());
		}
	}

	/**
	 * Tests a setting were no buffer is needed.
	 */
	@Test
	public void testNoBufferNeed() {
		setReader(1000, 1024, false);

		// check the values
		assertEquals(1000, reader.getArraySize());
		assertEquals(0, reader.getBufferSize());
		assertFalse(reader.usesBuffer());
	}

	/**
	 * Tests a setting were the buffer is needed and limited by the file-size.
	 */
	@Test
	public void testBufferLimited() {
		setReader(2000, 1000, false);

		// check the values
		assertEquals(1000, reader.getArraySize());
		assertEquals(2000, reader.getBufferSize());
		assertTrue(reader.usesBuffer());
	}

	/**
	 * Tests a setting were a buffer is needed and the file-size exceeds the
	 * buffer max-size.
	 */
	@Test
	public void testBufferExceeded() {
		setReader(12000, 1000, false);

		// check the values
		assertEquals(1000, reader.getArraySize());
		assertEquals(10000, reader.getBufferSize());
		assertTrue(reader.usesBuffer());
	}

	@Test
	public void testReadingWithoutBuffer() {
		setReader(100, 1000, true);

		// check the values
		assertFalse(reader.usesBuffer());
		assertTrue(reader.hasRemaining());
		assertEquals(100, reader.getLimit());

		int i = 0;
		while (reader.hasRemaining()) {
			assertEquals(i, reader.get());
			i++;
		}

		// last read value was 99 but i++ was triggered
		assertEquals(100, i);
	}

	@Test
	public void testReadingWithFullArray() {
		setReader(100, 100, true);

		// check the values
		assertFalse(reader.usesBuffer());
		assertTrue(reader.hasRemaining());
		assertEquals(100, reader.getLimit());

		int i = 0;
		while (reader.hasRemaining()) {
			assertEquals(i, reader.get());
			i++;
		}

		// last read value was 99 but i++ was triggered
		assertEquals(100, i);
	}
	
	@Test
	public void testReadingWithFullBuffer() {
		setReader(10000, 1000, true);

		// check the values
		assertTrue(reader.usesBuffer());
		assertTrue(reader.hasRemaining());
		assertEquals(10000, reader.getLimit());
		assertEquals(1000, reader.getArraySize());
		assertEquals(10000, reader.getBufferSize());

		int i = 0;
		while (reader.hasRemaining()) {
			assertEquals(i % Byte.MAX_VALUE, reader.get());
			i++;
		}

	}
	
	@Test
	public void testReadingWithExceededBuffer() {
		setReader(300, 1, true);

		// check the values
		assertTrue(reader.usesBuffer());
		assertTrue(reader.hasRemaining());
		assertEquals(300, reader.getLimit());
		assertEquals(1, reader.getArraySize());
		assertEquals(10, reader.getBufferSize());

		int i = 0;
		while (reader.hasRemaining()) {
			assertEquals(i % Byte.MAX_VALUE, reader.get());
			i++;
		}
	}
	
	@Test
	public void testReadingEmptyFile() {
		setReader(0, 1, true);
		
		assertFalse(reader.usesBuffer());
		assertFalse(reader.hasRemaining());
		assertEquals(0, reader.getLimit());
	}

	/**
	 * Close the reader and remove the file.
	 */
	@After
	public void cleanUp() {

		// cleanUp
		Streams.closeIO(reader);

		// important the file must be deleteable
		assertTrue("Unable to delete " + file, file.delete());
	}
}
