package renetik.android.network

import renetik.android.network.data.CSServerMapData
import renetik.android.network.process.CSProcess

interface CSServerWithPing {
    fun ping(): CSProcess<CSServerMapData>
}