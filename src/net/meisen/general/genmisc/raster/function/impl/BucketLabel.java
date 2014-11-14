package net.meisen.general.genmisc.raster.function.impl;

import java.util.Locale;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.definition.IRasterLogic;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.function.IsDataInvariant;


/**
 * This <code>RasterFunction</code> is used to created
 * 
 * @author pmeisen
 * 
 */
public class BucketLabel implements IsDataInvariant {

	/**
	 * The <code>IFormatter</code> interface can be used to define own
	 * formatters which are not covered by simple
	 * {@link String#format(Locale, String, Object...)} can be done.
	 * 
	 * @author pmeisen
	 */
	public interface IFormatter {

		/**
		 * Method which should be run to format the passed <code>start</code>
		 * and <code>end</code> according to the specified
		 * <code>IFormatter</code>.
		 * 
		 * @param start
		 *            the start object of the interval
		 * @param end
		 *            the end object of the interval
		 * @return the formatted string
		 */
		public String format(final Object start, final Object end);
	}

	@Override
	public Object getInitialValue() {
		return null;
	}

	@Override
	public String execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IRasterModelEntry entry, final Object intervalStart,
			final Object intervalEnd) {
		final Object[] parameter = entry.getFunctionParameter();

		// check a pre-defined format
		final String format;
		final IFormatter formatter;
		if (parameter.length > 0 && parameter[0] instanceof String) {
			format = (String) parameter[0];
			formatter = null;
		} else if (parameter.length > 0 && parameter[0] instanceof IFormatter) {
			format = null;
			formatter = (IFormatter) parameter[0];
		} else {
			format = "%1$s - %2s";
			formatter = null;
		}

		// get the new value to be added
		@SuppressWarnings("unchecked")
		final IRasterLogic<Object> logic = (IRasterLogic<Object>) configuration
				.getLogic();

		// determine the start and end of the current bucket
		final Object start = logic.getAbsoluteBucketStart(intervalStart);
		// it is correct to use intervalStart here, because intervalEnd can be
		// in the next bucket (i.e. if its really the bucket end, the
		// AbsolutBucketEnd will calculate the end of the next bucket). The
		// intervalStart is always within the bucket [bucketStart, bucketEnd),
		// even if [intervalStart, intervalEnd) is provided
		final Object end = logic.getAbsoluteBucketEnd(intervalStart);

		// now label those two
		final Locale locale = configuration.getLocale();

		// get the value
		final String value;
		if (formatter == null) {
			value = String.format(locale, format, start, end);
		} else {
			value = formatter.format(start, end);
		}
		return value;
	}
}
