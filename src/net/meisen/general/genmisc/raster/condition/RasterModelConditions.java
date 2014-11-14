package net.meisen.general.genmisc.raster.condition;

import net.meisen.general.genmisc.raster.condition.impl.Tautology;

/**
 * Lists of implemented <code>RasterModelConditions</code>
 * 
 * @see IRasterModelCondition
 * 
 * @author pmeisen
 * 
 */
public class RasterModelConditions {

	/**
	 * A <code>RasterModelCondition</code> which is always <code>true</code>
	 */
	public final static Tautology TAUTOLOGY = new Tautology();
}
