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
public class FileByteBufferReader {
	private final long limit;
	private final int bufferSizeInByte;

	private long curPos;
	private int curArrayPos;

	private final ByteBuffer buffer;
	private final byte[] array;

	private final FileInputStream fileInputStream;
	private final FileChannel channel;

	public FileByteBufferReader(final File file) throws FileNotFoundException {
		this(file, 1024);
	}

	public FileByteBufferReader(final File file, final int arraySizeInByte)
			throws FileNotFoundException {
		this(new FileInputStream(file), null, arraySizeInByte);
	}

	public FileByteBufferReader(final FileChannel channel) {
		this(channel, 1024);
	}

	public FileByteBufferReader(final FileChannel channel,
			final int arraySizeInByte) {
		this(null, channel, arraySizeInByte);
	}

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

	public void get(final byte[] dst) {
		get(dst, 0, dst.length);
	}

	public void get(final byte[] dst, final int offset, final int length) {
		if (length > remaining()) {
			throw new BufferUnderflowException();
		}

		int end = offset + length;
		for (int i = offset; i < end; i++) {
			dst[i] = get();
		}
	}

	public long remaining() {
		return Math.max(0, getLimit() - curPos);
	}

	public void close() throws IOException {
		Streams.closeIO(channel);
		Streams.closeIO(fileInputStream);
		Streams.closeIO(buffer);
	}

	public int getBufferSize() {
		return this.bufferSizeInByte;
	}

	public boolean usesBuffer() {
		return this.buffer != null;
	}

	public int getArraySize() {
		return this.array.length;
	}

	public long getLimit() {
		return this.limit;
	}

	public boolean hasRemaining() {
		return curPos < this.limit;
	}
}
