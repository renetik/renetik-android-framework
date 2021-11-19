package renetik.android.framework.view.touch

import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import renetik.android.content.dpToPixel
import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.event.event
import renetik.android.framework.event.listen
import renetik.android.framework.logging.CSLog.info
import renetik.android.framework.view.touch.CSSwipeDetector.CSSwipeType.*
import kotlin.math.abs

class CSSwipeDetector(
    view: View,
    val minDistance: Int = application.dpToPixel(30),
    function: (CSSwipeType) -> Unit) : View.OnTouchListener {

    private var downX = 0F
    private var downY = 0F
    private var upX = 0F
    private var upY = 0F
    private val eventSwipe = event<CSSwipeType>()

    init {
        view.setOnTouchListener(this)
        eventSwipe.listen(function)
    }

    enum class CSSwipeType {
        BottomToTop, LeftToRight, RightToLeft, TopToBottom
    }

    private fun onBottomToTopSwipe() {
        eventSwipe.fire(BottomToTop)
        info("SwipeDetector onBottomToTopSwipe!")
    }

    private fun onLeftToRightSwipe() {
        eventSwipe.fire(LeftToRight)
        info("SwipeDetector LeftToRightSwipe!")
    }

    private fun onRightToLeftSwipe() {
        eventSwipe.fire(RightToLeft)
        info("SwipeDetector RightToLeftSwipe!")
    }

    private fun onTopToBottomSwipe() {
        eventSwipe.fire(TopToBottom)
        info("SwipeDetector onTopToBottomSwipe!")
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
                val deltaY = downY - upY

                // swipe horizontal?
                if (abs(deltaX) > minDistance) {
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
                    info("SwipeDetector Swipe was only " + abs(deltaX) +
                            " long, need at least " + minDistance)

                // swipe vertical?
                if (abs(deltaY) > minDistance) {
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
                    info("Swipe was only " + abs(deltaX) + " long, need at least " + minDistance)
                    view.performClick()
                }
            }
        }
        return false
    }


}
