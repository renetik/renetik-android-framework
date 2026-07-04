package renetik.android.store.type

import renetik.android.core.kotlin.primitives.isFalse
import renetik.android.json.CSJson.isJsonPretty

class CSStringJsonStore(jsonString: String = "{}") : CSJsonObjectStore() {
    override val data: MutableMap<String, Any?> = mutableMapOf()

    var jsonString: String = jsonString
        set(value) {
            field = value
            loadJson(value)
        }

    init {
        loadJson(jsonString)
    }

    override fun onChanged() {
        super.onChanged()
        isOperation.isFalse {
            jsonString = toJson(isPretty = isJsonPretty)
        }
    }
}
