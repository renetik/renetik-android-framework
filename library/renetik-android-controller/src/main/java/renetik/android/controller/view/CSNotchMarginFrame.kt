package renetik.android.controller.view
//
//import android.content.Context
//import android.util.AttributeSet
//import android.view.View
//import android.view.ViewGroup
//import android.widget.FrameLayout
//import renetik.android.ui.extensions.widget.layoutMatch
//
//class CSNotchMarginFrame @JvmOverloads constructor(
//    context: Context, attrs: AttributeSet? = null,
//    defStyleAttr: Int = 0
//) : FrameLayout(context, attrs, defStyleAttr) {
//
//    private val innerFrame = FrameLayout(context)
//
//    init {
//        val params = layoutMatch
////        params.leftMargin = 100
//        addView(innerFrame, params)
//    }
//
//    override fun addView(child: View?) =
//        if (child == innerFrame) super.addView(child)
//        else innerFrame.addView(child)
//
//    override fun addView(child: View?, params: ViewGroup.LayoutParams?) =
//        if (child == innerFrame) super.addView(child, params)
//        else innerFrame.addView(child, params)
//
//    override fun addView(child: View?, index: Int) =
//        if (child == innerFrame) super.addView(child, index)
//        else innerFrame.addView(child, index)
//
//    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) =
//        if (child == innerFrame) super.addView(child, index, params)
//        else innerFrame.addView(child, index, params)
//
//    override fun addView(child: View?, width: Int, height: Int) =
//        if (child == innerFrame) super.addView(child, width, height)
//        else innerFrame.addView(child, width, height)
//}