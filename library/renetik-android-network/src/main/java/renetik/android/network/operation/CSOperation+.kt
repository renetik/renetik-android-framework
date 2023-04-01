package renetik.android.network.operation

import renetik.android.core.lang.ArgFunc
import renetik.android.event.process.CSProcess

fun <Data : Any> CSOperation<Data>.onSuccess(function: ArgFunc<CSProcess<Data>>) =
    apply { eventSuccess.listen(function) }

fun <Data : Any> CSOperation<Data>.onFailed(function: ArgFunc<CSProcess<*>>) =
    apply { eventFailed.listen(function) }

fun <Data : Any> CSOperation<Data>.onDone(function: ArgFunc<CSProcess<Data>>) =
    apply { eventDone.listen(function) }