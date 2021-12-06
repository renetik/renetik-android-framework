package renetik.android.framework.json.data

import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJSONObject
import java.io.File

fun CSJsonObject.toJsonObject() = asStringMap().toJSONObject()
fun <T : CSJsonObject> T.load(data: String) = apply { load(data.parseJsonMap()!!) }
fun <T : CSJsonObject> T.load(file: File) = load(file.readText())