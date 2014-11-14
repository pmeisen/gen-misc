package net.meisen.general.genmisc.raster.definition.impl;

import net.meisen.general.genmisc.raster.configuration.IRasterConfiguration;
import net.meisen.general.genmisc.raster.data.IModelData;
import net.meisen.general.genmisc.raster.data.IRasterModelData;
import net.meisen.general.genmisc.raster.definition.IRasterModelEntry;
import net.meisen.general.genmisc.raster.definition.RasterModelEntryType;
import net.meisen.general.genmisc.raster.function.IRasterFunction;
import net.meisen.general.genmisc.raster.function.IsAggregatable;
import net.meisen.general.genmisc.raster.function.IsDataInvariant;
import net.meisen.general.genmisc.raster.function.IsIntervalAndGroupInvariant;
import net.meisen.general.genmisc.raster.function.IsIntervalInvariant;
import net.meisen.general.genmisc.raster.function.IsInvariant;

/**
 * A usable base-implementation of a <code>RasterModelEntry</code>. The
 * implementation defines an <code>RasterModelEntry</code> by a
 * <code>RasterFunction</code>.
 * 
 * @author pmeisen
 * 
 */
public class BaseRasterModelEntry implements IRasterModelEntry {
	private final String name;
	private final RasterModelEntryType type;
	private final IRasterFunction function;
	private final Object[] parameter;

	/**
	 * Constructs a <code>RasterModelEntry</code> which is based on a
	 * <code>RasterFunction</code>.
	 * 
	 * @param name
	 *            the name of the entry used for references
	 * @param type
	 *            the <code>RasterModelEntryType</code> of this
	 *            <code>RasterModelEntry</code>, cannot be <code>null</code>
	 * @param function
	 *            the <code>IRasterFunction</code> to be used to create the
	 *            information, cannot be <code>null</code>
	 * @param functionParameters
	 *            the parameters which should be passed to the
	 *            <code>RasterFunction</code>
	 * 
	 * @throws IllegalArgumentException
	 *             if the passed <code>function</code> is illegal
	 */
	public BaseRasterModelEntry(final String name,
			final RasterModelEntryType type, final IRasterFunction function,
			final Object... functionParameters) throws IllegalArgumentException {
		if (type == null) {
			throw new IllegalArgumentException(
					"The RasterModelEntryType cannot be null.");
		} else if (function == null) {
			throw new IllegalArgumentException(
					"The RasterFunction cannot be null.");
		} else if (name == null) {
			throw new IllegalArgumentException("The name cannot be null.");
		} else if (RasterModelEntryType.INTERVALSTART.equals(type)
				&& function instanceof IsIntervalAndGroupInvariant == false) {
			throw new IllegalArgumentException(
					"An INTERVALEND entry must use a function which is interval and group invariant.");
		} else if (RasterModelEntryType.INTERVALEND.equals(type)
				&& function instanceof IsIntervalAndGroupInvariant == false) {
			throw new IllegalArgumentException(
					"An INTERVALEND entry must use a function which is interval and group invariant.");
		} else if (RasterModelEntryType.GROUP.equals(type)
				&& (function instanceof IsIntervalAndGroupInvariant
						|| function instanceof IsDataInvariant || function instanceof IsInvariant) == false) {
			throw new IllegalArgumentException(
					"A GROUP entry must use a function which is interval and group invariant.");
		}

		this.name = name;
		this.type = type;
		this.function = function;
		this.parameter = functionParameters;
	}

	@Override
	public RasterModelEntryType getEntryType() {
		return type;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void initTo(final IRasterModelData rasterModelData) {
		rasterModelData.setValue(name, function.getInitialValue());
	}

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IModelData modelData, final IRasterModelData rasterModelData,
			final Object intervalStart, final Object intervalEnd) {

		final Object value;
		if (isAggregatable()) {
			final IsAggregatable function = (IsAggregatable) this.function;
			value = function.execute(modelId, configuration, this, modelData,
					rasterModelData, intervalStart, intervalEnd);
		} else if (isDataInvariant()) {
			value = execute(modelId, configuration, intervalStart, intervalEnd);
		} else {
			value = execute(modelId, configuration, modelData);
		}

		rasterModelData.setValue(getName(), value);
		return value;
	}

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final IModelData modelData) {

		final Object value;
		if (isAggregatable()) {
			throw new IllegalStateException(
					"Executing an RasterModelDataEntry as invariant using a aggragatable RasterFunction (i.e. aggregatable)");
		} else if (isIntervalInvariant()) {
			final IsIntervalInvariant function = (IsIntervalInvariant) this.function;
			value = function.execute(modelId, configuration, this, modelData);
		} else if (isIntervalAndGroupInvariant()) {
			final IsIntervalAndGroupInvariant function = (IsIntervalAndGroupInvariant) this.function;
			value = function.execute(modelId, configuration, this, modelData);
		} else if (isInvariant()) {
			value = execute(modelId, configuration);
		} else {
			throw new IllegalStateException(
					"Executing an RasterModelDataEntry of unknown type");
		}

		return value;
	}

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration,
			final Object intervalStart, final Object intervalEnd) {

		final Object value;
		if (isDataInvariant()) {
			final IsDataInvariant function = (IsDataInvariant) this.function;
			value = function.execute(modelId, configuration, this,
					intervalStart, intervalEnd);
		} else if (isInvariant()) {
			value = execute(modelId, configuration);
		} else {
			throw new IllegalStateException(
					"Executing an RasterModelDataEntry as data invariant using a none data invariant RasterFunction");
		}

		return value;
	}

	@Override
	public Object execute(final String modelId,
			final IRasterConfiguration<?> configuration) {

		final Object value;
		if (!isInvariant()) {
			throw new IllegalStateException(
					"Executing an RasterModelDataEntry as invariant using a none invariant RasterFunction");
		} else {
			final IsInvariant function = (IsInvariant) this.function;
			value = function.execute(modelId, configuration, this);
		}

		return value;
	}

	@Override
	public boolean isIntervalAndGroupInvariant() {
		return function instanceof IsIntervalAndGroupInvariant;
	}

	@Override
	public boolean isAggregatable() {
		return function instanceof IsAggregatable;
	}

	@Override
	public boolean isIntervalInvariant() {
		return function instanceof IsIntervalInvariant;
	}

	@Override
	public boolean isDataInvariant() {
		return function instanceof IsDataInvariant;
	}

	@Override
	public boolean isInvariant() {
		return function instanceof IsInvariant;
	}

	@Override
	public Object[] getFunctionParameter() {
		return parameter;
	}
}
