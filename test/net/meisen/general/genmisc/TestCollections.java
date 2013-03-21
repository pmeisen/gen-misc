package net.meisen.general.genmisc;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;

import net.meisen.general.genmisc.collections.Collections;
import net.meisen.general.genmisc.collections.Collections.Filter;

/**
 * Tests for the {@link Collections}
 * 
 * @author pmeisen
 * 
 */
public class TestCollections {

	/**
	 * Tests the {@link Collections#get(int, Collection)} implementation
	 */
	@Test
	public void testGetByPosition() {
		final List<Integer> nrList = new ArrayList<Integer>();
		
		// add some values
		for (int i = 0; i < 100; i++) {
			nrList.add(i);
		}

		// check the values
		for (int i = 0; i < 100; i++) {
			assertEquals(Collections.get(i, nrList), new Integer(i));
		}
	}

	/**
	 * Test the implementation of the
	 * {@link Collections#filter(Collection, Filter)} method
	 */
	@Test
	public void testFilter() {
		final List<Integer> nrList = new ArrayList<Integer>();

		// add some values
		for (int i = 0; i < 100; i++) {
			nrList.add(i);
		}

		// create a filter
		final Filter<Integer> filter = new Filter<Integer>() {

			@Override
			public boolean check(final Integer value) {
				return value.compareTo(50) < 0;
			}
		};

		// check the result of the filter
		Collection<Integer> filteredList;
		filteredList = Collections.filter(nrList, null);
		assertEquals(filteredList.size(), 100);
		for (int i = 0; i < 100; i++) {
			assertEquals(filteredList.contains(i), true);
		}
		filteredList = Collections.filter(nrList, filter);
		assertEquals(filteredList.size(), 50);
		for (int i = 0; i < 50; i++) {
			assertEquals(filteredList.contains(i), true);
		}
		for (int i = 50; i < 100; i++) {
			assertEquals(filteredList.contains(i), false);
		}
	}

	/**
	 * Tests the exception thrown by the
	 * {@link Collections#get(int, Collection)} implementation, if the index is
	 * out of bound (lower)
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidGetByPositionUnder() {
		final List<Integer> nrList = new ArrayList<Integer>();

		// add some values
		for (int i = 0; i < 100; i++) {
			nrList.add(i);
		}

		Collections.get(-1, nrList);
	}

	/**
	 * Tests the exception thrown by the
	 * {@link Collections#get(int, Collection)} implementation, if the index is
	 * out of bound (upper)
	 */
	@Test(expected = IndexOutOfBoundsException.class)
	public void testInvalidGetByPositionOver() {
		final List<Integer> nrList = new ArrayList<Integer>();

		// add some values
		for (int i = 0; i < 100; i++) {
			nrList.add(i);
		}

		Collections.get(100, nrList);
	}

	/**
	 * Tests the generation of
	 */
	@Test
	public void testHashCode() {

		// the null Collection
		assertEquals(Collections.generateHashCode(null, 0, 5), 0);

		// start with an empty List
		final List<Object> values = new ArrayList<Object>();
		assertEquals(Collections.generateHashCode(values, 0, 5), 0);
		assertEquals(Collections.generateHashCode(values, 10, 5), 0);

		// add a value
		values.add(new Integer(5));
		assertEquals(Collections.generateHashCode(values, 0, 5), 25);
		assertEquals(Collections.generateHashCode(values, 10, 5), 35);

		// add a value
		values.add(new Integer(1));
		assertEquals(Collections.generateHashCode(values, 0, 5), 30);
		assertEquals(Collections.generateHashCode(values, 10, 5), 40);
	}

	/**
	 * Checks the equality implementations in <code>Collections</code>
	 */
	@Test
	public void testEqual() {

		// lets check lists
		List<String> list1 = new ArrayList<String>();
		List<String> list2 = new LinkedList<String>();

		list1.add("KATZE");
		list1.add("MAUS");
		list1.add("HUND");
		list2.add("HUND");
		assertEquals(Collections.checkEqual(list1, list2, true), false);
		assertEquals(Collections.checkEqual(list1, list2, true), false);
		list2.add("KATZE");
		assertEquals(Collections.checkEqual(list1, list2, true), false);
		assertEquals(Collections.checkEqual(list1, list2, true), false);
		list2.add("MAUS");
		assertEquals(Collections.checkEqual(list1, list2, false), false);
		assertEquals(Collections.checkEqual(list1, list2, true), true);

		// lets also check a comparison of sets
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new TreeSet<String>();
		set1.add("KATZE");
		set1.add("MAUS");
		set1.add("HUND");
		set2.add("HUND");
		assertEquals(Collections.checkEqual(set1, set2, true), false);
		set2.add("KATZE");
		assertEquals(Collections.checkEqual(set1, set2, true), false);
		set2.add("MAUS");
		assertEquals(Collections.checkEqual(set1, set2, true), true);

		// and of maps
		Map<String, Object> map1 = new HashMap<String, Object>();
		Map<String, Object> map2 = new HashMap<String, Object>();

		map1.put("ESGINGEINMAL", new Integer(5));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);
		map1.put("EINWESEN", new Double(5));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);

		map2.put("EINWESEN", new Double(5));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);
		map2.put("ESGINGEINMAL", new Integer(5));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), true);

		map1.put("NACHHAUS", new Date(1000));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);
		map1.put("UNDSCHAUTE", "SAMPLE");
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);
		map1.put("AUSDEMFENSTERRAUS", new BigDecimal(500.00));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), false);

		map2.put("AUSDEMFENSTERRAUS", new BigDecimal(500.00));
		map2.put("UNDSCHAUTE", "SAMPLE");
		map2.put("NACHHAUS", new Date(1000));
		assertEquals(Collections.checkEqual(map1.values(), map2.values(), true), true);
	}
}
