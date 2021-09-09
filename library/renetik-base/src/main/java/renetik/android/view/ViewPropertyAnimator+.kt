package renetik.android.view.extensions

import android.view.ViewPropertyAnimator
import renetik.android.framework.Func
import renetik.android.framework.view.adapter.CSOnAnimationEnd

fun ViewPropertyAnimator.onAnimationEnd(function: Func) = apply {
    setListener(CSOnAnimationEnd { setListener(null); function() })
}

