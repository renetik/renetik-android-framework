package cs.android.viewbase;

import android.view.KeyEvent;
import cs.java.lang.CSValue;

public class CSOnKeyDownResult {

	public final int _keyCode;
	public final KeyEvent _event;
	public final CSValue<Boolean> _return;

	public CSOnKeyDownResult(int keyCode, KeyEvent event) {
		_keyCode = keyCode;
		_event = event;
		_return = new CSValue<Boolean>(false);
	}
}