package renetik.android.preset.property.nullable

import junit.framework.Assert.assertEquals
import org.junit.Test
import renetik.android.event.owner.CSEventOwnerHasDestroyBase
import renetik.android.preset.CSPreset
import renetik.android.preset.propertyNullInt
import renetik.android.store.CSStore
import renetik.android.store.json.CSStoreJsonObject

class CSIntNullablePresetEventPropertyTest {

    private val store = CSStoreJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test() {
        assertEquals(1, parent.property1.value)
        parent.property1.value = 2
        assertEquals(2, parent.property1.value)
        parent.property1.value = null
        assertEquals(null, parent.property1.value)
        parent.parentPreset.reload()
        assertEquals(1, parent.property1.value)
    }

    @Test
    fun test2() {
        parent.property1.value = null
        parent.property2.value = 3
        assertEquals(null, parent.property1.value)
        assertEquals(3, parent.property2.value)
    }
}

class CSPresetTestPresetItem(override val id: String) : renetik.android.preset.CSPresetItem {
    override val store = CSStoreJsonObject()
    override fun save(properties: Iterable<renetik.android.preset.property.CSPresetKeyData>) =
        properties.forEach { it.saveTo(store) }
}

class CSPresetTestPresetItemList : renetik.android.preset.CSPresetItemList<CSPresetTestPresetItem> {
    override val defaultList = mutableListOf<CSPresetTestPresetItem>()
    override val userList = mutableListOf<CSPresetTestPresetItem>()
    override fun add(item: CSPresetTestPresetItem) {
        defaultList.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        defaultList.remove(item)
    }

    override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)

    override fun reload() = Unit
}

class CSPresetTestParentClass(store: CSStore) : CSEventOwnerHasDestroyBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.add(CSPresetTestPresetItem("Clear Parent"))
    }

    val id = "parentClass"
    val parentPreset = CSPreset(this, store, "$id parent", presetList)
    val property1 = parentPreset.propertyNullInt(this, "property1", 1)
    val property2 = parentPreset.propertyNullInt(this, "property2", 2)
}