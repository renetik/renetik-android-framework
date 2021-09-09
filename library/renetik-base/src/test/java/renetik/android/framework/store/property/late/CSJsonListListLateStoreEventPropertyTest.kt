package renetik.android.framework.store.property.late

import org.junit.Assert.assertEquals
import org.junit.Test
import renetik.android.framework.json.data.CSJsonMapStore
import renetik.android.framework.json.data.load
import renetik.android.framework.json.toJsonString

class TestData() : CSJsonMapStore() {
    val title = lateStringProperty("title")

    constructor(title: String) : this() {
        this.title.value(title)
    }
}

class CSJsonListListLateStoreEventPropertyTest {

    private val store = CSJsonMapStore()

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

        val json = store.toJsonString(true)

        val store2 = CSJsonMapStore()
        val property2 = CSJsonListListLateStoreEventProperty(store2, "property", TestData::class)
        store2.load(json)

        assertEquals(property2.value[1][1].title.value, "title22")
    }
}

