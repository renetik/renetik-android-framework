package renetik.android.framework.json.data

import renetik.android.framework.CSApplication.Companion.application
import renetik.android.framework.json.parseJsonMap
import renetik.android.framework.json.toJSONObject
import renetik.java.io.readText
import java.io.File

fun CSJsonObject.toJsonObject() = asStringMap().toJSONObject()
fun <T : CSJsonObject> T.load(data: String) = apply { load(data.parseJsonMap()!!) }
fun <T : CSJsonObject> T.load(file: File) = load(file.readText())
fun <T : CSJsonObject> T.loadAsset(file: String) =
    load(application.assets.open(file).readText())