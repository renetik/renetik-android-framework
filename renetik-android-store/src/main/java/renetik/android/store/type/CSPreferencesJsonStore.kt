package renetik.android.store.type

import android.content.Context
import android.content.Context.MODE_PRIVATE
import renetik.android.core.kotlin.primitives.isFalse

class CSPreferencesJsonStore(
    context: Context,
    val key: String = "store",
    id: String = "default",
    private val isPretty: Boolean = false
) : CSJsonObjectStore() {
    override val data: MutableMap<String, Any?> = mutableMapOf()
    internal val preferences = context.getSharedPreferences(id, MODE_PRIVATE)

    init {
        preferences.getString(key, "{}")?.also(::loadJson)
    }

    override fun onChanged() {
        super.onChanged()
        isOperation.isFalse {
            preferences.edit().putString(key, toJson(isPretty)).apply()
        }
    }
}