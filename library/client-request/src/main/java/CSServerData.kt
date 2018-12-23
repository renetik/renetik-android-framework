package renetik.android.client.request

import renetik.android.json.data.CSJsonData

abstract class CSServerData : CSJsonData() {
    abstract val success: Boolean
    abstract val message: String?

}