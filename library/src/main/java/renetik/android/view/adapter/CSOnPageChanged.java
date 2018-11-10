package renetik.android.view.adapter;

import androidx.viewpager.widget.ViewPager.OnPageChangeListener;

import renetik.android.java.callback.CSRunWith;

import static androidx.viewpager.widget.ViewPager.SCROLL_STATE_DRAGGING;
import static renetik.android.lang.CSLang.no;

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
