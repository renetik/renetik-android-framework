package cs.java.collections;

public class CSMapImpl<K, V> extends java.util.HashMap<K, V> implements CSMap<K, V> {

    public boolean hasKey(K key) {
        return super.containsKey(key);
    }

    public boolean hasValue(V value) {
        return super.containsValue(value);
    }

    public V value(K key) {
        return super.get(key);
    }

}
