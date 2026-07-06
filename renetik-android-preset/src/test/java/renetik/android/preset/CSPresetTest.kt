package renetik.android.preset

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import renetik.android.core.kotlin.collections.at
import renetik.android.core.kotlin.collections.second
import renetik.android.core.lang.variable.assign
import renetik.android.preset.model.CSPresetTestParentClass
import renetik.android.preset.model.ChildPresetItemId2
import renetik.android.preset.model.ChildPropertyInitialValue
import renetik.android.preset.model.ChildPropertyNewValue0
import renetik.android.preset.model.ChildPropertyNewValue1
import renetik.android.preset.model.ChildPropertyNewValue2
import renetik.android.preset.model.ChildPropertyNewValue3
import renetik.android.preset.model.ClearChildPresetItemId
import renetik.android.preset.model.ClearPresetItemId
import renetik.android.preset.model.ParentPresetItemId1
import renetik.android.preset.model.ParentPresetItemId2
import renetik.android.preset.model.ParentPropertyInitialValue
import renetik.android.preset.model.ParentPropertyNewValue1
import renetik.android.preset.model.ParentPropertyNewValue2
import renetik.android.preset.model.defaultCategory
import renetik.android.store.type.CSJsonObjectStore
import renetik.android.testing.CSAssert.assert

@Deprecated("Replace all cases by simple tests")
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class CSPresetTest {

    @Before
    fun setUp() = Dispatchers.setMain(StandardTestDispatcher())

    @After
    fun tearDown() = Dispatchers.resetMain()

    private val store = CSJsonObjectStore()
    private val parent = CSPresetTestParentClass(store)

    @Test
    fun test1() = runTest {
        assertEquals(ClearPresetItemId, parent.parentPreset.listItem.value.id)
        assertEquals(ParentPropertyInitialValue, parent.property.value)
        parent.children.forEach {
            assertEquals(ClearChildPresetItemId, it.childPreset1.listItem.value.id)
            it.childPreset1Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
            assertEquals(ClearChildPresetItemId, it.childPreset2.listItem.value.id)
            it.childPreset2Props.forEach {
                assertEquals(ChildPropertyInitialValue, it.value)
            }
        }
        advanceUntilIdle()
        assertTrue(parent.parentPreset.store.has("children:0 childPreset1 preset store"))
    }

    @Test
    fun test2() = runTest {
        parent.property.value = ParentPropertyNewValue1
        assertEquals(ParentPropertyNewValue1, parent.property.value)

        val item = parent.parentPreset.list
            .createItem(ParentPresetItemId1, category = defaultCategory)
        parent.parentPreset.save(item)

        val parentPropertyNewValue2 = ParentPropertyNewValue2
        parent.property.value = parentPropertyNewValue2

        parent.parentPreset.reloadInternal()
        assertEquals(ParentPropertyInitialValue, parent.property.value)
    }

    @Test
    fun test3() = runTest {
        assertEquals(
            ClearChildPresetItemId, parent.children.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        val item = parent.children.first().childPreset1.list
            .createItem(ChildPresetItemId2, category = defaultCategory)
        parent.children.first().childPreset1.save(item)
        parent.children.first().childPreset1.listItem assign item

        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )
        assertEquals(
            ChildPresetItemId2, parent.children.first()
                .childPreset1.listItem.value.id
        )

        parent.parentPreset.reloadInternal()
        advanceUntilIdle()
        assertEquals(
            ClearChildPresetItemId, parent.children.first()
                .childPreset1.listItem.value.id
        )
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test4() = runTest {
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        assert(
            expected = ChildPropertyNewValue0,
            actual = parent.children.first().childPreset1Props.first().value
        )
        val item = parent.parentPreset.list.createItem(
            ParentPresetItemId1, category = defaultCategory)
        parent.parentPreset.save(item)
        advanceUntilIdle()
        assert(
            expected = ChildPropertyNewValue0,
            actual = parent.children.first().childPreset1Props.first().value
        )
        parent.parentPreset.reloadInternal()
        advanceUntilIdle()
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test5() = runTest {
        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue0
        val item1 = parent.parentPreset.list.createItem(
            ParentPresetItemId1, category = defaultCategory)
        parent.parentPreset.save(item1)
        advanceUntilIdle()
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.children.first().childPreset1Props.first().value = ChildPropertyNewValue1

        val item2 = parent.parentPreset.list.createItem(
            ParentPresetItemId2, category = defaultCategory)
        parent.parentPreset.save(item2)
        parent.parentPreset.reloadInternal()
        advanceUntilIdle()
        assertEquals(
            ChildPropertyInitialValue, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list[2]
        advanceUntilIdle()
        assertEquals(
            ChildPropertyNewValue1, parent.children.first()
                .childPreset1Props.first().value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list[1]
        advanceUntilIdle()
        assertEquals(
            ChildPropertyNewValue0, parent.children.first()
                .childPreset1Props.first().value
        )
    }

    @Test
    fun test6() = runTest {
        parent.children.first().childPreset1Props.at(0)!!.value = ChildPropertyNewValue0
        parent.children.first().childPreset1Props.at(2)!!.value = ChildPropertyNewValue2
        parent.children.second().childPreset1Props.at(1)!!.value = ChildPropertyNewValue1
        parent.children.second().childPreset1Props.at(3)!!.value = ChildPropertyNewValue3

        val item = parent.parentPreset.list.createItem(
            ParentPresetItemId1, category = defaultCategory)
        parent.parentPreset.save(item)
        advanceUntilIdle()
        assertEquals(
            ChildPropertyNewValue1, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.reloadInternal()
        advanceUntilIdle()
        assertEquals(
            ChildPropertyInitialValue, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )

        parent.parentPreset.listItem.value = parent.parentPreset.list[1]
        advanceUntilIdle()
        assertEquals(
            ChildPropertyNewValue2, parent.children.first()
                .childPreset1Props.at(2)!!.value
        )
        assertEquals(
            ChildPropertyNewValue1, parent.children.second()
                .childPreset1Props.at(1)!!.value
        )
    }
}
