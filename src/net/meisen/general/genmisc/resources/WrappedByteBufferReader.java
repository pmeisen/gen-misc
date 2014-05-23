package net.meisen.general.genmisc.resources;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * A wrapper of a {@code ByteBuffer} to be used as {@code ByteBufferReader}.
 * 
 * @author pmeisen
 * 
 */
public class WrappedByteBufferReader implements IByteBufferReader {

	private final ByteBuffer byteBuffer;

	/**
	 * Default constructor which specifies which {@code ByteBuffer} to be
	 * wrapped.
	 * 
	 * @param byteBuffer
	 *            the {@code ByteBuffer} to be wrapped, cannot be {@code null}
	 */
	public WrappedByteBufferReader(final ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}

	@Override
	public final boolean hasRemaining() {
		return byteBuffer.hasRemaining();
	}

	@Override
	public byte get() {
		return byteBuffer.get();
	}

	@Override
	public void get(byte[] dst, int offset, int length) {
		byteBuffer.get(dst, offset, length);
	}

	@Override
	public void get(byte[] dst) {
		byteBuffer.get(dst);
	}

	@Override
	public void close() throws IOException {
		byteBuffer.clear();
	}
}
