package net.meisen.general.genmisc.exceptions;

public class CatalogedException extends ParametrizedException {
	private int exceptionNumber;

	public CatalogedException(final int exceptionNumber) {
		super("", "");
		this.exceptionNumber = exceptionNumber;
	}

	public int getExceptionNumber() {
		return exceptionNumber;
	}

	public void setExceptionNumber(int exceptionNumber) {
		this.exceptionNumber = exceptionNumber;
	}
}
