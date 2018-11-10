package renetik.android.java.collections;

import renetik.android.java.lang.CSValueInterface;

public interface CSIterated<T> extends CSValueInterface<T> {
	T getNext();

	T getPrevious();

	int index();

	void remove();
}
