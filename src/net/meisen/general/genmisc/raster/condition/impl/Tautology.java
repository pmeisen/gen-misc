package net.meisen.general.genmisc.raster.condition.impl;

import net.meisen.general.genmisc.raster.condition.IRasterModelCondition;
import net.meisen.general.genmisc.raster.data.IModelData;

/**
 * <code>RasterModelCondition</code> which is always <code>true</code>
 * 
 * @author pmeisen
 * 
 */
public class Tautology implements IRasterModelCondition {

	@Override
	public boolean checkCondition(IModelData data) {
		return true;
	}
}
