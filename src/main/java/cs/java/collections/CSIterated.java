package cs.java.collections;

import cs.java.lang.HasValue;

public interface CSIterated<T> extends HasValue<T> {
	T getNext();

	T getPrevious();

	int index();

	void remove();
}
