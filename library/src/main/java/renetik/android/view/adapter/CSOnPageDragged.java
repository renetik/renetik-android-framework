package renetik.android.view.adapter;

import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import renetik.android.view.CSPagerController;
import renetik.java.callback.CSRunWith;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_IDLE;
import static renetik.android.lang.CSLang.between;
import static renetik.android.lang.CSLang.infof;
import static renetik.android.lang.CSLang.is;
import static renetik.android.lang.CSLang.no;
import static renetik.android.lang.CSLang.run;
import static renetik.android.lang.CSLang.size;

public class CSOnPageDragged implements OnPageChangeListener {

    private CSPagerController<?> _pager;
    private CSRunWith<Integer> _onPageDragged;
    private int _state;
    private Integer _firstVisiblePageIndex;
    private int _draggedPage;
    private CSRunWith<Integer> _onPageReleased;

    public CSOnPageDragged(CSPagerController<?> pager) {
        _pager = pager;
        pager.getView().addOnPageChangeListener(this);
    }

    public CSOnPageDragged onDragged(CSRunWith<Integer> onPageDragged) {
        _onPageDragged = onPageDragged;
        return this;
    }

    public CSOnPageDragged onReleased(CSRunWith<Integer> onPageReleased) {
        _onPageReleased = onPageReleased;
        return this;
    }

    public void onPageScrollStateChanged(int state) {
        _state = state;
//        infof("onPageScrollStateChanged %s", state);
        if (SCROLL_STATE_IDLE == _state) onPageReleased();
    }

    public void onPageScrolled(int firstVisiblePageIndex, float offset, int offsetPixels) {
        if (SCROLL_STATE_DRAGGING == _state &&
                (no(_firstVisiblePageIndex) || _firstVisiblePageIndex != firstVisiblePageIndex)) {
            if (is(_firstVisiblePageIndex)) onPageReleased();
            _firstVisiblePageIndex = firstVisiblePageIndex;
            onPageDragged(firstVisiblePageIndex < _pager.currentIndex() ?
                    firstVisiblePageIndex : _pager.currentIndex() + 1);
        }
    }

    protected void onPageDragged(int index) {
        _draggedPage = index;
        if (between(_draggedPage, 0, size(_pager.controllers()))) {
            run(_onPageDragged, _draggedPage);
            infof("onPageDragged index:%s", index);
        }
    }

    protected void onPageReleased() {
        if (between(_draggedPage, 0, size(_pager.controllers()))) {
            infof("onPageReleased index:%s", _draggedPage);
            run(_onPageReleased, _draggedPage);
        }
        _firstVisiblePageIndex = null;
    }

    public void onPageSelected(int position) {
    }

}
