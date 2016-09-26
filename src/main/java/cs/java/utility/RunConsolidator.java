package cs.java.utility;

import static cs.java.lang.Lang.doLater;
import static cs.java.lang.Lang.list;
import cs.java.collections.CSList;
import cs.java.callback.Run;

public class RunConsolidator implements Run {

	private final CSList<Run> runnables = list();
	private final int miliseconds;
	private boolean isRunning;

	public RunConsolidator(int miliseconds) {
		this.miliseconds = miliseconds;
	}

	public void invoke(Run runnable) {
		if (isRunning)
			runnables.add(runnable);
		else {
			runnable.run();
			isRunning = true;
			doLater(miliseconds, this);
		}
	}

	@Override
	public void run() {
		if (runnables.hasItems()) {
			runnables.removeLast().run();
			doLater(miliseconds, this);
		} else isRunning = false;
	}

}
