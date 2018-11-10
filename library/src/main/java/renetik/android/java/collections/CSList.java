package renetik.android.java.collections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import renetik.android.java.lang.CSValues;

public interface CSList<T> extends java.util.List<T>, CSValues<T> {

    @Nullable
    T at(int index);

    int delete(T item);

    T first();

    T second();

    int index(T item);

    boolean getHasItems();

    /**
     * @deprecated Use {@link #index}
     */
    @Deprecated
    int indexOf(Object arg0);

    boolean isLast(T item);

    boolean isLastIndex(int index);

    @Nullable
    T last();

    @Nullable
    T previousLast();

    int count();

    int lastIndex();

    @NotNull
    CSList<T> rangeFrom(int fromIndex);

    @NotNull
    CSList<T> range(int fromIndex, int toIndex);

    /**
     * @deprecated Use {@link #delete}
     */
    @Deprecated
    boolean remove(Object arg0);

    T removeLast();

    @NotNull
    CSList<T> removeAll();

    @NotNull
    T put(T item);

    @NotNull
    T add(T item, int index);

    @NotNull
    T set(T item, int index);

    @NotNull
    CSList<T> append(T... item);

    @NotNull
    CSList<T> append(List<T> items);

    @NotNull
    CSList<T> insert(int index, T item);

    @NotNull
    CSList<T> reload(List<T> values);

    boolean has(T bow);

    int length();

    @NotNull
    CSList<T> reverse();
}
