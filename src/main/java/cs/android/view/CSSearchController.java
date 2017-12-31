package cs.android.view;

import android.support.v7.widget.SearchView;

import cs.android.viewbase.menu.CSOnMenu;
import cs.android.viewbase.CSViewController;
import cs.java.callback.CSRunWith;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.run;
import static cs.java.lang.CSLang.stringify;

public class CSSearchController extends CSViewController implements SearchView.OnQueryTextListener {

    private final int _searchMenuId;
    private CSRunWith<String> _queryListener;
    private String _query;
    private SearchView _searchView;
    private boolean _expanded;

    public CSSearchController(CSViewController parent, int searchMenuId) {
        super(parent);
        _searchMenuId = searchMenuId;
        menu(_searchMenuId);
    }

    @Override
    public void onResumeNative() {
        super.onResumeNative();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public CSSearchController setQueryListener(CSRunWith<String> queryListener) {
        _queryListener = queryListener;
        return this;
    }

    public void onPrepareOptionsMenu(CSOnMenu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isPaused()) return;
//        _searchView = (SearchView) MenuItemCompat.getActionView(menu.find(_searchMenuId));
        _searchView = (SearchView) menu.find(_searchMenuId).getActionView();
        _searchView.setQuery(_query, NO);
        _searchView.setOnQueryTextListener(this);
        _searchView.setIconified(!_expanded);
        _searchView.setIconifiedByDefault(!_expanded);
        if (_expanded) _searchView.requestFocusFromTouch();
    }

    public CSSearchController expanded(boolean expanded) {
        _expanded = expanded;
        return this;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    public boolean onQueryTextChange(String query) {
        _query = query;
        run(_queryListener, query);
        return false;
    }

    public void clear() {
        CSRunWith<String> tmpListener = _queryListener;
        _queryListener = null;
        _searchView.setQuery("", NO);
        _searchView.clearFocus();
        _searchView.setIconified(true);
        _queryListener = tmpListener;
    }

    public String text() {
        return is(_searchView) ? stringify(_searchView.getQuery()) : "";
    }
}
