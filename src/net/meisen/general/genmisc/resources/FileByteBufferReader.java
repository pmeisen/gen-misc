package net.meisen.general.genmisc.resources;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import net.meisen.general.genmisc.types.Streams;

/**
 * This class implements the features known from a {@code MappedByteBuffer}
 * retrieved via {@link FileChannel#map(MapMode, long, long)}. Nevertheless a
 * {@code MappedByteBuffer} has the problem that it does not release, the file
 * "correctly" and therefore makes it impossible that the file can be deleted
 * afterwards. The class can be used for read-only purposes. It uses a direct
 * ByteBuffer, which is backed by a byte-array. Externally the byte-arrays size
 * can be set using the {@link #FileByteBufferReader(FileChannel, int)}
 * constructor.
 * 
 * @author pmeisen
 * 
 */
public class FileByteBufferReader implements IByteBufferReader {
	private final long limit;
	private final int bufferSizeInByte;

	private long curPos;
	private int curArrayPos;

	private final ByteBuffer buffer;
	private final byte[] array;

	private final FileInputStream fileInputStream;
	private final FileChannel channel;

	/**
	 * Default constructor creates a {@code ByteBufferReader} for the specified
	 * {@code file}.
	 * 
	 * @param file
	 *            the {@code File} to create the reader for
	 * 
	 * @throws FileNotFoundException
	 *             if the file cannot be found
	 */
	public FileByteBufferReader(final File file) throws FileNotFoundException {
		this(file, 1024);
	}

	/**
	 * Default constructor creates a {@code ByteBufferReader} for the specified
	 * {@code file}.
	 * 
	 * @param file
	 *            the {@code File} to create the reader for
	 * @param arraySizeInByte
	 *            the size of the internally used array
	 * 
	 * @throws FileNotFoundException
	 *             if the file cannot be found
	 */
	public FileByteBufferReader(final File file, final int arraySizeInByte)
			throws FileNotFoundException {
		this(new FileInputStream(file), null, arraySizeInByte);
	}

	/**
	 * Default constructor creates a {@code ByteBufferReader} for the specified
	 * {@code channel}.
	 * 
	 * @param channel
	 *            the {@code FileChannel} to create the reader for
	 */
	public FileByteBufferReader(final FileChannel channel) {
		this(channel, 1024);
	}

	/**
	 * Default constructor creates a {@code ByteBufferReader} for the specified
	 * {@code channel}.
	 * 
	 * @param channel
	 *            the {@code FileChannel} to create the reader for
	 * @param arraySizeInByte
	 *            the size of the internally used array
	 */
	public FileByteBufferReader(final FileChannel channel,
			final int arraySizeInByte) {
		this(null, channel, arraySizeInByte);
	}

	/**
	 * Internally used constructor which initializes the reader.
	 * 
	 * @param fis
	 *            a {@code FileInputStream} if one was defined to create the
	 *            instance
	 * @param channel
	 *            a {@code FileChannel} if one was defined to create the
	 *            instance
	 * @param arraySizeInByte
	 *            the size of the internally used array
	 */
	protected FileByteBufferReader(final FileInputStream fis,
			final FileChannel channel, final int arraySizeInByte) {
		this.fileInputStream = fis;
		this.channel = channel == null ? fis.getChannel() : channel;

		this.curArrayPos = 0;
		this.curPos = 0l;

		try {
			limit = this.channel.size();
		} catch (final IOException e) {
			throw new IllegalArgumentException(
					"The size of the channel could not be read", e);
		}

		// the buffer size can be a maximum of the file-size
		final int initArraySizeInByte = (int) Math.min(this.getLimit(),
				arraySizeInByte);

		// determine the sizes
		this.array = new byte[initArraySizeInByte];
		if (initArraySizeInByte == this.getLimit()) {
			this.buffer = null;
			this.bufferSizeInByte = 0;

			fillArray();
		} else {
			this.bufferSizeInByte = (int) Math.min(getLimit(),
					Math.min(Integer.MAX_VALUE, 10l * arraySizeInByte));

			this.buffer = ByteBuffer.allocateDirect(this.bufferSizeInByte);
			this.buffer.position(0);
			this.buffer.limit(this.bufferSizeInByte);

			fillBuffer();
			fillArray();
		}
	}

	@Override
	public byte get() {
		if (!hasRemaining()) {
			throw new BufferUnderflowException();
		}

		final int curArraySize = array.length;
		final byte value = array[curArrayPos];
		curArrayPos++;
		curPos++;

		// next read would fail, therefore reload the array
		if (curArrayPos >= curArraySize) {
			fillArray();

			curArrayPos = 0;
		}
		return value;
	}

	/**
	 * Fills the array with data from the buffer, or the file if no buffer is
	 * needed.
	 */
	protected void fillArray() {
		final int curArraySize = array.length;

		if (buffer == null) {
			try {
				channel.read(ByteBuffer.wrap(array));
			} catch (IOException e) {
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
				buffer.get(array, bufferedBytes, curArraySize - bufferedBytes);
			}
		}
	}

	/**
	 * Fills the buffer with data from the file.
	 */
	protected void fillBuffer() {
		buffer.clear();
		final int readFileBytes;
		try {
			readFileBytes = channel.read(buffer);
		} catch (final IOException e) {
			throw new IllegalStateException("Unable to read channel.", e);
		}
		if (readFileBytes == -1) {
			curPos = limit;
		} else {
			buffer.position(0);
			buffer.limit(readFileBytes);
		}
	}

	@Override
	public void get(final byte[] dst) {
		get(dst, 0, dst.length);
	}

	@Override
	public void get(final byte[] dst, final int offset, final int length) {
		if (length > remaining()) {
			throw new BufferUnderflowException();
		}

		int end = offset + length;
		for (int i = offset; i < end; i++) {
			dst[i] = get();
		}
	}

	/**
	 * Gets the amount of remaining bytes to be read.
	 * 
	 * @return the amount of remaining bytes to be read
	 */
	public long remaining() {
		return Math.max(0, getLimit() - curPos);
	}

	@Override
	public void close() throws IOException {
		Streams.closeIO(channel);
		Streams.closeIO(fileInputStream);
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

	/**
	 * Gets the limit, i.e. the maximal amount of bytes to read.
	 * 
	 * @return the limit, i.e. the maximal amount of bytes to read
	 */
	public long getLimit() {
		return this.limit;
	}

	@Override
	public boolean hasRemaining() {
		return curPos < this.limit;
	}
}
