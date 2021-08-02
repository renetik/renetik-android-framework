package renetik.android.json.store

import android.content.Context

class CSPreferencesJsonStore(id: String) : CSJsonStore() {
    private val preferences = getSharedPreferences(id, Context.MODE_PRIVATE)
    override fun loadJsonString() = preferences.getString("json", "{}")
    override fun saveJsonString(json: String) {
        preferences.edit().putString("json", json).apply()
    }
}