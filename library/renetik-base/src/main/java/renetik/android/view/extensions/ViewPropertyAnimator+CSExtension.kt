package renetik.android.view.extensions

import android.view.ViewPropertyAnimator
import renetik.android.java.Func
import renetik.android.view.adapter.CSOnAnimationEnd

fun ViewPropertyAnimator.onAnimationEnd(function: Func) = apply {
    setListener(CSOnAnimationEnd { setListener(null); function() })
}

