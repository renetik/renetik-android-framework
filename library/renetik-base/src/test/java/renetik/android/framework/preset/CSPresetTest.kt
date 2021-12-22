package renetik.android.framework.preset

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test
import renetik.android.framework.CSModelBase
import renetik.android.framework.json.data.CSJsonObject
import renetik.android.framework.preset.property.CSPresetKeyData
import renetik.android.framework.store.CSStoreInterface
import renetik.kotlin.collections.at
import renetik.kotlin.collections.second
import renetik.kotlin.collections.third

const val ClearPresetItemId = "clear parent preset item id"
const val ParentPresetItemId1 = "prent preset item id 1"
const val ParentPresetItemId2 = "prent preset item id 2"
const val ParentPropertyInitialValue = "parent property initial value"
const val ParentPropertyNewValue1 = "parent property new value 1"
const val ParentPropertyNewValue2 = "parent property new value 2"
const val ClearChildPresetItemId = "clear child preset item id"
const val ChildPresetItemId2 = "child preset item id 2"
const val ChildPropertyInitialValue = "child property initial value"
const val ChildPropertyNewValue0 = "child property new value 1"
const val ChildPropertyNewValue1 = "child property new value 2"
const val ChildPropertyNewValue2 = "child property new value 3"
const val ChildPropertyNewValue3 = "child property new value 4"

class CSPresetTest {

    private val store = CSJsonObject()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test1() {
        assertEquals(ClearPresetItemId, parent.parentPreset.item.value.id)
        assertEquals(ParentPropertyInitialValue, parent.property.value)
        parent.childs.forEach {
            assertEquals(ClearChildPresetItemId, it.childPreset1.item.value.id)
            it.childPreset1Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
            assertEquals(ClearChildPresetItemId, it.childPreset2.item.value.id)
            it.childPreset2Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
        }
        assertTrue(parent.parentPreset.store.has("childs:0 childPreset1 preset store"))
    }

    @Test
    fun test2() {
        parent.property.value = ParentPropertyNewValue1
        assertEquals(ParentPropertyNewValue1, parent.property.value)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId1))

        val parentPropertyNewValue2 = ParentPropertyNewValue2
        parent.property.value = parentPropertyNewValue2

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ParentPropertyInitialValue, parent.property.value)
    }

    @Test
    fun test3() {
        assertEquals(ClearChildPresetItemId, parent.childs.first()
            .childPreset1.item.value.id)
        assertEquals(ChildPropertyInitialValue, parent.childs.first()
            .childPreset1Props.first().value)
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)

        parent.childs.first().childPreset1
            .saveAsNew(CSPresetTestPresetItem(ChildPresetItemId2))
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)
        assertEquals(ChildPresetItemId2, parent.childs.first()
            .childPreset1.item.value.id)

        parent.parentPreset.reload()
        assertEquals(ClearChildPresetItemId, parent.childs.first()
            .childPreset1.item.value.id)
        assertEquals(ChildPropertyInitialValue, parent.childs.first()
            .childPreset1Props.first().value)
    }

    @Test
    fun test4() {
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)

        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId1))
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ChildPropertyInitialValue, parent.childs.first()
            .childPreset1Props.first().value)
    }

    @Test
    fun test5() {
        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue0
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId1))
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)

        parent.childs.first().childPreset1Props.first().value = ChildPropertyNewValue1
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId2))

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ChildPropertyInitialValue, parent.childs.first()
            .childPreset1Props.first().value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.third()
        assertEquals(ChildPropertyNewValue1, parent.childs.first()
            .childPreset1Props.first().value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.second()
        assertEquals(ChildPropertyNewValue0, parent.childs.first()
            .childPreset1Props.first().value)
    }

    @Test
    fun test6() {
        parent.childs.first().childPreset1Props.at(0)!!.value = ChildPropertyNewValue0
        parent.childs.first().childPreset1Props.at(2)!!.value = ChildPropertyNewValue2
        parent.childs.second().childPreset1Props.at(1)!!.value = ChildPropertyNewValue1
        parent.childs.second().childPreset1Props.at(3)!!.value = ChildPropertyNewValue3
        parent.parentPreset.saveAsNew(CSPresetTestPresetItem(ParentPresetItemId1))
        assertEquals(ChildPropertyNewValue1, parent.childs.second()
            .childPreset1Props.at(1)!!.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.first()
        assertEquals(ChildPropertyInitialValue, parent.childs.second()
            .childPreset1Props.at(1)!!.value)

        parent.parentPreset.item.value = parent.parentPreset.list.items.second()
        assertEquals(ChildPropertyNewValue2, parent.childs.first()
            .childPreset1Props.at(2)!!.value)
        assertEquals(ChildPropertyNewValue1, parent.childs.second()
            .childPreset1Props.at(1)!!.value)
    }
}

private class CSPresetTestParentClass(val store: CSStoreInterface) : CSModelBase() {
    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.add(CSPresetTestPresetItem(ClearPresetItemId))
    }

    val parentPreset = CSPreset(this, store, "parent", presetList)

    val childs = List(4) {
        CSPresetTestChildClass(this, parentPreset, "childs:$it")
    }

    val property = parentPreset.property(this, "property", ParentPropertyInitialValue)
}

private class CSPresetTestChildClass(
    parent: CSPresetTestParentClass,
    preset: CSPreset<*, *>,
    key: String) : CSModelBase(parent) {

    private val presetList = CSPresetTestPresetItemList()

    init {
        presetList.add(CSPresetTestPresetItem(ClearChildPresetItemId))
    }

    val childPreset1 = CSPreset(this, preset, "$key childPreset1", presetList)
    val childPreset1Props = List(4) {
        childPreset1.property(this, "$key childPreset1Props:$it property",
            ChildPropertyInitialValue)
    }

    val childPreset2 = CSPreset(this, preset, "$key childPreset2", presetList)
    val childPreset2Props = List(4) {
        childPreset2.property(this, "$key childPreset2Props:$it property",
            ChildPropertyInitialValue)
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
    override val defaultList = mutableListOf<CSPresetTestPresetItem>()
    override val userList = mutableListOf<CSPresetTestPresetItem>()

    override fun add(item: CSPresetTestPresetItem) {
        defaultList.add(item)
    }

    override fun remove(item: CSPresetTestPresetItem) {
        items.remove(item)
    }

    override fun createPresetItem(title: String, isDefault: Boolean, id: String) =
        CSPresetTestPresetItem(title)

    override fun reload() = Unit
}