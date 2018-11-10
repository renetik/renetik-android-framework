package renetik.android.view.list;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

import java.util.List;

import renetik.android.viewbase.CSView;
import renetik.android.viewbase.CSViewController;
import renetik.android.java.event.CSEvent;

import static android.view.Gravity.BOTTOM;
import static android.view.Gravity.CENTER;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static renetik.android.lang.CSLang.NO;
import static renetik.android.lang.CSLang.event;
import static renetik.android.lang.CSLang.fire;
import static renetik.android.lang.CSLang.no;

public class CSListLoadNextController extends CSViewController {

    public final CSEvent<Void> onLoadNext = event();
    private final CSView<?> _loadView;
    private EndlessScrollListener _scrollListener;
    private boolean _loading;

    public CSListLoadNextController(CSListController parent, int loadViewLayout) {
        super(parent);
        _loadView = new CSView(this, layout(loadViewLayout));
        parent.onLoad.add((registration, list) -> onListLoad((List) list));
    }

    private void onListLoad(List data) {
        _loadView.hide();
        if (no(_scrollListener)) _scrollListener = new EndlessScrollListener();
        else if (data.isEmpty()) _scrollListener = null;
        updateScrollListener();
        _loading = false;
    }

    private void updateScrollListener() {
        asAbsListView().setOnScrollListener(_scrollListener);
    }

    private void onLoadNext() {
        fire(onLoadNext);
        _loading = true;
        _loadView.show();
    }

    public void onResume() {
        super.onResume();
        updateScrollListener();
        if (getView() instanceof ListView) {
            asListView().addFooterView(_loadView.getView());
            asListView().setFooterDividersEnabled(NO);
        } else if (!_loadView.hasParent())
            ((FrameLayout) getView().getParent().getParent()).addView(_loadView.getView(),
                    new LayoutParams(WRAP_CONTENT, WRAP_CONTENT, BOTTOM | CENTER));
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
