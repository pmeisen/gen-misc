package net.meisen.general.genmisc.resources;

import java.util.ResourceBundle;

/**
 * Some helper implementation to work with <code>ResourceBundle</code>
 * instances.
 * 
 * @author pmeisen
 */
public class BundleHelper {

	/**
	 * Gets a localized message from the <code>ResourceBundle</code>
	 * 
	 * @param bundle
	 *            the bundle to be used to retrieve the message from
	 * @param msgId
	 *            the identifier of the message to be retrieved
	 * @param parameters
	 *            the parameters to be replaced
	 * @return the localized message
	 */
	public static String get(final ResourceBundle bundle, final String msgId,
			final String... parameters) {

		// the bundle to be used
		if (bundle == null) {
			return null;
		}

		// get the message and replace the parameters
		String message = bundle.getString(msgId);

		// replace the used parameters
		int i = 0;
		for (final String parameter : parameters) {
			i++;

			if (parameter != null)
				message = message.replace("%" + i, parameter);
		}

		return message;
	}
}
