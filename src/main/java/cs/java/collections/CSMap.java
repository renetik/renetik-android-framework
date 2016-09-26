package cs.java.collections;

public interface CSMap<K, V> extends java.util.Map<K, V> {
	@Override
	@Deprecated public boolean containsKey(Object key);

	@Override
	@Deprecated public boolean containsValue(Object value);

	@Override
	@Deprecated V get(Object key);

	boolean hasKey(K key);

	boolean hasValue(V value);

	V value(K key);
}
