package cs.java.collections;

import java.util.Iterator;

import static cs.java.lang.Lang.unexpected;

public abstract class CSIterator<T> implements CSIteration<T> {
    private int index = -1;
    private boolean iteratingForward = true;
    private int iterationLength;

    public CSIterator(int length) {
        this.iterationLength = length;
    }

    public abstract T getValue();

    public boolean hasNext() {
        if (iteratingForward) return index < iterationLength - 1;
        return index > 0;
    }

    public int index() {
        return index;
    }

    public Iterator<T> iterator() {
        return this;
    }

    public T next() {
        if (iteratingForward)
            ++index;
        else --index;
        return getValue();
    }

    protected void onRemove() {
        throw unexpected();
    }

    public final void remove() {
        onRemove();
        index--;
        iterationLength--;
    }

    public CSIteration<T> reverse() {
        this.iteratingForward = false;
        index = iterationLength;
        return this;
    }

    public CSIteration<T> skip(int length) {
        if (iteratingForward)
            index += length;
        else index -= length;
        return this;
    }
}