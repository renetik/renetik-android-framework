package cs.android.view;

import static cs.java.lang.CSLang.list;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;

public class CSPagerAdapter extends PagerAdapter {

	private CSList<View> _views = list();
	private CSList<String> _titles;

	public CSPagerAdapter(CSList<String> titles, CSViewController... views) {
		_titles = titles;
		for (CSViewController viewController : views)
			_views.add(viewController.asView());
	}

	@Override public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

	@Override public int getCount() {
		return _views.count();
	}

	public CharSequence getPageTitle(int position) {
		return _titles.at(position);
	}

	@Override public Object instantiateItem(ViewGroup container, int position) {
		View view = _views.at(position);
		container.addView(view, 0);
		return view;
	}

	@Override public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

}
