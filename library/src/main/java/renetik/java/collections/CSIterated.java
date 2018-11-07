package renetik.java.collections;

import renetik.java.lang.CSValueInterface;

public interface CSIterated<T> extends CSValueInterface<T> {
	T getNext();

	T getPrevious();

	int index();

	void remove();
}
