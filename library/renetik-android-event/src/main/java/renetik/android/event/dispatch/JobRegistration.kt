package renetik.android.event.dispatch

import kotlinx.coroutines.Job
import renetik.android.event.registration.CSRegistration

interface JobRegistration : CSRegistration {
    val job: Job?
    suspend fun cancelAndWait() {
        cancel()
        waitToFinish()
    }

    suspend fun waitToFinish() {
        job?.join()
    }
}