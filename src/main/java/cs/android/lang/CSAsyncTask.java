package cs.android.lang;

import android.os.AsyncTask;
import cs.java.callback.Run;

public abstract class CSAsyncTask<Argument, Progress, Result> extends
		AsyncTask<Argument, Progress, Result> implements Run {

	public CSAsyncTask() {
		execute((Argument) null);
	}

	@Override protected Result doInBackground(Argument... params) {
		run();
		return null;
	}
}
