package renetik.android.ui.extensions.view

import android.view.ViewPropertyAnimator
import renetik.android.core.lang.Fun
import renetik.android.ui.view.adapter.CSOnAnimationEnd

fun ViewPropertyAnimator.onAnimationEnd(function: Fun) = apply {
    setListener(CSOnAnimationEnd { setListener(null); function() })
}

