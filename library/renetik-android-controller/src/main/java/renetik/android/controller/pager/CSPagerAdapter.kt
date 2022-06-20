package renetik.android.controller.pager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import renetik.android.controller.base.CSActivityView
import renetik.android.core.kotlin.collections.list

class CSPagerAdapter<PagerPageType>(val controllers: List<PagerPageType> = list())
    : PagerAdapter() where PagerPageType : CSActivityView<*>, PagerPageType : CSPagerPage {

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) =
        container.removeView(`object` as View)

    override fun getCount() = controllers.size

    override fun getPageTitle(position: Int) = controllers[position].pagerPageTitle

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = controllers[position].view
        container.addView(view, 0)
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any) = view === `object`

}