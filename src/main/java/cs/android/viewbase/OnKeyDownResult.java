package cs.android.viewbase;

import android.view.KeyEvent;
import cs.java.lang.Value;

public class OnKeyDownResult {

	public final int _keyCode;
	public final KeyEvent _event;
	public final Value<Boolean> _return;

	public OnKeyDownResult(int keyCode, KeyEvent event) {
		_keyCode = keyCode;
		_event = event;
		_return = new Value<Boolean>(false);
	}
}