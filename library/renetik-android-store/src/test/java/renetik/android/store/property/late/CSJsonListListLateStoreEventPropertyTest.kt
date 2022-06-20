package renetik.android.store.property.late

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.json.load
import renetik.android.store.json.CSStoreJsonObject
import renetik.android.json.toJsonString

class TestData() : CSStoreJsonObject() {
    val title = lateStringProperty("title")

    constructor(title: String) : this() {
        this.title.value(title)
    }
}

@RunWith(RobolectricTestRunner::class)
class CSJsonListListLateStoreEventPropertyTest {

    private val store = CSStoreJsonObject()

    @Test
    fun `Value set get`() {
        val property = CSJsonListListLateStoreEventProperty(store, "property", TestData::class)
        property.value = listOf(
            listOf(TestData("title11"), TestData("title12")),
            listOf(TestData("title21"), TestData("title22")),
            listOf(TestData("title31"), TestData("title32")))

        assertEquals(property.value[1][1].title.value, "title22")
    }

    @Test
    fun `Save Load`() {
        val property = CSJsonListListLateStoreEventProperty(store, "property", TestData::class)
        property.value = listOf(
            listOf(TestData("title11"), TestData("title12")),
            listOf(TestData("title21"), TestData("title22")),
            listOf(TestData("title31"), TestData("title32")))

        val json = store.toJsonString(formatted = true)

        val store2 = CSStoreJsonObject()
        val property2 = CSJsonListListLateStoreEventProperty(store2, "property", TestData::class)
        store2.load(json)

        assertEquals(property2.value[1][1].title.value, "title22")
    }
}

