package renetik.android.framework.extensions

import android.view.ViewGroup
import renetik.android.controller.base.CSView
import renetik.android.core.kotlin.collections.put
import renetik.android.core.kotlin.primitives.update
import renetik.android.event.CSEvent
import renetik.android.event.common.onDestructed
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import renetik.android.event.registration.plus
import renetik.android.preset.Preset
import renetik.android.preset.extensions.action
import renetik.android.ui.extensions.add
import renetik.android.ui.extensions.remove
import renetik.android.ui.protocol.CSViewInterface

fun <ItemView : CSViewInterface> CSHasChangeValue<Int>.updates(
    preset: Preset,
    items: MutableList<ItemView>,
    layout: ViewGroup, fromStart: Boolean = false,
    layoutParams: ViewGroup.LayoutParams? = null,
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
            if (!itemView.isDestructed) layout.remove(itemView)
        })
}

fun <View : CSView<*>, Model> MutableList<View>.viewFactory(
    parent: CSView<*>, list: List<Model>, eventAdded: CSEvent<Model>,
    content: ViewGroup, layoutParams: ViewGroup.LayoutParams? = null,
    fromStart: Boolean = false, create: (Model) -> View
) = apply {
    fun createItemView(model: Model) {
        this += create(model).also { view ->
            val addIndex = if (fromStart) 0 else -1
            layoutParams?.let { content.add(view = view, params = it, addIndex) }
                ?: content.add(view = view, addIndex)
            view.onDestructed { this -= view; content.remove(view) }
        }
    }
    list.forEach(::createItemView)
    parent + eventAdded.listen(::createItemView)
}