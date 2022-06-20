package renetik.android.network.process

fun <Data : Any, Process : CSProcessBase<Data>>
        Process.connect(process: CSProcessBase<Data>) = apply {
    onSuccess { process.success(it.data!!) }.onFailed { process.failed(it) }
}