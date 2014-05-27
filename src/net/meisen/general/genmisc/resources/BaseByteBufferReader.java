package net.meisen.general.genmisc.resources;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import net.meisen.general.genmisc.types.Streams;

/**
 * Base implementation of a byte-reader which buffers the bytes to be read in an
 * array and a larger {@code ByteBuffer}. The data is transfered from the
 * resource into the buffer and than into the array.
 * 
 * @author pmeisen
 * 
 */
public abstract class BaseByteBufferReader implements IByteBufferReader {
	private int bufferSizeInByte;

	private ByteBuffer buffer;
	private byte[] array;

	private int curArrayPos;
	private int curArrayLength;

	/**
	 * Default constructor
	 */
	public BaseByteBufferReader() {
		this.curArrayPos = 0;
		this.curArrayLength = 0;
	}

	/**
	 * Initializes the base implementation and should be called as last command
	 * within the concrete implementation's constructor.
	 * 
	 * @param arraySizeInByte
	 *            the size of the array to be used
	 * @param limit
	 *            the maximal amount of data to be handled, can be {@code -1} if
	 *            not known
	 */
	protected void init(final int arraySizeInByte, final long limit) {

		// the buffer size can be a maximum of the file-size
		final long usedLimit = limit == -1 ? Long.MAX_VALUE : limit;
		final int initArraySizeInByte = (int) Math.min(usedLimit,
				arraySizeInByte);

		// determine the sizes
		this.array = new byte[initArraySizeInByte];
		if (initArraySizeInByte == usedLimit) {
			this.buffer = null;
			this.bufferSizeInByte = 0;
		} else {
			this.bufferSizeInByte = (int) Math.min(usedLimit,
					Math.min(Integer.MAX_VALUE, 10l * arraySizeInByte));

			this.buffer = ByteBuffer.allocateDirect(this.bufferSizeInByte);
			this.buffer.position(this.bufferSizeInByte);
		}

		// fill the array
		fillArray();
	}

	@Override
	public byte get() {
		if (!hasRemaining()) {
			throw new BufferUnderflowException();
		}

		final byte value = array[curArrayPos];
		curArrayPos++;

		// next read would fail, therefore reload the array
		if (curArrayPos >= curArrayLength) {
			fillArray();
			curArrayPos = 0;
		}
		return value;
	}

	@Override
	public void get(final byte[] dst) {
		get(dst, 0, dst.length);
	}

	@Override
	public void get(final byte[] dst, final int offset, final int length) {
		int end = offset + length;
		for (int i = offset; i < end; i++) {
			dst[i] = get();
		}
	}

	/**
	 * Fills the array with data from the buffer, or the file if no buffer is
	 * needed.
	 */
	protected void fillArray() {
		final int curArraySize = array.length;

		if (buffer == null) {
			try {
				curArrayLength = read(ByteBuffer.wrap(array));
			} catch (final IOException e) {
				throw new IllegalStateException("Unable to read channel.", e);
			}
		} else {
			final int bufferedBytes = buffer.remaining();

			// there are enough bytes in the buffer
			if (bufferedBytes >= curArraySize) {
				buffer.get(array, 0, curArraySize);
			}
			// the buffer does not have enough bytes, but has some
			else {
				if (bufferedBytes > 0) {
					buffer.get(array, 0, bufferedBytes);
				}

				// refill the buffer
				fillBuffer();

				// get the rest
				if (buffer.remaining() > 0) {
					final int neededBytesToFill = curArraySize - bufferedBytes;
					curArrayLength = Math.min(buffer.remaining(),
							neededBytesToFill);
					buffer.get(array, bufferedBytes, curArrayLength);
				} else {
					curArrayLength = 0;
				}
			}
		}
	}

	/**
	 * Reads a sequence of bytes into the given {@code buffer}. Bytes are read
	 * starting at the current position, and then the position is updated with
	 * the number of bytes actually read. The buffer is cleared and can be
	 * directly filled.
	 * 
	 * @param buffer
	 *            the buffer into which bytes are to be transferred
	 * @return the number of bytes read, possibly zero, or -1 if the
	 *         end-of-stream is reached
	 * @throws IOException
	 *             if some other I/O error occurs
	 */
	protected abstract int read(final ByteBuffer buffer) throws IOException;

	/**
	 * Fills the buffer with data from the file.
	 */
	protected void fillBuffer() {
		buffer.clear();
		final int readFileBytes;
		try {
			readFileBytes = read(buffer);
		} catch (final IOException e) {
			throw new IllegalStateException("Unable to read channel.", e);
		}

		if (readFileBytes == -1) {
			buffer.position(0);
			buffer.limit(0);
		} else {
			buffer.position(0);
			buffer.limit(readFileBytes);
		}
	}

	public void close() {
		Streams.closeIO(buffer);
	}

	/**
	 * Gets the size of the buffer used.
	 * 
	 * @return the size of the buffer used
	 */
	public int getBufferSize() {
		return this.bufferSizeInByte;
	}

	/**
	 * Checks if a buffer is used.
	 * 
	 * @return {@code true} if a buffer is used, {@code false} otherwise
	 */
	public boolean usesBuffer() {
		return this.buffer != null;
	}

	/**
	 * Gets the size of the array used.
	 * 
	 * @return the size of the array used
	 */
	public int getArraySize() {
		return this.array.length;
	}

	@Override
	public boolean hasRemaining() {
		return curArrayLength > 0;
	}
}
