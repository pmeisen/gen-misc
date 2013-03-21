package net.meisen.general.genmisc.collections;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A {@link Map} which does not satisfy all the criteria a map implies. The
 * <code>MultiMap</code> can have several values associated to a specific key.
 * 
 * @author pmeisen
 * 
 * @param <K>
 *            the type of the key
 * @param <V>
 *            the type of the values added to the {@link Map}
 */
public class MultiMap<K, V> implements Map<K, V> {

	final Map<K, List<V>> innerMap = new HashMap<K, List<V>>();

	// return all values of the map
	@Override
	public Collection<V> values() {
		final List<V> all = new ArrayList<V>();

		// go through all the lists
		for (final List<V> values : innerMap.values()) {
			all.addAll(values);
		}

		return all;
	}

	// remove everything
	@Override
	public void clear() {
		innerMap.clear();
	}

	@Override
	public V put(final K key, V value) {

		// get the list
		List<V> list = innerMap.get(key);

		// check if one is available, otherwise create it
		if (list == null) {
			list = new ArrayList<V>();
			innerMap.put(key, list);
		}

		// add the value
		list.add(value);

		// return always null, cause there was never a value
		return null;
	}

	@Override
	public boolean containsKey(final Object key) {
		return innerMap.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	/**
	 * Returns an entry set of only the first associated values
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		final Set<Entry<K, V>> set = new HashSet<Entry<K, V>>();

		for (final Entry<K, List<V>> e : innerMap.entrySet()) {
			final K key = e.getKey();
			final V value = getFirstValue(e.getValue());

			// create the entry
			final Entry<K, V> entry = new SimpleEntry<K, V>(key, value);
			set.add(entry);
		}

		return set;
	}

	@Override
	public boolean isEmpty() {
		return innerMap.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return innerMap.keySet();
	}

	@Override
	public void putAll(final Map<? extends K, ? extends V> m) {

		// add each value separately
		for (final Entry<? extends K, ? extends V> e : m.entrySet()) {
			put(e.getKey(), e.getValue());
		}
	}

	/**
	 * Returns the first value of the removed key, if multiply values were
	 * associated
	 */
	@Override
	public V remove(final Object key) {
		final List<V> list = innerMap.remove(key);
		return getFirstValue(list);
	}

	@Override
	public int size() {
		return innerMap.size();
	}

	/**
	 * Returns the first value of the key, if multiply values are associated
	 */
	@Override
	public V get(final Object key) {
		final List<V> list = innerMap.get(key);
		return getFirstValue(list);
	}

	/**
	 * Returns the {@link Collection} of the associated values to the specified
	 * key
	 * 
	 * @param key
	 *            the key to get all associated values for
	 * @return the {@link Collection} of all associated values, will never
	 *         return <code>null</code> instead an empty {@link Collection} will
	 *         be returned
	 */
	public Collection<V> getAll(final Object key) {
		final List<V> l = innerMap.get(key);

		return l == null ? new ArrayList<V>() : l;
	}

	private V getFirstValue(final List<V> list) {
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}
}