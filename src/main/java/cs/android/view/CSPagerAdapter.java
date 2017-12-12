package cs.android.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import cs.android.viewbase.CSViewController;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.list;

public class CSPagerAdapter<T extends CSViewController & CSPagerPageInterface> extends PagerAdapter {

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
//        if (set(_icons)) {
//            Drawable image = rootController().getDrawable(_icons.get(position));
//            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//            SpannableString sb = new SpannableString(" " + _titles.get(position));
//            sb.setSpan(new ImageSpan(image), 0, 1, SPAN_EXCLUSIVE_EXCLUSIVE);
//            return sb;
//        }
        return _controllers.at(position).csPagerControllerTitle();
    }

    public Object instantiateItem(ViewGroup container, int position) {
        View view = _controllers.at(position).asView();
        container.addView(view, 0);
        return view;
    }

    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
