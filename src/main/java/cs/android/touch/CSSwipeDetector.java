package cs.android.touch;

import static cs.java.lang.CSLang.event;
import static cs.java.lang.CSLang.fire;
import static cs.java.lang.CSLang.info;
import android.view.MotionEvent;
import android.view.View;

import cs.android.viewbase.CSView;
import cs.java.event.CSEvent;

public class CSSwipeDetector implements View.OnTouchListener {

	public CSSwipeDetector(CSView<?> widget) {
		widget.getView().setOnTouchListener(this);
	}

	public enum CSSwipeType {
		BottomToTop, LeftToRight, RightToLeft, TopToBottom,

	}
	static final int MIN_DISTANCE = 70;
	private float downX, downY, upX, upY;

	private final CSEvent<CSSwipeType> onSwipe = event();

	public CSSwipeDetector() {
	}

	public CSEvent<CSSwipeType> getOnSwipe() {
		return onSwipe;
	}

	public void onBottomToTopSwipe(View v) {
		fire(onSwipe, CSSwipeType.BottomToTop);
		info("SwipeDetector onBottomToTopSwipe!");
	}

	public void onLeftToRightSwipe(View v) {
		fire(onSwipe, CSSwipeType.LeftToRight);
		info("SwipeDetector LeftToRightSwipe!");
	}

	public void onRightToLeftSwipe(View v) {
		fire(onSwipe, CSSwipeType.RightToLeft);
		info("SwipeDetector RightToLeftSwipe!");
	}

	public void onTopToBottomSwipe(View v) {
		fire(onSwipe, CSSwipeType.TopToBottom);
		info("SwipeDetector onTopToBottomSwipe!");
	}

	@Override public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			downX = event.getX();
			downY = event.getY();
			return true;
		}
		case MotionEvent.ACTION_UP: {
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
			if (Math.abs(deltaX) > MIN_DISTANCE) {
				// left or right
				if (deltaX < 0) {
					onLeftToRightSwipe(v);
					return true;
				}
				if (deltaX > 0) {
					onRightToLeftSwipe(v);
					return true;
				}
			} else info("SwipeDetector Swipe was only " + Math.abs(deltaX) + " long, need at least "
					+ MIN_DISTANCE);

			// swipe vertical?
			if (Math.abs(deltaY) > MIN_DISTANCE) {
				// top or down
				if (deltaY < 0) {
					onTopToBottomSwipe(v);
					return true;
				}
				if (deltaY > 0) {
					onBottomToTopSwipe(v);
					return true;
				}
			} else {
				info("Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
				v.performClick();
			}
		}
		}
		return false;
	}

}
