package renetik.android.json

fun Map<String, Any?>.cloneJsonMap() =
    toJson(forceString = false, formatted = false).parseJsonMap()