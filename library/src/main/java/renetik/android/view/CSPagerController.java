package renetik.android.view;

import android.content.res.Configuration;
import androidx.viewpager.widget.ViewPager;

import renetik.android.view.adapter.CSOnPageChanged;
import renetik.android.view.adapter.CSOnPageDragged;
import renetik.android.viewbase.CSView;
import renetik.android.viewbase.CSViewController;
import renetik.java.collections.CSList;

import static renetik.android.lang.CSLang.NO;
import static renetik.android.lang.CSLang.YES;
import static renetik.android.lang.CSLang.debug;
import static renetik.android.lang.CSLang.doLater;
import static renetik.android.lang.CSLang.empty;
import static renetik.android.lang.CSLang.equal;
import static renetik.android.lang.CSLang.info;
import static renetik.android.lang.CSLang.is;
import static renetik.android.lang.CSLang.iterate;
import static renetik.android.lang.CSLang.set;
import static renetik.android.lang.CSLang.size;

public class CSPagerController<Page extends CSPagerPage> extends CSViewController<ViewPager> {

    private CSList<Page> _controllers;
    private CSView _emptyView;
    private int _tabLayout;
    private Integer _currentIndex;
    private int _index;

    public CSPagerController(CSViewController parent, int pagerId, int tabs) {
        this(parent, pagerId, tabs, null);
    }

    public CSPagerController(CSViewController parent, int pagerId, CSList<Page> controllers) {
        this(parent, pagerId, 0, controllers);
    }

    public CSPagerController(CSViewController parent, int pagerId, int tabLayout, CSList<Page> controllers) {
        super(parent, pagerId);
        _tabLayout = tabLayout;
        _controllers = controllers;
    }

    public CSView setEmptyView(int id) {
        return setEmptyView(new CSView<>(parent(), id));
    }

    public CSView setEmptyView(CSView view) {
        _emptyView = view;
        if (is(_emptyView)) _emptyView.visible(empty(_controllers));
        return _emptyView;
    }

    public CSPagerController reload(CSList<Page> controllers) {
        int currentItem = getView().getCurrentItem();
        for (Page iController : iterate(_controllers)) {
            iController.asController().onDeinitialize(null);
            iController.asController().onDestroy();
        }
        _controllers = controllers;
        updateControllersState(_controllers.length() > currentItem ? currentItem : 0);
        for (Page page : _controllers) page.asController().initialize(getState());
        if (_controllers.length() > currentItem) getView().setCurrentItem(currentItem, YES);
        updateView();
        return this;
    }

    private void updateView() {
        if (is(_controllers)) getView().setAdapter(new CSPagerAdapter(_controllers));
        visible(set(_controllers));
        if (is(_emptyView)) _emptyView.visible(empty(_controllers));
    }

    protected void onCreate() {
        super.onCreate();
        new CSOnPageDragged(this)
                .onDragged(draggedIndex -> _controllers.get(draggedIndex).asController().setShowingInContainer(YES))
                .onReleased(draggedIndex -> {
                    if (!equal(currentIndex(), draggedIndex))
                        _controllers.get(draggedIndex).asController().setShowingInContainer(NO);
                });
        getView().addOnPageChangeListener(new CSOnPageChanged(currentIndex -> {
            debug("OnPageChanged", currentIndex);
            doLater(100, () -> updateControllersState(currentIndex));
        }));
        updateView();
        updateTabsVisibility();
    }

    private void updateTabsVisibility() {
        if (set(_tabLayout)) item(_tabLayout).fade(isPortrait());
    }

    private void updateControllersState(int currentIndex) {
        if (equal(_currentIndex, currentIndex)) return;
        _currentIndex = currentIndex;
        if (empty(_controllers)) return;
        for (int index = 0; index < size(_controllers); index++)
            _controllers.get(index).asController().setShowingInContainer(index == currentIndex ? YES : NO);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateTabsVisibility();
    }

    public Page currentController() {
        if (empty(_controllers)) return null;
        return _controllers.get(_currentIndex);
    }

    public void currentIndex(int index) {
        getView().setCurrentItem(index);
    }

    public Integer currentIndex() {
        return _currentIndex;
    }

    public CSList<Page> controllers() {
        return _controllers;
    }

    public boolean isLoaded() {
        return is(_controllers);
    }
}
