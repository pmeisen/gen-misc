package net.meisen.general.genmisc.resources;

import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

import net.meisen.general.genmisc.types.Streams;

/**
 * Wrapper to use a {@code DataInput} as IByteBufferReader.
 * 
 * @author pmeisen
 * 
 */
public class WrappedDataInputReader extends BaseByteBufferReader {

	private DataInput input;

	/**
	 * Constructor which defines the {@code DataInput} to be wrapped.
	 * 
	 * @param input
	 *            the {@code DataInput} to be wrapped
	 */
	public WrappedDataInputReader(final DataInput input) {
		this(input, 1024);
	}

	/**
	 * Wrapper for a {@code DataInput}.
	 * 
	 * @param input
	 *            the {@code DataInput} to be wrapped
	 * @param arraySize
	 *            the size of the array to be used
	 */
	public WrappedDataInputReader(final DataInput input, final int arraySize) {
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
		final int readBytes;
		if (input instanceof DataInputStream) {
			final DataInputStream dis = (DataInputStream) input;
			readBytes = dis.read(array);
		} else if (input instanceof RandomAccessFile) {
			final RandomAccessFile ras = (RandomAccessFile) input;
			readBytes = ras.read(array);
		} else {

			// read byte by byte - no other way
			int currentlyReadBytes = 0;
			for (; currentlyReadBytes < maxSize; currentlyReadBytes++) {
				try {
					buffer.put(input.readByte());
				} catch (final EOFException e) {
					break;
				}
			}

			readBytes = currentlyReadBytes;
		}
		buffer.put(array);

		return readBytes;
	}
}
