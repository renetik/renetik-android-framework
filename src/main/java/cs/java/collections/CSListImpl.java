package cs.java.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static cs.java.lang.CSLang.empty;
import static java.util.Arrays.asList;

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

    public int delete(T item) {
        int index = indexOf(item);
        remove(index);
        return index;
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

    public boolean getHasItems() {
        return size() > 0;
    }

    public boolean isLast(T item) {
        return last() == item;
    }

    public T last() {
        return at(lastIndex());
    }

    public T previous() {
        return at(lastIndex() - 1);
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
        if (isEmpty()) return null;
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

    public CSList<T> reverse() {
        Collections.reverse(this);
        return this;
    }

    public CSList<T> removeAll() {
        clear();
        return this;
    }

    public T put(T item) {
        add(item);
        return item;
    }

    public T add(T item, int index) {
        add(index, item);
        return item;
    }

    public T set(T item, int index) {
        set(index, item);
        return item;
    }

    public CSList<T> append(T... items) {
        if (empty(items)) return this;
        addAll(asList(items));
        return this;
    }

    public CSList<T> append(List<T> items) {
        if (empty(items)) return this;
        addAll(items);
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

    public CSList<T> values() {
        return this;
    }
}
