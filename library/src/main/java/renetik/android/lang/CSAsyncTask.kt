package renetik.android.lang

import android.os.AsyncTask

class CSAsyncTask<Result>(val run: () -> Result) : AsyncTask<Unit, Unit, Result>() {

    init {
        execute(Unit)
    }

    override fun doInBackground(vararg params: Unit): Result? = run()
}
