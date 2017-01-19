package cs.android.view;

import cs.android.view.adapter.CSOnPageChange;
import cs.android.viewbase.CSView;
import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.*;

public class CSPagerController extends CSViewController {

    private CSList<String> _titles;
    private CSViewController[] _views;
    private CSView _emptyView;

    public CSPagerController(CSViewController parent, int pagerId, CSList<String> titles, CSViewController... views) {
        super(parent, pagerId);
        _titles = titles;
        _views = views;
        visible(set(_titles));
    }

    public CSView setEmptyView(int id) {
        return setEmptyView(new CSView<>(parent(), id));
    }

    public CSView setEmptyView(CSView view) {
        _emptyView = view;
        if (is(_emptyView)) _emptyView.visible(empty(_titles));
        return _emptyView;
    }

    public void reload(CSList<String> titles, CSViewController[] views) {
        int currentItem = asPager().getCurrentItem();
        for (CSViewController controller : _views){
            controller.onDeinitialize(null);
            controller.onDestroy();
        }
        _titles = titles;
        _views = views;
        asPager().setAdapter(new CSPagerAdapter(_titles, _views));
        for (CSViewController controller : _views) controller.onInitialize();
        updateControllersState(views.length > currentItem ? currentItem : 0);
        if (is(_emptyView)) _emptyView.visible(empty(_titles));
        visible(set(_titles));
        if (views.length > currentItem) asPager().setCurrentItem(currentItem, YES);
    }

    public void reload(CSViewController[] views) {
        reload(_titles, views);
    }

    protected void onCreate() {
        super.onCreate();
        asPager().setAdapter(new CSPagerAdapter(_titles, _views));
        asPager().addOnPageChangeListener(new CSOnPageChange() {
            public void onPageSelected(int index) {
                updateControllersState(index);
            }
        });
    }

    public void onResume() {
        super.onResume();
        updateControllersState(asPager().getCurrentItem());
    }

    private void updateControllersState(int index) {
        for (int i = 0; i < _views.length; i++)
            if (i == index) _views[i].onResumeNative();
            else _views[i].onPauseNative();
        invalidateOptionsMenu();
    }
}
