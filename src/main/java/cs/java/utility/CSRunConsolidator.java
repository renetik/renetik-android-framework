package cs.java.utility;

import static cs.java.lang.CSLang.doLater;
import static cs.java.lang.CSLang.list;
import cs.java.collections.CSList;
import cs.java.callback.CSRun;

public class CSRunConsolidator implements CSRun {

	private final CSList<CSRun> runnables = list();
	private final int miliseconds;
	private boolean isRunning;

	public CSRunConsolidator(int miliseconds) {
		this.miliseconds = miliseconds;
	}

	public void invoke(CSRun runnable) {
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
