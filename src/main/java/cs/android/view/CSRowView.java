package cs.android.view;

import cs.android.viewbase.CSViewInterface;

public interface CSRowView<T> extends CSViewInterface {
	T data();

	void load(T row);
}
