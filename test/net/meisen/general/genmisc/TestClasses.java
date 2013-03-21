package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;

import net.meisen.general.genmisc.test.two.Placeholder1;
import net.meisen.general.genmisc.test.two.Placeholder2;
import net.meisen.general.genmisc.types.Classes;

/**
 * Tests the <code>Classes</code> implementation
 * 
 * @author pmeisen
 * 
 */
public class TestClasses {

	/**
	 * Tests the reading of an empty package
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testEmptyPackage() throws Exception {
		final Set<Class<?>> list = Classes
				.getClasses("net.meisen.general.genmisc.test.empty");
		assertEquals(list.size(), 0);
		list.clear();
	}

	/**
	 * Tests the reading of a full package
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testFullPackage() throws Exception {
		final Set<Class<?>> list = Classes
				.getClasses("net.meisen.general.genmisc.test.two");
		assertEquals(list.size(), 2);
		assertTrue("Contains Placeholder1 class",
				list.contains(Placeholder1.class));
		assertTrue("Contains Placeholder2 class",
				list.contains(Placeholder2.class));
	}

	/**
	 * Tests the reading of a parent package with content
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Test
	public void testParentEmptyPackage() throws Exception {
		final Set<Class<?>> list = Classes
				.getClasses("net.meisen.general.genmisc.test");
		assertEquals(list.size(), 0);
		list.clear();
	}
}
