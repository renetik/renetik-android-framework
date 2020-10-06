package renetik.android.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import info.degois.damien.android.CustomFontHtml.CaseInsensitiveAssetFontLoader
import info.degois.damien.android.CustomFontHtml.CustomHtml
import renetik.android.R

class CSHtmlTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setHtml(text)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        setHtml(text)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setHtml(html: CharSequence) {
        val fontLoader = CaseInsensitiveAssetFontLoader(context, "fonts")
        text = CustomHtml.fromHtml(html.toString(), fontLoader, {
            context.getDrawable(R.drawable.outline_add_24)
        }, null)
    }
}