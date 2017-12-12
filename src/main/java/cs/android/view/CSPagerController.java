package cs.android.view;

import android.support.design.widget.TabLayout;

import cs.android.view.adapter.CSOnPageChange;
import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;
import cs.java.lang.CSLang;

import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.set;
import static cs.java.lang.CSLang.size;

public class CSPagerController<T extends CSViewController & CSPagerPageInterface> extends CSViewController {

    private CSList<T> _controllers;
    private CSView _emptyView;
    private int _tabLayout;

    public CSPagerController(CSViewController parent, int pagerId) {
        this(parent, pagerId, 0, null);
    }

    public CSPagerController(CSViewController parent, int pagerId, int tabLayout) {
        this(parent, pagerId, tabLayout, null);
    }

    public CSPagerController(CSViewController parent, int pagerId, CSList<T> controllers) {
        this(parent, pagerId, 0, controllers);
    }

    public CSPagerController(CSViewController parent, int pagerId, int tabLayout, CSList<T> controllers) {
        super(parent, pagerId);
        gone(YES);
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

    public CSPagerController reload(CSList<T> controllers) {
        int currentItem = asPager().getCurrentItem();
        for (CSViewController controller : iterate(_controllers)) {
            controller.onDeinitialize(null);
            controller.onDestroy();
        }
        _controllers = controllers;
        for (T controller : _controllers) controller.onInitialize();
        updateControllersState(_controllers.length() > currentItem ? currentItem : 0);
        if (_controllers.length() > currentItem) asPager().setCurrentItem(currentItem, YES);
        updateView();
        return this;
    }

    private void updateView() {
        if (is(_controllers)) asPager().setAdapter(new CSPagerAdapter(_controllers));
        visible(set(_controllers));
        if (is(_emptyView)) _emptyView.visible(empty(_controllers));
        if (set(_tabLayout)) {
            for (int i = 0; i < _controllers.size(); i++) {
                T controller = _controllers.get(i);
                if (set(controller.csPagerControllerImage()))
                    getView(_tabLayout, TabLayout.class).getTabAt(i).setIcon(controller.csPagerControllerImage());
            }
        }
    }

    protected void onCreate() {
        super.onCreate();
        asPager().addOnPageChangeListener(new CSOnPageChange() {
            public void onPageSelected(int index) {
                updateControllersState(index);
            }
        });
        updateView();
    }

    public void onResume() {
        super.onResume();
        updateControllersState(asPager().getCurrentItem());
    }

    private void updateControllersState(int index) {
        for (int i = 0; i < size(_controllers); i++)
            if (i == index) _controllers.get(i).onResumeNative();
            else _controllers.get(i).onPauseNative();
        invalidateOptionsMenu();
    }
}
