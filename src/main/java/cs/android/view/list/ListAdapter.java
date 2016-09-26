package cs.android.view.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListAdapter extends BaseAdapter {

    private final ListController _controller;

    public ListAdapter(ListController controller) {
        _controller = controller;
    }

    public int getCount() {
        return _controller.getItemsCount();
    }

    public int getViewTypeCount() {
        return _controller.getItemTypesCount();
    }

    public boolean isEnabled(int position) {
        return _controller.isEnabled(position);
    }

    public Object getItem(int position) {
        return _controller.data().at(position);
    }

    public int getItemViewType(int position) {
        return _controller.getItemViewType(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {
        return _controller.getRowView(position, view);
    }
}