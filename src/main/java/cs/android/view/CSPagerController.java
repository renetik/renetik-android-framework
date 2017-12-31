package cs.android.view;

import android.content.res.Configuration;

import cs.android.view.adapter.CSOnPageSelected;
import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.size;

public class CSPagerController extends CSViewController {

    private CSList<CSPagerPageInterface> _controllers;
    private CSView _emptyView;
    private int _tabLayout;
    private int _currentIndex;

    public CSPagerController(CSViewController parent, int pagerId) {
        this(parent, pagerId, 0, null);
    }

    public CSPagerController(CSViewController parent, int pagerId, int tabLayout) {
        this(parent, pagerId, tabLayout, null);
    }

    public CSPagerController(CSViewController parent, int pagerId, CSList<CSPagerPageInterface> controllers) {
        this(parent, pagerId, 0, controllers);
    }

    public CSPagerController(CSViewController parent, int pagerId, int tabLayout, CSList<CSPagerPageInterface> controllers) {
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

    public CSPagerController reload(CSList<CSPagerPageInterface> controllers) {
        int currentItem = asPager().getCurrentItem();
        for (CSPagerPageInterface iController : iterate(_controllers)) {
            iController.asController().onDeinitialize(null);
            iController.asController().onDestroy();
        }
        _controllers = controllers;
        for (CSPagerPageInterface page : _controllers) page.asController().onInitialize();
        updateControllersState(_controllers.length() > currentItem ? currentItem : 0);
        if (_controllers.length() > currentItem) asPager().setCurrentItem(currentItem, YES);
        updateView();
        return this;
    }

    private void updateView() {
        if (is(_controllers)) asPager().setAdapter(new CSPagerAdapter(_controllers));
        visible(set(_controllers));
        if (is(_emptyView)) _emptyView.visible(empty(_controllers));
    }

    protected void onCreate() {
        super.onCreate();
        asPager().addOnPageChangeListener(new CSOnPageSelected(this::updateControllersState));
        updateView();
        updateTabsVisibility();
    }

    private void updateTabsVisibility() {
        if (set(_tabLayout)) view(_tabLayout).visible(isPortrait());
    }

    public void onResume() {
        super.onResume();
        updateControllersState(asPager().getCurrentItem());
    }

    private void updateControllersState(int currentIndex) {
        _currentIndex = currentIndex;
        if (empty(_controllers)) return;
        for (int index = 0; index < size(_controllers); index++) {
            CSViewController controller = _controllers.get(index).asController();
            if (index == currentIndex) {
                controller.setActive(YES);
                controller.onResumeNative();
            } else {
                if (controller.isResumed()) controller.onPauseNative();
                controller.setActive(NO);
            }
        }
        invalidateOptionsMenu();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateTabsVisibility();
    }

    public CSPagerPageInterface currentController() {
        if (empty(_controllers)) return null;
        return _controllers.get(_currentIndex);
    }

    public void currentIndex(int index) {
        asPager().setCurrentItem(index);
    }

    public CSList<CSPagerPageInterface> controllers() {
        return _controllers;
    }
}
