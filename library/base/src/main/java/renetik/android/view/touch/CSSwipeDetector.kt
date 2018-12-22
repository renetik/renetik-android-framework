package renetik.android.view.touch

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_UP
import android.view.View
import renetik.android.base.CSView
import renetik.android.logging.CSLog.logInfo
import renetik.android.java.event.event
import java.lang.Math.abs

class CSSwipeDetector() : View.OnTouchListener {

    val MIN_DISTANCE = 70
    private var downX = 0F
    private var downY = 0F
    private var upX = 0F
    private var upY = 0F

    val eventSwipe = event<CSSwipeType>()

    constructor(widget: CSView<*>) : this() {
        widget.view.setOnTouchListener(this)
    }

    enum class CSSwipeType {
        BottomToTop, LeftToRight, RightToLeft, TopToBottom
    }

    fun onBottomToTopSwipe() {
        eventSwipe.fire(CSSwipeType.BottomToTop)
        logInfo("SwipeDetector onBottomToTopSwipe!")
    }

    fun onLeftToRightSwipe() {
        eventSwipe.fire(CSSwipeType.LeftToRight)
        logInfo("SwipeDetector LeftToRightSwipe!")
    }

    fun onRightToLeftSwipe() {
        eventSwipe.fire(CSSwipeType.RightToLeft)
        logInfo("SwipeDetector RightToLeftSwipe!")
    }

    fun onTopToBottomSwipe() {
        eventSwipe.fire(CSSwipeType.TopToBottom)
        logInfo("SwipeDetector onTopToBottomSwipe!")
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN -> {
                downX = event.x
                downY = event.y
                return true
            }
            ACTION_UP -> {
                upX = event.x
                upY = event.y

                val deltaX = downX - upX
                val deltaY = downY - upY

                // swipe horizontal?
                if (abs(deltaX) > MIN_DISTANCE) {
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
                    logInfo("SwipeDetector Swipe was only " + abs(deltaX) + " long, need at least "
                            + MIN_DISTANCE)

                // swipe vertical?
                if (abs(deltaY) > MIN_DISTANCE) {
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
                    logInfo("Swipe was only " + abs(deltaX) + " long, need at least " + MIN_DISTANCE)
                    view.performClick()
                }
            }
        }
        return false
    }


}
