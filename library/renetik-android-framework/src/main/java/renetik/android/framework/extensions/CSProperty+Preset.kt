package renetik.android.framework.extensions

import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import renetik.android.controller.base.CSView
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.common.CSHasRegistrationsHasDestruct
import renetik.android.event.registration.CSHasChange
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSHasRegistrations
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.invoke
import renetik.android.event.registration.plus
import renetik.android.preset.Preset
import renetik.android.preset.extensions.action
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.minusAssign
import renetik.android.ui.extensions.view.add
import renetik.android.ui.extensions.view.removeAt
import renetik.android.ui.protocol.CSViewInterface

fun <ItemView : CSViewInterface> CSHasChangeValue<Int>.updates(
    preset: Preset,
    items: MutableList<ItemView>,
    layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: LayoutParams? = null,
    createView: (index: Int) -> ItemView
): CSRegistration = action(preset) { value ->
    items.size.update(value,
        onAdd = { index ->
            val view = items.put(createView(index))
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { layout.add(view = view, params = it, index = addIndex) }
                ?: layout.add(view = view, index = addIndex)
        },
        onRemove = { index ->
            val itemView: ItemView = items.removeAt(index)
            if (!itemView.isDestructed) layout -= itemView
        })
}

fun <View : CSViewInterface, Model> MutableList<View>.viewFactory(
    parent: CSView<*>, list: List<Model>, eventAdded: CSHasChange<Model>,
    content: ViewGroup, layoutParams: LayoutParams? = null,
    fromStart: Boolean = false, create: (Model) -> View
) = apply {
    fun createView(model: Model) {
        this += create(model).also { view ->
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { content.add(view = view, params = it, addIndex) }
                ?: content.add(view = view, addIndex)
            view.eventDestruct { this -= view; content -= view }
        }
    }
    list.forEach(::createView)
    parent + eventAdded.onChange(::createView)
}


fun <View : CSViewInterface, Model> List<Model>.viewFactory(
    parent: CSHasRegistrations, eventAdded: CSHasChange<Model>,
    content: ViewGroup, layoutParams: LayoutParams? = null,
    fromStart: Boolean = false, create: (Model) -> View
) {
    fun createView(model: Model) {
        create(model).also { view ->
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { content.add(view = view, params = it, addIndex) }
                ?: content.add(view = view, addIndex)
            view.eventDestruct { content -= view }
        }
    }
    forEach(::createView)
    parent + eventAdded.onChange(::createView)
}

fun <V : CSViewInterface, M> MutableList<V>.viewFactory(
    parent: CSView<*>, list: List<M>, eventAdded: CSHasChange<M>,
    content: ViewGroup, layoutParams: LayoutParams? = null,
    fromStart: Boolean = false, separator: () -> View, create: (M) -> V,
) = apply {
    fun removeSeparator(view: V) {
        val separatorOffset = if (fromStart) 1 else -1
        val indexOfView = content.indexOfChild(view.view)
        val separatorIndex = indexOfView + separatorOffset
        content.removeAt(separatorIndex)
    }

    fun createView(model: M) {
        this += create(model).also { view ->
            val addIndex = if (fromStart) 0 else -1
            if (isNotEmpty()) content.add(separator(), addIndex)
            layoutParams?.let {
                content.add(view = view, params = it, addIndex)
            } ?: content.add(view = view, addIndex)
            view.eventDestruct {
                if (size > 1) removeSeparator(view)
                content -= view
                this -= view
            }
        }
    }
    list.forEach(::createView)
    parent + eventAdded.onChange(::createView)
}