package cs.android.view.adapter;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import cs.android.viewbase.CSView;

public class OnPageChange implements OnPageChangeListener {

    public OnPageChange() {
    }

    public OnPageChange(ViewPager viewPager) {
        viewPager.setOnPageChangeListener(this);
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageSelected(int arg0) {
    }

}
