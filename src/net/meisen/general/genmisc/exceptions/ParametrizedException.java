package net.meisen.general.genmisc.exceptions;

public class ParametrizedException extends Exception {

	private String[] parameters;

	public ParametrizedException(final String message, final Throwable cause,
			final String... parameters) {
		super(message, cause);

		this.parameters = parameters;
	}

	public ParametrizedException(final String message, final String... parameters) {
		this(message, null, parameters);
	}
}
