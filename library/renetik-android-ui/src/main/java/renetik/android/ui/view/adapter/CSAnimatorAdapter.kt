package renetik.android.ui.view.adapter

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import renetik.android.core.lang.Func

open class CSAnimatorAdapter : AnimatorListener {
    override fun onAnimationRepeat(animator: Animator?) = Unit
    override fun onAnimationEnd(animator: Animator?) = Unit
    override fun onAnimationCancel(animator: Animator?) = Unit
    override fun onAnimationStart(animator: Animator?) = Unit
}

class CSOnAnimationEnd(val onAnimationEnd: Func) : AnimatorListener {
    override fun onAnimationRepeat(animator: Animator?) = Unit
    override fun onAnimationEnd(animator: Animator?) = onAnimationEnd()
    override fun onAnimationCancel(animator: Animator?) = Unit
    override fun onAnimationStart(animator: Animator?) = Unit
}