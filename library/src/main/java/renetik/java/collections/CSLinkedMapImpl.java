package renetik.java.collections;

import java.util.Map;

import static renetik.android.lang.CSLang.list;

public class CSLinkedMapImpl<K, V> extends java.util.LinkedHashMap<K, V> implements CSLinkedMap<K, V> {

    public CSLinkedMapImpl(Map<? extends K, ? extends V> m){
        super(m);
    }

    public CSLinkedMapImpl(){
    }


    public V getValue(int index) {
        return list(values()).get(index);
    }

    public int index(K key) {
        return list(keySet()).index(key);
    }

    public boolean hasKey(K key) {
        return super.containsKey(key);
    }

    public boolean hasValue(V value) {
        return super.containsValue(value);
    }

    public V value(K key) {
        return super.get(key);
    }

    public V removeAt(int i) {
        return remove(list(keySet()).get(i));
    }
}
