package cs.android.view.list;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import java.util.List;

import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;
import cs.java.event.CSEvent;
import cs.java.event.CSEvent.EventRegistration;
import cs.java.event.CSListener;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.*;
import static cs.java.lang.Lang.NO;
import static cs.java.lang.Lang.event;
import static cs.java.lang.Lang.fire;
import static cs.java.lang.Lang.is;
import static cs.java.lang.Lang.no;

public class ListLoadNextController extends CSViewController {

    private final CSEvent<Void> _onLoadNext = event();
    private final CSView<?> _loadView;
    private EndlessScrollListener _scrollListener;
    private boolean _loading;

    public <T> ListLoadNextController(ListController<T> parent, int loadViewLayout) {
        super(parent);
        _loadView = new CSView(this, layout(loadViewLayout));
        parent.getOnLoad().add(new CSListener<List<T>>() {
            public void onEvent(EventRegistration registration, List<T> arg) {
                onListLoad((CSList<?>) arg);
            }
        });
    }

    private void onListLoad(CSList<?> data) {
        _loadView.hide();
        if (no(_scrollListener)) _scrollListener = new EndlessScrollListener();
        else {
            if (data.isEmpty()) _scrollListener = null;
        }
        updateScrollListener();
        _loading = false;
    }

    private void updateScrollListener() {
        asAbsListView().setOnScrollListener(_scrollListener);
    }

    public CSEvent<Void> eventOnLoadNext() {
        return _onLoadNext;
    }

    private void onLoadNext() {
        fire(_onLoadNext);
        _loading = true;
        _loadView.show();
    }

//    private int totalItemCount() {
//        return is(_data) ? _data.size() : 0;
//    }

    public void onResume() {
        super.onResume();
        updateScrollListener();
        if (asView() instanceof ListView) {
            asListView().addFooterView(_loadView.asView());
            asListView().setFooterDividersEnabled(NO);
        } else {
            if (!_loadView.hasParent())
                ((FrameLayout) asView().getParent().getParent()).addView(_loadView.asView(),
                        new LayoutParams(WRAP_CONTENT, WRAP_CONTENT, BOTTOM | CENTER));
        }
        _loadView.hide();
    }

    private class EndlessScrollListener implements OnScrollListener {
        private int _visibleThreshold = 3;

        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        public void onScroll(AbsListView view, int first, int visible, int total) {
            if (total - visible <= 0 || _loading) return;
            if (total - visible <= first + _visibleThreshold) onLoadNext();
        }
    }
}
