package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import net.meisen.general.genmisc.collections.MultiMap;

/**
 * Tests which are used to test the {@link MultiMap} class
 * 
 * @author pmeisen
 * 
 */
public class TestMultiMap {

	/**
	 * Tests the adding of values to the MultiMap
	 */
	@Test
	public void testAdding() {
		final MultiMap<String, String> multi = new MultiMap<String, String>();

		// check initial values
		assertEquals(multi.getAll("KEY1").size(), 0);
		assertEquals(multi.getAll("KEY2").size(), 0);
		assertEquals(multi.getAll("KEY3").size(), 0);
		assertEquals(multi.getAll("KEY4").size(), 0);

		// add some values
		multi.put("KEY", "VAL1");
		multi.put("KEY", "VAL2");
		multi.put("KEY", "VAL3");
		multi.put("KEY", "VAL4");
		multi.put("KEY", "VAL5");

		// check the values associated
		assertEquals(multi.keySet().size(), 1);
		assertEquals(multi.values().size(), 5);
		assertEquals(multi.getAll("KEY").size(), 5);
		assertEquals(multi.getAll("KEY").contains("VAL1"), true);
		assertEquals(multi.getAll("KEY").contains("VAL2"), true);
		assertEquals(multi.getAll("KEY").contains("VAL3"), true);
		assertEquals(multi.getAll("KEY").contains("VAL4"), true);
		assertEquals(multi.getAll("KEY").contains("VAL5"), true);
		assertEquals(multi.getAll("KEY").contains("VAL6"), false);

		// add some values with different key
		multi.put("KEY1", "VALUE1");
		multi.put("KEY1", "VALUE2");

		// check the values associated
		assertEquals(multi.keySet().size(), 2);
		assertEquals(multi.values().size(), 7);
		assertEquals(multi.getAll("KEY").size(), 5);
		assertEquals(multi.getAll("KEY1").size(), 2);
		assertEquals(multi.getAll("KEY1").contains("VALUE1"), true);
		assertEquals(multi.getAll("KEY1").contains("VALUE2"), true);
		assertEquals(multi.getAll("KEY1").contains("VALUE3"), false);
	}

	/**
	 * Tests the removing of elements from the multi-map
	 */
	@Test
	public void testRemoving() {
		final MultiMap<String, Integer> multi = new MultiMap<String, Integer>();
		
		// add some values
		multi.put("KEY", 1);
		multi.put("KEY", 2);
		multi.put("KEY", 3);
		multi.put("KEY", 4);
		multi.put("KEY", 5);
		multi.put("KEY1", 100);
		multi.put("KEY1", 101);
		
		// make sure we have the key
		assertEquals(multi.keySet().size(), 2);
		assertEquals(multi.getAll("KEY").size(), 5);
		assertEquals(multi.getAll("KEY1").size(), 2);
		assertEquals(multi.values().size(), 7);
		
		// remove the key
		multi.remove("KEY");
		assertEquals(multi.keySet().size(), 1);
		assertEquals(multi.getAll("KEY").size(),  0);
		assertEquals(multi.getAll("KEY1").size(), 2);
		assertEquals(multi.values().size(), 2);
	}
}
