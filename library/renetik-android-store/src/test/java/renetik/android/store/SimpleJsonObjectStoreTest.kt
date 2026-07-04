package renetik.android.store

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotSame
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.obj.clone
import renetik.android.json.obj.load
import renetik.android.json.toJson
import renetik.android.store.extensions.reload

@RunWith(RobolectricTestRunner::class)
class SimpleJsonObjectStoreTest {
    @Test
    fun jsonTypeTest() {
        val instance = SimpleJsonObjectStore()
        assertEquals("""{}""", instance.toJson())
        instance.string = "string"
        assertEquals("""{"stringId":"string"}""", instance.toJson())
        instance.lateString = "lateString"
        assertEquals("""{"lateStringId":"lateString","stringId":"string"}""", instance.toJson())
        instance.nullString = "nullString"
        assertEquals(
            """{"lateStringId":"lateString","nullStringId":"nullString","stringId":"string"}""",
            instance.toJson())
        instance.nullString = null
        assertEquals("""{"lateStringId":"lateString","stringId":"string"}""", instance.toJson())
    }

    @Test
    fun reloadJsonTypeTest() {
        val instance = SimpleJsonObjectStore()
        assertEquals("""{}""", instance.toJson())
        instance.string = "string 2"
        assertEquals("""{"stringId":"string 2"}""", instance.toJson())
        instance.reload(
            """{"stringId":"string 3","lateStringId":"lateString","nullStringId":"nullString"}""")
        assertEquals("string 3", instance.string)
        assertEquals("lateString", instance.lateString)
    }

    @Test
    fun loadJsonTypeTest() {
        val instance = SimpleJsonObjectStore()
        assertEquals("""{}""", instance.toJson())
        instance.string = "string 2"
        assertEquals("""{"stringId":"string 2"}""", instance.toJson())
        instance.load("""{"lateStringId":"lateString"}""")
        assertEquals("string 2", instance.string)
        assertEquals(null, instance.nullString)
        assertEquals("lateString", instance.lateString)
    }

    @Test
    fun cloneJsonTypeTest() {
        val instance = SimpleJsonObjectStore("string 2", "nullString", "lateString")
        assertEquals("lateString", instance.lateString)
        val instance2 = instance.clone()
        assertNotSame(instance, instance2)
        assertEquals("lateString", instance2.lateString)
    }
}