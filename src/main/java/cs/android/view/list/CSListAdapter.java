package cs.android.view.list;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CSListAdapter extends BaseAdapter {

    private final CSListController _controller;

    public CSListAdapter(CSListController controller) {
        _controller = controller;
    }

    public int getCount() {
        return _controller.size();
    }

    public int getViewTypeCount() {
        return _controller.getItemTypesCount();
    }

    public boolean isEnabled(int position) {
        return _controller.isEnabled(position);
    }

    public Object getItem(int position) {
        return _controller.dataAt(position);
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