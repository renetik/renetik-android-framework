package renetik.android.network

import renetik.android.network.data.CSServerMapData
import renetik.android.event.process.CSProcess

interface CSServerWithPing {
    fun ping(): CSProcess<CSServerMapData>
}