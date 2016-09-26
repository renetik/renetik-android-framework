package cs.android.view.adapter;

import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;

public class OnScroll implements OnScrollListener {

	@Override public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		onScroll();
	}

	@Override public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	protected void onScroll() {
	}

}
