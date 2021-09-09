package renetik.android.json.store

import android.annotation.SuppressLint
import android.content.Context
import renetik.android.framework.CSApplication.Companion.application

class CSPreferencesJsonStore(id: String, isJsonPretty: Boolean = false)
    : CSJsonStore(isJsonPretty) {
    private val preferences = application.getSharedPreferences(id, Context.MODE_PRIVATE)
    override fun loadJsonString() = preferences.getString("json", "{}")
    @SuppressLint("CommitPrefEdits")
    override fun saveJsonString(json: String) {
        preferences.edit().putString("json", json).apply()
    }
}