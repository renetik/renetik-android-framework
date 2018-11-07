package renetik.android.view.list;

import renetik.android.viewbase.CSViewInterface;

public interface CSIRowView<T> extends CSViewInterface {
    T data();

    void data(T row);
}
