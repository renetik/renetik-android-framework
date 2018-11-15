package renetik.android.view.adapter

import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class CSOnPageChange(private val onPageSelect: (Int) -> Unit) : OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) = onPageSelect(position)

}
