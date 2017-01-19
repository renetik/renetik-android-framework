package cs.java.collections;

import cs.java.lang.CSIValue;

public interface CSIterated<T> extends CSIValue<T> {
	T getNext();

	T getPrevious();

	int index();

	void remove();
}
