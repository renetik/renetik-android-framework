package cs.java.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CSListImpl<T> extends ArrayList<T> implements CSList<T> {

    public CSListImpl() {
        super();
    }

    public CSListImpl(Collection<? extends T> collection) {
        super(collection);
    }

    public CSListImpl(int capacity) {
        super(capacity);
    }

    public CSListImpl(java.util.List<T> list) {
        super(list);
    }

    public T at(int index) {
        if (index < size() && index >= 0) return get(index);
        return null;
    }

    public boolean delete(T item) {
        return remove(item);
    }

    public T first() {
        return at(0);
    }

    public T second() {
        return at(1);
    }

    public int index(T item) {
        return indexOf(item);
    }

    public boolean hasItems() {
        return size() > 0;
    }

    public boolean isLast(T item) {
        return last() == item;
    }

    public T last() {
        return at(lastIndex());
    }

    public int lastIndex() {
        return size() - 1;
    }

    public CSList<T> range(int fromIndex) {
        return range(fromIndex, size());
    }

    public CSList<T> range(int fromIndex, int toIndex) {
        return new CSListImpl<T>(subList(fromIndex, toIndex));
    }

    public T removeLast() {
        return remove(size() - 1);
    }

    public int count() {
        return size();
    }

    public boolean has(T object) {
        return super.contains(object);
    }

    public int length() {
        return size();
    }

    public CSList<T> removeAll() {
        clear();
        return this;
    }

    public T put(T item) {
        add(item);
        return item;
    }

    public CSList<T> append(T... items) {
        for (T item : items)
            add(item);
        return this;
    }

    public CSList<T> append(List<T> items) {
        for (T item : items)
            add(item);
        return this;
    }

    public CSList<T> insert(int index, T item) {
        add(index, item);
        return this;
    }

    public CSList<T> reload(List<T> values) {
        clear();
        addAll(values);
        return this;
    }

    public List<T> values() {
        return this;
    }
}
