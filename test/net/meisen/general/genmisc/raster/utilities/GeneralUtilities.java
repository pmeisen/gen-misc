package net.meisen.general.genmisc.raster.utilities;

import net.meisen.general.genmisc.resources.Resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Some general utilities used for testing
 * 
 * @author pmeisen
 * 
 */
public class GeneralUtilities {

	/**
	 * @param date
	 *            the {@link String} in <code>dd.MM.yyyy HH:mm:ss</code> format
	 * @return the {@link Date} object of the passed {@link String} or current
	 *         date, if the creation failed
	 */
	public static Date getDate(final String date) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

		try {
			return formatter.parse(date);
		} catch (final ParseException e) {
			System.err.println("The date '" + date
							+ "' could not be parsed (Error: '"
							+ e.getMessage() + "')");
			return new Date();
		}
	}

	public static boolean isWindows() {
		final String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win")) {
			return true;
		} else {
			return false;
		}
	}
}
