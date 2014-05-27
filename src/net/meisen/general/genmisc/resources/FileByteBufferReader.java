package net.meisen.general.genmisc.resources;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
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
public class FileByteBufferReader extends BaseByteBufferReader {

	private final Closeable closeable;
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
		this(new FileInputStream(file), null, null, arraySizeInByte);
	}

	/**
	 * Default constructor creates a {@code RandomAccessFile} for the specified
	 * {@code file}.
	 * 
	 * @param file
	 *            the {@code RandomAccesFile} to create the reader for
	 */
	public FileByteBufferReader(final RandomAccessFile file) {
		this(file, 1024);
	}

	/**
	 * Default constructor creates a {@code RandomAccessFile} for the specified
	 * {@code file}.
	 * 
	 * @param file
	 *            the {@code RandomAccesFile} to create the reader for
	 * @param arraySizeInByte
	 *            the size of the internally used array
	 */
	public FileByteBufferReader(final RandomAccessFile file,
			final int arraySizeInByte) {
		this(null, file, null, arraySizeInByte);
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
		this(null, null, channel, arraySizeInByte);
	}

	/**
	 * Internally used constructor which initializes the reader.
	 * 
	 * @param fis
	 *            a {@code FileInputStream} if one was defined to create the
	 *            instance
	 * @param raf
	 *            a {@code RandomAccessFile} if one was defined to create the
	 *            instance
	 * @param channel
	 *            a {@code FileChannel} if one was defined to create the
	 *            instance
	 * @param arraySizeInByte
	 *            the size of the internally used array
	 */
	protected FileByteBufferReader(final FileInputStream fis,
			final RandomAccessFile raf, final FileChannel channel,
			final int arraySizeInByte) {

		// determine the channel to be used
		if (channel != null) {
			this.channel = channel;
			this.closeable = null;
			Streams.closeIO(fis);
			Streams.closeIO(raf);
		} else if (fis != null) {
			this.channel = fis.getChannel();
			this.closeable = fis;
			Streams.closeIO(raf);
		} else if (raf != null) {
			this.channel = raf.getChannel();
			this.closeable = raf;
			Streams.closeIO(fis);
		} else {
			throw new IllegalArgumentException(
					"You must specify a FileChannel, FileInputStream, or RandomAccessFile.");
		}

		try {
			init(arraySizeInByte, this.channel.size());
		} catch (final IOException e) {
			init(arraySizeInByte, -1l);
		}
	}

	@Override
	public void close() {
		super.close();

		Streams.closeIO(channel);
		Streams.closeIO(closeable);
	}

	@Override
	protected int read(final ByteBuffer buffer) throws IOException {
		return channel.read(buffer);
	}
}
