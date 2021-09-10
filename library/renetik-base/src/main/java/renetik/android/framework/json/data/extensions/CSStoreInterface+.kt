package  renetik.android.framework.json.data.extensions

import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.store.CSStoreInterface
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("unchecked_cast")
fun <T : CSJsonObject> CSStoreInterface.getJsonListList(key: String, type: KClass<T>)
        : MutableList<List<T>>? {
    val playsData = getList(key) ?: return null
    val plays = mutableListOf<List<T>>()
    for (keyPlayData in playsData) {
        val keyPlay = mutableListOf<T>()
        for (playNoteData in keyPlayData as List<Map<String, Any?>>) {
            val notePlay = type.createInstance()
            notePlay.load(playNoteData)
            keyPlay.add(notePlay)
        }
        plays.add(keyPlay)
    }
    return plays
}