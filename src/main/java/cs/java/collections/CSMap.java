package cs.java.collections;

import cs.java.lang.CSLang;

public interface CSMap<K, V> extends java.util.Map<K, V> {
    static <T, D> D value(CSMap<T, D> map, T key) {
        if (CSLang.no(map)) return null;
        return map.value(key);
    }

    @Deprecated
    public boolean containsKey(Object key);

    @Deprecated
    public boolean containsValue(Object value);

    @Deprecated
    V get(Object key);

    boolean hasKey(K key);

    boolean hasValue(V value);

    V value(K key);
}
