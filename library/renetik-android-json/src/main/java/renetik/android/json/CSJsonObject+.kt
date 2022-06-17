package renetik.android.json

import renetik.android.core.CSApplication
import renetik.android.core.java.io.readText
import renetik.android.core.kotlin.reflect.createInstance
import java.io.File

fun CSJsonObject.toJsonObject() = data.toJSONObject()

fun <T : CSJsonObject> T.load(data: String) = apply { load(data.parseJsonMap()!!) }

fun <T : CSJsonObject> T.load(file: File) = load(file.readText())

fun <T : CSJsonObject> T.loadAsset(file: String) =
    load(CSApplication.app.assets.open(file).readText())

fun CSJsonObject.getObject(key: String) = getMap(key)?.let(::CSJsonObject)

fun <T : CSJsonObject> T.clone() =
    this::class.createInstance()!!.also { it.load(this.data) }