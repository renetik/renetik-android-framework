package  renetik.android.store.json

import renetik.android.store.CSStore
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

@Suppress("unchecked_cast")
fun <T : CSStoreJsonObject> CSStore.getJsonListList(key: String, type: KClass<T>)
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