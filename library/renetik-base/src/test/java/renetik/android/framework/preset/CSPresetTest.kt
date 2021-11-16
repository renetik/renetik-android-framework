package renetik.android.framework.preset

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import renetik.android.framework.CSModelBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.second
import renetik.kotlin.collections.third

const val ClearPresetItemId = "clear parent preset item id"
const val ParentPresetItemId2 = "prent preset item id 2"
const val ParentPresetItemId3 = "prent preset item id 3"
const val ParentPropertyInitialValue = "parent property initial value"
const val ParentPropertyNewValue1 = "parent property new value 1"
const val ParentPropertyNewValue2 = "parent property new value 2"
const val ClearChildPresetItemId = "clear child preset item id"
const val ChildPresetItemId2 = "child preset item id 2"
const val ChildPropertyInitialValue = "child property initial value"
const val ChildPropertyNewValue1 = "child property new value 1"
const val ChildPropertyNewValue2 = "child property new value 2"


class CSPresetTest {

    private val store = CSJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test1() {
        assertEquals(ClearPresetItemId, parent.parentPreset.item.value.id)
        assertEquals(ParentPropertyInitialValue, parent.property.value)
        assertEquals(ClearChildPresetItemId, parent.child.childPreset.item.value.id)
        assertEquals(ChildPropertyInitialValue, parent.child.property.value)
        assertTrue(parent.parentPreset.store.has("child preset store"))
    }

    @Test
    fun test2() {
        parent.property.value = ParentPropertyNewValue1
        assertEquals(ParentPropertyNewValue1, parent.property.value)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId2))

        val parentPropertyNewValue2 = ParentPropertyNewValue2
        parent.property.value = parentPropertyNewValue2

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ParentPropertyInitialValue, parent.property.value)
    }

    @Test
    fun test3() {
        assertEquals(ClearChildPresetItemId, parent.child.childPreset.item.value.id)
        assertEquals(ChildPropertyInitialValue, parent.child.property.value)
        parent.child.property.value = ChildPropertyNewValue1
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)

        parent.child.childPreset.saveAsNew(CSPresetTestPresetItem(ChildPresetItemId2))
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)
        assertEquals(ChildPresetItemId2, parent.child.childPreset.item.value.id)

        parent.parentPreset.reload()
        assertEquals(ClearChildPresetItemId, parent.child.childPreset.item.value.id)
        assertEquals(ChildPropertyInitialValue, parent.child.property.value)
    }

    @Test
    fun test4() {
        parent.child.property.value = ChildPropertyNewValue1
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId2))
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ChildPropertyInitialValue, parent.child.property.value)
    }

    @Test
    fun test5() {
        parent.child.property.value = ChildPropertyNewValue1
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId2))
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)

        parent.child.property.value = ChildPropertyNewValue2
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId3))

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ChildPropertyInitialValue, parent.child.property.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.third()
        assertEquals(ChildPropertyNewValue2, parent.child.property.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.second()
        assertEquals(ChildPropertyNewValue1, parent.child.property.value)
    }
}

class CSPresetTestPresetItem(override val id: String) : CSPresetItem {
    override val store = CSJsonObject()
    override fun delete() = Unit
    override fun save(properties: Iterable<CSPresetKeyData>) =
        properties.forEach { it.saveTo(store) }

    override fun toString() = "${super.toString()}, id:$id"
}

private class CSPresetTestPresetItemList : CSPresetItemList<CSPresetTestPresetItem> {
    override val default = mutableListOf<CSPresetTestPresetItem>()
    override val user = mutableListOf<CSPresetTestPresetItem>()
    override val items = mutableListOf<CSPresetTestPresetItem>()
    override fun put(item: CSPresetTestPresetItem) {
        items.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        items.remove(item)
    }

    override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)
}

private class CSPresetTestParentClass(val store: CSStoreInterface) : CSModelBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem(ClearPresetItemId))
    }

    val parentPreset = CSPreset(this, store, "parent", presetList)
    val child = CSPresetTestChildClass(this, parentPreset)
    val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}

private class CSPresetTestChildClass(
    parent: CSPresetTestParentClass,
    preset: CSPreset<*, *>) :
    CSModelBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.put(CSPresetTestPresetItem(ClearChildPresetItemId))
    }

    val childPreset = CSPreset(this, preset, "child", presetList)
    val property = childPreset.property(this, "property", ChildPropertyInitialValue)
}