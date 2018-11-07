package renetik.java.collections;

import java.util.Map;

public class CSMapItemImpl<K, V> implements CSMapItem<K, V> {
	private final Map<K, V> map;
	private final K key;

	public CSMapItemImpl(Map<K, V> map, K key) {
		this.map = map;
		this.key = key;
	}

	@Override
	public K key() {
		return key;
	}

	@Override
	public V value() {
		return map.get(key);
	}

}