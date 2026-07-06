package renetik.android.store.property

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.event.lifecycle.CSModel
import renetik.android.json.CSJson.forceString
import renetik.android.json.toJson
import renetik.android.store.CSStore
import renetik.android.store.SimpleJsonObjectStore
import renetik.android.store.TestIdItem
import renetik.android.store.TestIdItem.Companion.TestIdItems
import renetik.android.store.TestIdItem.Fourth
import renetik.android.store.TestIdItem.Second
import renetik.android.store.dataLateJsonProperty
import renetik.android.store.dataLateStringProperty
import renetik.android.store.lateBoolProperty
import renetik.android.store.lateIntProperty
import renetik.android.store.lateJsonListListProperty
import renetik.android.store.lateJsonListProperty
import renetik.android.store.lateListItemProperty
import renetik.android.store.reload
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.testing.CSAssert.assertThrows

@RunWith(RobolectricTestRunner::class)
class LateStorePropertyTest {
    private val store: CSStore = CSJsonObjectStore()

    @Test
    fun testStringProperty() {
        var eventCount = 0
        var newValue: String? = null
        var value: String by store.dataLateStringProperty("key") {
            newValue = it
            eventCount += 1
        }
        value = "value 1"

        assertEquals("""{"key":"value 1"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"value 2"}""")
        assertEquals(newValue, "value 2")
        assertEquals(2, eventCount)
    }

    @Test
    fun testBooleanProperty() {
        val parent = CSModel()
        var newValue: Boolean? = null
        var value: Boolean by store.lateBoolProperty("key") { newValue = it }
            .listenLoad(parent)
        value = false

        assertEquals("""{"key":false}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":true}""")
        assertTrue(newValue!!)
    }

    @Test
    fun testIntProperty() {
        val parent = CSModel()
        forceString = true
        var newValue: Int? = null
        var value: Int by store.lateIntProperty("key") { newValue = it }
            .listenLoad(parent)
        value = 34

        assertEquals("""{"key":"34"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"56"}""")
        assertEquals(56, newValue)
        forceString = false
    }

    @Test
    fun testListItemValueProperty() {
        val parent = CSModel()
        var eventCount = 0
        var newValue: TestIdItem? = null
        var value: TestIdItem by store.lateListItemProperty("key", TestIdItems) {
            newValue = it
            eventCount += 1
        }.listenLoad(parent)
        value = Fourth

        assertEquals("""{"key":"id4"}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":"id2"}""")
        assertEquals(newValue, Second)
        assertEquals(2, eventCount)
    }

    @Test
    fun testJsonProperty() {
        var newValue: SimpleJsonObjectStore? = null
        var value: SimpleJsonObjectStore by store.dataLateJsonProperty("key") {
            newValue = it
        }
        value = SimpleJsonObjectStore()

        assertEquals("""{"key":{}}""", store.toJson())
        assertEquals(newValue, value)

        value.string = "string 2"
        assertEquals("""{"key":{"stringId":"string 2"}}""", store.toJson())

        store.reload("""{"key":{"stringId":"string 1"}}""")
        assertEquals(newValue, SimpleJsonObjectStore().apply { string = "string 1" })
    }

    @Test
    fun testLateJsonListProperty() {
        val parent = CSModel()
        var newValue: List<SimpleJsonObjectStore>? = null
        var value: List<SimpleJsonObjectStore> by store.lateJsonListProperty("key") {
            newValue = it
        }.listenLoad(parent)
        value = listOf(
            SimpleJsonObjectStore(), SimpleJsonObjectStore(lateString = "string 1")
        )

        assertEquals("""{"key":[{},{"lateStringId":"string 1"}]}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":[{"nullStringId":"string 2"},{}]}""")
        assertEquals(
            newValue,
            listOf(
                SimpleJsonObjectStore(nullString = "string 2"), SimpleJsonObjectStore()
            )
        )
    }

    @Test
    fun testLateJsonListListProperty() {
        var newValue: List<List<SimpleJsonObjectStore>>? = null
        var value: List<List<SimpleJsonObjectStore>> by store.lateJsonListListProperty(
            "key", onChange = { newValue = it }
        ).listenLoadOnce()
        assertThrows { value.last() }
        value =
            listOf(
                listOf(SimpleJsonObjectStore()), listOf(),
                listOf(SimpleJsonObjectStore("test"))
            )

        assertEquals("""{"key":[[{}],[],[{"stringId":"test"}]]}""", store.toJson())
        assertEquals(newValue, value)

        store.reload("""{"key":[[{"stringId":"test 2"}],[]]}""")
        assertEquals("test 2", value.first().first().string)
    }
}

