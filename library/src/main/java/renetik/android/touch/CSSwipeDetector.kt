package renetik.android.touch

import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import renetik.android.lang.CSLang.event
import renetik.android.lang.CSLang.info
import renetik.android.viewbase.CSView
import java.lang.Math.*

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

    fun onBottomToTopSwipe(v: View) {
        eventSwipe.fire(CSSwipeType.BottomToTop)
        info("SwipeDetector onBottomToTopSwipe!")
    }

    fun onLeftToRightSwipe(v: View) {
        eventSwipe.fire(CSSwipeType.LeftToRight)
        info("SwipeDetector LeftToRightSwipe!")
    }

    fun onRightToLeftSwipe(v: View) {
        eventSwipe.fire(CSSwipeType.RightToLeft)
        info("SwipeDetector RightToLeftSwipe!")
    }

    fun onTopToBottomSwipe(v: View) {
        eventSwipe.fire(CSSwipeType.TopToBottom)
        info("SwipeDetector onTopToBottomSwipe!")
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
                        onLeftToRightSwipe(view)
                        return true
                    }
                    if (deltaX > 0) {
                        onRightToLeftSwipe(view)
                        return true
                    }
                } else
                    info("SwipeDetector Swipe was only " + abs(deltaX) + " long, need at least "
                            + MIN_DISTANCE)

                // swipe vertical?
                if (abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        onTopToBottomSwipe(view)
                        return true
                    }
                    if (deltaY > 0) {
                        onBottomToTopSwipe(view)
                        return true
                    }
                } else {
                    info("Swipe was only " + abs(deltaX) + " long, need at least " + MIN_DISTANCE)
                    view.performClick()
                }
            }
        }
        return false
    }


}
