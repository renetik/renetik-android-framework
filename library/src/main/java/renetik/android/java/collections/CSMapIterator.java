package renetik.android.java.collections;

import java.util.List;
import java.util.Map;

import static renetik.android.java.collections.CSListKt.list;


public class CSMapIterator<K, V> extends CSIterator<CSMapped<K, V>> implements CSMapped<K, V> {

    private final List<K> keys;
    private final Map<K, V> map;
    private int last_index = -1;

    public CSMapIterator(Map<K, V> map) {
        super(map.size());
        this.map = map;
        this.keys = list(map.keySet());
    }

    public CSMapItem<K, V> getValue() {
        if (last_index != index()) last_index = index();
        return this;
    }

    protected CSMapItem<K, V> getItem(int index) {
        return new CSMapItemImpl<K, V>(map, keys.get(index));
    }

    public CSMapItem<K, V> getNext() {
        return getItem(index() + 1);
    }

    public CSMapItem<K, V> getPrevious() {
        return getItem(index() - 1);
    }

    public CSMapped<K, V> getCurrent() {
        return this;
    }

    public K key() {
        return keys.get(index());
    }

    public void onRemove() {
        removeItem(index());
        last_index = -1;
    }

    protected void removeItem(int index) {
        map.remove(keys.get(index));
        keys.remove(index);
    }

    public V value() {
        return map.get(key());
    }
}
