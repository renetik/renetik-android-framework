package renetik.java.collections;

import renetik.android.lang.CSLang;

public interface CSMap<K, V> extends java.util.Map<K, V> {
    static <T, D> D value(CSMap<T, D> map, T key) {
        if (CSLang.no(map)) return null;
        return map.value(key);
    }

    @Deprecated
    boolean containsKey(Object key);

    @Deprecated
    boolean containsValue(Object value);

    V get(Object key);

    boolean hasKey(K key);

    boolean hasValue(V value);

    V value(K key);

}