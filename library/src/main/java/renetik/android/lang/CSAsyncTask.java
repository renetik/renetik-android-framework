package renetik.android.lang;

import android.os.AsyncTask;
import renetik.java.callback.CSRun;

public abstract class CSAsyncTask<Argument, Progress, Result> extends
		AsyncTask<Argument, Progress, Result> implements CSRun {

	public CSAsyncTask() {
		execute((Argument) null);
	}

	@Override protected Result doInBackground(Argument... params) {
		run();
		return null;
	}
}
