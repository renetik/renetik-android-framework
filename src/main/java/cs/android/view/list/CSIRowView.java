package cs.android.view.list;

import cs.android.viewbase.CSViewInterface;

public interface CSIRowView<T> extends CSViewInterface {
    T rowData();

    void rowData(T row);
}
