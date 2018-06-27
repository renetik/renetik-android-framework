package cs.android.view;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;

import cs.android.viewbase.CSLayoutId;
import cs.android.viewbase.CSViewController;
import cs.android.viewbase.menu.CSOnMenu;
import cs.java.callback.CSRunWith;

import static android.support.v7.widget.SearchView.OnQueryTextListener;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.YES;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.run;
import static cs.java.lang.CSLang.stringify;

public class CSSearchController extends CSViewController {

    private int _searchMenuId;
    private CSRunWith<String> _queryListener;
    private String _query;
    private SearchView _menuSearchView;
    private SearchView _searchView;
    private boolean _searchOpened;
    private boolean _expanded;

    public CSSearchController(CSViewController parent, int searchMenuId) {
        super(parent);
        _searchMenuId = searchMenuId;
        menu(_searchMenuId);
    }

    public CSSearchController(CSViewController parent, CSLayoutId searchViewLayout) {
        super(parent, searchViewLayout);
        _searchView = (SearchView) asView();
        _searchView.setQuery(_query, NO);
        _searchView.setOnQueryTextListener(new OnQueryTextListener() {
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String query) {
                return CSSearchController.this.onQueryTextChange(query);
            }
        });
        _searchView.setOnSearchClickListener(v -> onSearchClick());
        _searchView.setOnCloseListener(() -> onClose());
        _searchView.setIconifiedByDefault(!_expanded);
        _searchView.setIconified(!_expanded);
        _searchView.requestFocusFromTouch();
    }

    private boolean onQueryTextChange(String query) {
        _query = query;
        run(_queryListener, query);
        return false;
    }

    public CSSearchController setQueryListener(CSRunWith<String> queryListener) {
        _queryListener = queryListener;
        return this;
    }

    public void onPrepareOptionsMenu(CSOnMenu menu) {
        super.onPrepareOptionsMenu(menu);
        if (!isShowing() || is(_searchView)) return;
        _menuSearchView = (SearchView) MenuItemCompat.getActionView(menu.find(_searchMenuId));
        _menuSearchView.setQuery(_query, NO);
        _menuSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            public boolean onQueryTextChange(String query) {
                return CSSearchController.this.onQueryTextChange(query);
            }
        });
        _menuSearchView.setOnSearchClickListener(v -> onSearchClick());
        _menuSearchView.setOnCloseListener(() -> onClose());
        _menuSearchView.setIconifiedByDefault(!_expanded);
        _menuSearchView.setIconified(!_expanded);
        _menuSearchView.requestFocusFromTouch();
    }

    private boolean onClose() {
        _searchOpened = NO;
        return false;
    }

    private void onSearchClick() {
        _searchOpened = YES;
    }

    public CSSearchController expanded(boolean expanded) {
        _expanded = expanded;
        return this;
    }

    public void clear() {
        CSRunWith<String> tmpListener = _queryListener;
        _queryListener = null;
        _menuSearchView.setQuery("", NO);
        _menuSearchView.clearFocus();
        _menuSearchView.setIconified(true);
        _queryListener = tmpListener;
    }

    public String text() {
        return is(_menuSearchView) ? stringify(_menuSearchView.getQuery())
                : is(_searchView) ? stringify(_searchView.getQuery())
                : "";
    }
}
