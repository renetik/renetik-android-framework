package renetik.android.ui.extensions.view

import android.view.ViewPropertyAnimator
import renetik.android.core.lang.Func
import renetik.android.ui.view.adapter.CSOnAnimationEnd

fun ViewPropertyAnimator.onAnimationEnd(function: Func) = apply {
    setListener(CSOnAnimationEnd { setListener(null); function() })
}

