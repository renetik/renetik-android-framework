package cs.android.view.adapter;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import cs.java.callback.CSRunWith;

import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static cs.java.lang.CSLang.no;

public class CSOnPageChanged implements OnPageChangeListener {

    private CSRunWith<Integer> _onPageSelected;
    private int _state;

    public CSOnPageChanged(CSRunWith<Integer> onPageSelected) {
        _onPageSelected = onPageSelected;
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int position, float offset, int offsetPixels) {
    }

    public void onPageSelected(int position) {
        _onPageSelected.run(position);
    }

}
