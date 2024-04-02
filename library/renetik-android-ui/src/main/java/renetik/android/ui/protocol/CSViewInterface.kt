package renetik.android.ui.protocol

import android.content.Context
import android.view.ContextThemeWrapper
import android.view.View
import androidx.annotation.StyleRes
import renetik.android.event.common.CSHasContext

interface CSViewInterface : CSHasContext {
    companion object {
        @StyleRes
        var themeOverride: Int? = null

        fun context(context: Context): Context {
            if ((context as? CSContextThemeWrapper)?.themeResId == themeOverride) return context
            return themeOverride?.let { CSContextThemeWrapper(context, it) } ?: context
        }

        fun context(context: CSHasContext): Context = context(context.context)
    }

    val view: View
}

class CSContextThemeWrapper(
    context: Context, @StyleRes val themeResId: Int
) : ContextThemeWrapper(context, themeResId)