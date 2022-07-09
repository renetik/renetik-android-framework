package renetik.android.ui.view.touch

import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import renetik.android.core.extensions.content.dpToPixel
import renetik.android.core.CSApplication.Companion.app
import renetik.android.event.CSEvent.Companion.event
import renetik.android.core.logging.CSLog.logInfo
import renetik.android.ui.view.touch.CSSwipeDetector.CSSwipeType.*
import kotlin.math.abs

class CSSwipeDetector(
    view: View,
    val minDistance: Int = app.dpToPixel(30),
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
        logInfo("SwipeDetector onBottomToTopSwipe!")
    }

    private fun onLeftToRightSwipe() {
        eventSwipe.fire(LeftToRight)
        logInfo("SwipeDetector LeftToRightSwipe!")
    }

    private fun onRightToLeftSwipe() {
        eventSwipe.fire(RightToLeft)
        logInfo("SwipeDetector RightToLeftSwipe!")
    }

    private fun onTopToBottomSwipe() {
        eventSwipe.fire(TopToBottom)
        logInfo("SwipeDetector onTopToBottomSwipe!")
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
                    logInfo("SwipeDetector Swipe was only " + abs(deltaX) +
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
                    logInfo("Swipe was only " + abs(deltaX) + " long, need at least " + minDistance)
                    view.performClick()
                }
            }
        }
        return false
    }


}
