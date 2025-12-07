package renetik.android.controller.view.grid

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import renetik.android.core.lang.value.minus
import renetik.android.core.lang.variable.assign
import renetik.android.core.lang.variable.plusAssign
import renetik.android.event.property.CSProperty
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.CSRegistration
import kotlin.math.abs

class RecyclerScrollX(private val recycler: RecyclerView) : CSHasChangeValue<Int> {
    private val property = property(0)

    private val onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            property += dx
        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            if (newState == RecyclerView.SCROLL_STATE_IDLE) reconcileWithSystem()
        }
    }

    private val layoutChangeListener = View.OnLayoutChangeListener { _, _, _, _, _, _, _, _, _ ->
        reconcileWithSystem()
    }

    private val adapterObserver = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() = reconcileWithSystem()
        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = reconcileWithSystem()
        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) = reconcileWithSystem()
        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) =
            reconcileWithSystem()
    }

    private fun attach() {
        property assign recycler.computeHorizontalScrollOffset()
        recycler.addOnScrollListener(onScrollListener)
        recycler.addOnLayoutChangeListener(layoutChangeListener)
        recycler.adapter?.registerAdapterDataObserver(adapterObserver)
    }

    fun detach() {
        recycler.removeOnScrollListener(onScrollListener)
        recycler.removeOnLayoutChangeListener(layoutChangeListener)
        recycler.adapter?.unregisterAdapterDataObserver(adapterObserver)
    }

    init {
        attach()
    }

    private fun reconcileWithSystem() {
        val system = runCatching { recycler.computeHorizontalScrollOffset() }
            .getOrDefault(0)
        if (abs(system - property) > 2) property assign system
    }

    override val value: Int get() = property.value
    override fun onChange(function: (Int) -> Unit): CSRegistration = property.onChange(function)
}