package cs.android.view;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;

import cs.android.view.adapter.CSOnQueryText;
import cs.android.viewbase.CSViewController;
import cs.android.viewbase.CSOnMenu;
import cs.java.callback.CSRunWith;

import static cs.java.lang.CSLang.*;

public class CSSearchController extends CSViewController {

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

    public CSSearchController setQueryListener(CSRunWith<String> queryListener) {
        _queryListener = queryListener;
        return this;
    }

    public void onPrepareOptionsMenu(CSOnMenu menu) {
        super.onPrepareOptionsMenu(menu);
        if (isPaused()) return;
        _searchView = (SearchView) MenuItemCompat.getActionView(menu.find(_searchMenuId));
        _searchView.setQuery(_query, NO);
        _searchView.setOnQueryTextListener(new CSOnQueryText() {
            public boolean onQueryTextChange(String query) {
                _query = query;
                if (is(_queryListener)) _queryListener.run(query);
                return false;
            }
        });
        _searchView.setIconified(!_expanded);
        _searchView.setIconifiedByDefault(!_expanded);
        if (_expanded) _searchView.requestFocusFromTouch();
    }

    public CSSearchController expanded(boolean expanded) {
        _expanded = expanded;
        return this;
    }

    public void clear() {
        CSRunWith<String> tmpListener = _queryListener;
        _queryListener = null;
        _searchView.setQuery("", NO);
        _searchView.clearFocus();
        _searchView.setIconified(true);
        _queryListener = tmpListener;
    }

    public String query() {
        return is(_searchView) ? _searchView.getQuery() + "" : "";
    }
}
