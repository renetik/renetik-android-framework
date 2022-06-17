package renetik.android.view

import android.view.ViewPropertyAnimator
import renetik.android.core.lang.Func
import renetik.android.framework.view.adapter.CSOnAnimationEnd

fun ViewPropertyAnimator.onAnimationEnd(function: Func) = apply {
    setListener(CSOnAnimationEnd { setListener(null); function() })
}

