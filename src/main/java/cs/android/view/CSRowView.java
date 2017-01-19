package cs.android.view;

import cs.android.viewbase.CSIView;

public interface CSRowView<T> extends CSIView {
	T data();

	void load(T row);
}
