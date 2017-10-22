package cs.java.collections;

import cs.java.lang.CSValueInterface;

public interface CSIterated<T> extends CSValueInterface<T> {
	T getNext();

	T getPrevious();

	int index();

	void remove();
}
