package renetik.android.framework.json

import renetik.android.framework.base.CSApplication
import renetik.java.io.readText
import renetik.kotlin.reflect.createInstance
import java.io.File

fun CSJsonObject.toJsonObject() = asStringMap().toJSONObject()

fun <T : CSJsonObject> T.load(data: String) = apply { load(data.parseJsonMap()!!) }

fun <T : CSJsonObject> T.load(file: File) = load(file.readText())

fun <T : CSJsonObject> T.loadAsset(file: String) =
    load(CSApplication.app.assets.open(file).readText())

fun CSJsonObject.getObject(key: String) = getMap(key)?.let(::CSJsonObject)

fun CSJsonObject.reload(data: Map<String, Any?>) = bulkSave().use {
    clear()
    load(data)
}

fun <T : CSJsonObject> T.clone() = this::class.createInstance()!!.also { it.load(this) }