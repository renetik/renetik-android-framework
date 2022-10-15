package renetik.android.network.process

import renetik.android.event.common.CSHasDestruct

open class CSHttpProcess<Data : Any>(
    parent: CSHasDestruct, url: String, data: Data) : CSProcessBase<Data>(parent, data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}