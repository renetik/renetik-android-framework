package cs.java.collections;

import java.util.List;

import cs.java.lang.CSValues;

public interface CSList<T> extends java.util.List<T>, CSValues<T> {

    T at(int index);

    int delete(T item);

    T first();

    T second();

    int index(T item);

    boolean getHasItems();

    @Override
    @Deprecated
    int indexOf(Object arg0);

    boolean isLast(T item);

    T last();

    int count();

    int lastIndex();

    CSList<T> range(int fromIndex);

    CSList<T> range(int fromIndex, int toIndex);

    boolean remove(Object arg0);

    T removeLast();

    CSList<T> removeAll();

    T put(T item);

    T add(T item, int index);

    T set(T item, int index);

    CSList<T> append(T... item);

    CSList<T> append(List<T> items);

    CSList<T> insert(int index, T item);

    CSList<T> reload(List<T> values);

    boolean has(T bow);

    int length();

    CSList<T> reverse();
}
