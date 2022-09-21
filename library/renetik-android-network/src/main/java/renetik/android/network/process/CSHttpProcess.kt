package renetik.android.network.process

import renetik.android.event.common.CSHasDestroy

open class CSHttpProcess<Data : Any>(
    parent: CSHasDestroy, url: String, data: Data) : CSProcessBase<Data>(parent, data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}