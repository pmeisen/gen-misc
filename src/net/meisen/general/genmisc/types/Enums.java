package net.meisen.general.genmisc.types;

import java.util.HashMap;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Helper class for {@link Enum}
 * 
 * @author pmeisen
 * 
 */
public class Enums {

	/**
	 * A {@link NamedEnum} is an {@link Enum} which has a specific name
	 * 
	 * @author pmeisen
	 * 
	 */
	public interface NamedEnum {

		/**
		 * @return a name for the {@link Enum}
		 */
		public String getName();
	}

	/**
	 * A {@link LocalizedEnum} is an {@link Enum} which has names which are
	 * localized (i.e. using some {@link ResourceBundle} to offer different
	 * names according to a localization)
	 * 
	 * @author pmeisen
	 */
	public interface LocalizedEnum extends NamedEnum {

		/**
		 * @return the name of the property used to identify the name of the
		 *         localization in the property list
		 */
		public String getPropertyName();
	};

	/**
	 * 
	 * @param localizedEnums
	 *            the array of {@link LocalizedEnum} which should be localized
	 * @param resources
	 *            the {@link ResourceBundle} which has the values for available
	 *            resources
	 * @return a {@link Map} of translated names for the {@link Enum}
	 */
	public static Map<String, LocalizedEnum> getLocalizedNames(
			final LocalizedEnum[] localizedEnums, final ResourceBundle resources) {
		final Map<String, LocalizedEnum> names = new HashMap<String, LocalizedEnum>();

		if (localizedEnums != null) {

			for (final LocalizedEnum localizedEnum : localizedEnums) {
				final String propertyName = localizedEnum.getPropertyName();

				String localizedName = propertyName;
				try {
					localizedName = (String) resources.getObject(propertyName);
				} catch (final MissingResourceException missing) {
					// just ignore the error
				}

				// add the name
				names.put(localizedName, localizedEnum);
			}
		}

		return names;
	}
}
