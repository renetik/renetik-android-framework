package renetik.android.ui.view.touch

import android.view.MotionEvent
import android.view.MotionEvent.ACTION_CANCEL
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.MotionEvent.ACTION_POINTER_DOWN
import android.view.MotionEvent.ACTION_POINTER_UP
import android.view.MotionEvent.ACTION_UP
import android.view.View
import renetik.android.core.logging.CSLog.logDebug
import renetik.android.ui.view.touch.CSTwoFingerSwipeDetector.CSSwipeType.BottomToTop
import renetik.android.ui.view.touch.CSTwoFingerSwipeDetector.CSSwipeType.LeftToRight
import renetik.android.ui.view.touch.CSTwoFingerSwipeDetector.CSSwipeType.RightToLeft
import renetik.android.ui.view.touch.CSTwoFingerSwipeDetector.CSSwipeType.TopToBottom
import kotlin.math.abs

class CSTwoFingerSwipeDetector(
    view: View,
    private val tracked: List<CSSwipeType> = CSSwipeType.entries,
    private val distance: Float = 30f,
    private val action: (CSSwipeType) -> Unit,
) : View.OnTouchListener {


    private var pointerId1 = -1
    private var pointerId2 = -1
    private var startMidX = 0f
    private var startMidY = 0f

    init {
        view.setOnTouchListener(this)
    }

    enum class CSSwipeType {
        BottomToTop, LeftToRight, RightToLeft, TopToBottom
    }

    private fun notifyIfTracked(type: CSSwipeType) {
        if (type in tracked) {
            action(type)
            logDebug()
        }
    }

    private fun resetTwoFinger() {
        pointerId1 = -1
        pointerId2 = -1
        startMidX = 0f
        startMidY = 0f
    }

    override fun onTouch(view: View, event: MotionEvent): Boolean {
        when (event.actionMasked) {
            ACTION_DOWN -> {
                resetTwoFinger()
                return true
            }

            ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    pointerId1 = event.getPointerId(0)
                    pointerId2 = event.getPointerId(1)

                    val x1 = event.getX(0)
                    val y1 = event.getY(0)
                    val x2 = event.getX(1)
                    val y2 = event.getY(1)

                    startMidX = (x1 + x2) / 2f
                    startMidY = (y1 + y2) / 2f
                }
                return true
            }

            ACTION_MOVE -> {
                if (pointerId1 != -1 && pointerId2 != -1 && event.pointerCount >= 2) {
                    val pointerIndex1 = event.findPointerIndex(pointerId1)
                    val pointerIndex2 = event.findPointerIndex(pointerId2)

                    if (pointerIndex1 == -1 || pointerIndex2 == -1) {
                        resetTwoFinger()
                        return false
                    }

                    val midX = (event.getX(pointerIndex1) + event.getX(pointerIndex2)) / 2f
                    val midY = (event.getY(pointerIndex1) + event.getY(pointerIndex2)) / 2f

                    val distanceX = startMidX - midX
                    val distanceY = startMidY - midY

                    if (abs(distanceX) > distance) {
                        if (distanceX < 0) notifyIfTracked(LeftToRight)
                        else notifyIfTracked(RightToLeft)
                        resetTwoFinger()
                        return true
                    }

                    if (abs(distanceY) > distance) {
                        if (distanceY < 0) notifyIfTracked(TopToBottom)
                        else notifyIfTracked(BottomToTop)
                        resetTwoFinger()
                        return true
                    }
                }
                return true
            }

            ACTION_POINTER_UP, ACTION_UP, ACTION_CANCEL -> resetTwoFinger()
        }
        return false
    }
}
