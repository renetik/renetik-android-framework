package renetik.android.client.request

import renetik.android.json.data.CSJsonData

open class CSServerData : CSJsonData() {
    open val success: Boolean get() = getBoolean("success") ?: false
    open val message: String? get() = getString("message")
}