package renetik.android.ui.view.touch

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import renetik.android.core.kotlin.primitives.dp
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.event.CSEvent.Companion.event
import renetik.android.ui.view.touch.CSSwipeDetector.CSSwipeType.BottomToTop
import renetik.android.ui.view.touch.CSSwipeDetector.CSSwipeType.LeftToRight
import renetik.android.ui.view.touch.CSSwipeDetector.CSSwipeType.RightToLeft
import renetik.android.ui.view.touch.CSSwipeDetector.CSSwipeType.TopToBottom
import kotlin.math.abs

class CSSwipeDetector(
    view: View,
    private val distance: Int = 30.dp,
    action: (CSSwipeType) -> Unit,
) : View.OnTouchListener {

    private var downX = 0F
    private var downY = 0F
    private var upX = 0F
    private var upY = 0F
    private val eventSwipe = event<CSSwipeType>()

    init {
        view.setOnTouchListener(this)
        eventSwipe.listen(action)
    }

    enum class CSSwipeType {
        BottomToTop, LeftToRight, RightToLeft, TopToBottom
    }

    private fun onBottomToTopSwipe() {
        eventSwipe.fire(BottomToTop)
        logDebug()
    }

    private fun onLeftToRightSwipe() {
        eventSwipe.fire(LeftToRight)
        logDebug()
    }

    private fun onRightToLeftSwipe() {
        eventSwipe.fire(RightToLeft)
        logDebug()
    }

    private fun onTopToBottomSwipe() {
        eventSwipe.fire(TopToBottom)
        logDebug()
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }

            ACTION_MOVE -> {
                upX = event.x
                upY = event.y

                val deltaX = downX - upX

                // swipe horizontal?
                if (abs(deltaX) > distance) {
                    // left or right
                    if (deltaX < 0) {
                        onLeftToRightSwipe()
                        return true
                    }
                    if (deltaX > 0) {
                        onRightToLeftSwipe()
                        return true
                    }
                } else
                    logDebug {
                        "Swipe was only ${abs(deltaX)} long, " +
                                "need at least $distance"
                    }

                val deltaY = downY - upY
                // swipe vertical?
                if (abs(deltaY) > distance) {
                    // top or down
                    if (deltaY < 0) {
                        onTopToBottomSwipe()
                        return true
                    }
                    if (deltaY > 0) {
                        onBottomToTopSwipe()
                        return true
                    }
                } else {
                    logDebug {
                        "Swipe was only ${abs(deltaX)} long, " +
                                "need at least $distance"
                    }
                    view.performClick()
                }
            }
        }
        return false
    }

}


