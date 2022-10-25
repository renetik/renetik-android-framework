package renetik.android.network.process

fun <Data : Any, Process : CSProcess<Data>>
        Process.connect(process: CSProcess<Data>) = apply {
    onSuccess { process.success(it.data!!) }.onFailed { process.failed(it) }
}