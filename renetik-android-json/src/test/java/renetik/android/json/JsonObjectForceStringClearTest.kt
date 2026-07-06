package renetik.android.json

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.CSJson.forceString
import renetik.android.json.obj.CSJsonObject

@RunWith(RobolectricTestRunner::class)
class JsonObjectForceStringClearTest {
    /**
     * You can force strings while store on to json conversion ,
     * also you can choose to have formatted text
     */
    @Test
    fun jsonObjectStoreBooleanClear() {
        forceString = true
        val stringJsonObject = CSJsonObject().apply {
            set("key1", false)
            set("key2", "value2")
            set("key3", 1.3)
        }
        assertEquals("""{"key1":"false","key2":"value2","key3":"1.3"}""", stringJsonObject.toJson())
        forceString = false
        val jsonObject = CSJsonObject().apply {
            set("key1", false)
            set("key2", "value2")
            set("key3", 1.3)
        }
        assertEquals("""{"key1":false,"key2":"value2","key3":1.3}""", jsonObject.toJson())
        assertEquals("""{"key1":"false","key2":"value2","key3":"1.3"}""",
            jsonObject.toJson(forceString = true))

        assertEquals("""{
  "key1": false,
  "key2": "value2",
  "key3": 1.3
}""", jsonObject.toJson(formatted = true))

        jsonObject.clear("key2");jsonObject.clear("key3")
        assertEquals("""{"key1":false}""", jsonObject.toJson())
    }
}
