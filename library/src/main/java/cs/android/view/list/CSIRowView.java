package cs.android.view.list;

import cs.android.viewbase.CSViewInterface;

public interface CSIRowView<T> extends CSViewInterface {
    T data();

    void data(T row);
}
