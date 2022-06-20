package renetik.android.network.process

import renetik.android.network.data.CSServerMapData

interface CSServerWithPing {
    fun ping(): CSProcessBase<CSServerMapData>
}