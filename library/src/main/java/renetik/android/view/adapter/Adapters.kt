package renetik.android.view.adapter

import android.animation.Animator
import android.animation.Animator.AnimatorListener

open class AnimatorAdapter : AnimatorListener {
    override fun onAnimationRepeat(animator: Animator?) = Unit
    override fun onAnimationEnd(animator: Animator?) = Unit
    override fun onAnimationCancel(animator: Animator?) = Unit
    override fun onAnimationStart(animator: Animator?) = Unit
}