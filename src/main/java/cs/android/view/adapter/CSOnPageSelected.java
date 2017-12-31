package cs.android.view.adapter;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import cs.java.callback.CSRunWith;

public class CSOnPageSelected implements OnPageChangeListener {

    private CSRunWith<Integer> _onPageSelected;

    public CSOnPageSelected(CSRunWith<Integer> onPageSelected) {
        _onPageSelected = onPageSelected;
    }

    public void onPageScrollStateChanged(int arg0) {
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    public void onPageSelected(int position) {
        _onPageSelected.run(position);
    }

}
