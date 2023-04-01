package renetik.android.network

import renetik.android.event.common.CSHasDestruct
import renetik.android.network.process.CSProcess

open class CSHttpProcess<Data : Any>(
    parent: CSHasDestruct, url: String, data: Data) : CSProcess<Data>(parent, data) {

    var url: String? = url

    override fun toString(): String {
        return super.toString() + url
    }
}