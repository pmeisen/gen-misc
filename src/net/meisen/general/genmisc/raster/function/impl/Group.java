package net.meisen.general.genmisc.raster.function.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModel;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.function.IsIntervalInvariant;

/**
 * <code>RasterFunction</code> used to create a label for a Group. The
 * <code>RasterFunction</code> can get a formatting parameter, which is based on
 * the {@link Formatter} syntax. Instead of using %1 to identify the first
 * parameter, the <code>RasterFunction</code> uses [ENTRYNAME] identifier,
 * whereby the formatting parameter can be passed within [ENTRYNAME$tM].<br />
 * <br />
 * <b>Samples:</b>
 * <ul>
 * <li><code>This is the value of [ENTRY1]</code><br/>
 * having an ENTRY1 <code>RasterModelEntry</code> with ENTRY1-value 'SAMPLE'
 * will return:<br/>
 * <code>This is the value of SAMPLE</code></li>
 * <li><code>[ENTRY1$td]-[ENTRY1$tB]-[ENTRY1$tY]</code><br/>
 * with ENTRY1-value '20.01.1981' will return (depending on the
 * <code>Locale</code> of the <code>RasterConfiguration</code>):<br/>
 * <code>20-January-1981</code></li>
 * </li>
 * </ul>
 * 
 * @author pmeisen
 * 
 */
public class Group extends BaseRasterFunction implements IsIntervalInvariant {

	@Override
	public Object getInitialValue() {
		return null;
	}

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final IModelData modelData) {

		// get the definitions
		final Object[] parameter = entry.getFunctionParameter();
		final IRasterModel model = configuration.getModel(modelId);

		// check a pre-defined format
		final String paramFormat;
		if (parameter.length > 0 && parameter[0] instanceof String) {
			paramFormat = (String) parameter[0];
		} else {
			paramFormat = null;
		}

		// get the parameters and replace the holders
		final Collection<IRasterModelEntry> values = model
				.getEntries(RasterModelEntryType.GROUP);

		// get the values for the place-holders and the format
		int i = 1;
		String definedFormat = (paramFormat == null ? "" : paramFormat);
		String madeFormat = "";
		final List<Object> parameters = new ArrayList<Object>(values.size());
		for (final IRasterModelEntry e : values) {

			// create a formatting String, if none is defined
			madeFormat += (i == 1 ? "" : "-") + "%" + i + "$s";

			// create a regular expression to find replacements
			final Pattern pattern = Pattern.compile("\\[" + e.getName()
					+ "(\\$[^\\]]+)?\\]");

			// now match the defined format
			boolean lookUp = true;
			while (lookUp) {
				final Matcher matcher = pattern.matcher(definedFormat);
				if (matcher.find()) {
					String formatParams = matcher.group(1);
					if (formatParams == null) {
						formatParams = "$s";
					}

					// now replace the value
					definedFormat = definedFormat.substring(0, matcher.start())
							+ "%" + i + formatParams
							+ definedFormat.substring(matcher.end());
				} else {
					lookUp = false;
				}
			}

			// execute each function
			final Object o = e.execute(modelId, configuration, modelData);
			parameters.add(o);

			i++;
		}

		// get the final formats
		final String format = paramFormat == null ? madeFormat : definedFormat;
		final Locale locale = configuration.getLocale();

		// get the value
		return String.format(locale, format, parameters.toArray());
	}
}
