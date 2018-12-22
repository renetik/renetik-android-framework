package renetik.android.controller.pager

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import renetik.android.controller.base.CSViewController
import renetik.android.java.collections.CSList
import renetik.android.java.collections.list

class CSPagerAdapter<PagerPageType>(val controllers: CSList<PagerPageType> = list<PagerPageType>())
    : PagerAdapter() where PagerPageType : CSViewController<*>, PagerPageType : CSPagerPage {

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
