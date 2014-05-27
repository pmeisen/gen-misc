package net.meisen.general.genmisc.resources;

import java.io.Closeable;
import java.io.IOException;

/**
 * An interface to read bytes, e.g. from a {@code ByteBuffer}.
 * 
 * @author pmeisen
 */
public interface IByteBufferReader extends Closeable {

	/**
	 * Get the next byte.
	 * 
	 * @return the next byte
	 */
	public byte get();

	/**
	 * Fills {@code dst} with the next bytes.
	 * 
	 * @param dst
	 *            the array to be filled
	 */
	public void get(final byte[] dst);

	/**
	 * Fills {@code dst} at the {@code offset} position with {@code length}
	 * bytes. The values {@code dst[offset]} till
	 * {@code dst[offset + length - 1]} are set with the next {@code length}
	 * values of the buffer.
	 * 
	 * @param dst
	 *            the array to be filled
	 * @param offset
	 *            the position to start the filling at
	 * @param length
	 *            the amount of values to fill
	 */
	public void get(final byte[] dst, final int offset, final int length);

	@Override
	public void close();

	/**
	 * Checks if bytes to be read are available
	 * 
	 * @return {@code true} if values are available, {@code false} otherwise
	 */
	public boolean hasRemaining();
}