package renetik.android.view.adapter

import androidx.viewpager.widget.ViewPager.OnPageChangeListener

class CSOnPageChanged(private val function: (Int) -> Unit) : OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) = Unit

    override fun onPageScrolled(position: Int, offset: Float, offsetPixels: Int) = Unit

    override fun onPageSelected(position: Int) = function(position)

}
