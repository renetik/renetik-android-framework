package cs.android.lang;

import static cs.java.lang.Lang.doLater;
import cs.android.viewbase.CSViewController;
import cs.java.callback.Run;

public abstract class DoLater implements Run {

	public DoLater() {
		doLater(this);
	}

	public DoLater(final CSViewController parent, int miliseconds) {
		doLater(miliseconds, new Run() {
			@Override public void run() {
				if (parent.isResumed()) DoLater.this.run();
			}
		});
	}

	public DoLater(final CSViewController parent) {
		this(parent, 0);
	}

	public DoLater(int miliseconds) {
		doLater(miliseconds, this);
	}

}
