package net.meisen.general.genmisc.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import net.meisen.general.genmisc.types.Streams;

/**
 * Wrapper to use a {@code InputStream} as IByteBufferReader.
 * 
 * @author pmeisen
 * 
 */
public class WrappedInputStreamReader extends BaseByteBufferReader {

	private InputStream input;

	/**
	 * Constructor which defines the {@code InputStream} to be wrapped.
	 * 
	 * @param input
	 *            the {@code InputStream} to be wrapped
	 */
	public WrappedInputStreamReader(final InputStream input) {
		this(input, 1024);
	}

	/**
	 * Wrapper for a {@code InputStream}.
	 * 
	 * @param input
	 *            the {@code InputStream} to be wrapped
	 * @param arraySize
	 *            the size of the array to be used
	 */
	public WrappedInputStreamReader(final InputStream input, final int arraySize) {
		this.input = input;

		init(arraySize, -1l);
	}

	@Override
	public void close() {
		Streams.closeIO(input);
	}

	@Override
	protected int read(final ByteBuffer buffer) throws IOException {

		// get the maximal amount to be read
		final int maxSize = buffer.capacity();

		// fill the rest of the buffer
		final byte[] array = new byte[maxSize];

		// add the data
		final int readBytes = input.read(array);
		buffer.put(array);

		return readBytes;
	}
}
