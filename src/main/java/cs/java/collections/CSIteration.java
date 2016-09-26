package cs.java.collections;

import java.util.Iterator;

public interface CSIteration<T> extends Iterator<T>, Iterable<T> {
	int index();

	CSIteration<T> reverse();

	CSIteration<T> skip(int length);
}