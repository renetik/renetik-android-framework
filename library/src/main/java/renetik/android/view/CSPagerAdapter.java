package renetik.android.view;

import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.PagerAdapter;
import renetik.android.java.collections.CSList;
import renetik.android.viewbase.CSViewController;

import static renetik.android.java.collections.CSListKt.list;

public class CSPagerAdapter<T extends CSViewController & CSPagerPage> extends PagerAdapter {

    private CSList<T> _controllers = list();

    public CSPagerAdapter(CSList<T> controllers) {
        _controllers = controllers;
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    public int getCount() {
        return _controllers.count();
    }

    public CharSequence getPageTitle(int position) {
        return _controllers.at(position).csPagerControllerTitle();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = _controllers.at(position).getView();
        container.addView(view, 0);
        return view;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
