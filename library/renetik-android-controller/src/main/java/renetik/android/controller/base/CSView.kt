package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import renetik.android.core.extensions.content.inflate
import renetik.android.core.extensions.content.inputService
import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar
import renetik.android.event.common.CSContext
import renetik.android.event.common.destruct
import renetik.android.ui.extensions.view
import renetik.android.ui.extensions.view.inflate
import renetik.android.ui.extensions.view.onDestroy
import renetik.android.ui.protocol.CSHasParentView
import renetik.android.ui.protocol.CSViewInterface

open class CSView<ViewType : View> : CSContext,
    CSHasParentView, CSViewInterface {

    var lifecycleStopOnRemoveFromParentView = true

    private val layout: CSLayoutRes?

    @IdRes
    private val viewId: Int?

    private var parent: CSViewInterface? = null

    private var group: ViewGroup? by lazyNullableVar {
        parent?.view as? ViewGroup ?: (parent as? CSView<*>)?.group
    }

    private var _view: ViewType? = null

    constructor(parent: CSViewInterface, view: ViewType) : super(parent) {
        this.parent = parent
        this.layout = null
        this.viewId = null
        setView(view)
    }

    constructor(parent: CSViewInterface, layout: CSLayoutRes) : super(parent) {
        this.parent = parent
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, group: ViewGroup, layout: CSLayoutRes) : super(parent) {
        this.parent = parent
        this.group = group
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, @IdRes viewId: Int) : super(parent) {
        this.parent = parent
        this.layout = null
        this.viewId = viewId
    }

    constructor(parent: CSViewInterface) : super(parent) {
        this.parent = parent
        this.layout = null
        this.viewId = null
    }

    @Suppress("UNCHECKED_CAST")
    final override val view: ViewType
        get() {
            when {
                _view != null -> return _view!!
                isDestructed -> unexpected("$className $this Already destroyed")
                layout != null -> setView(inflate(layout.id))
                viewId != null -> setView(parent!!.view<View>(viewId) as ViewType)
                else -> createView()?.let { setView(it) }
                    ?: run {
                        (parent?.view as? ViewType)?.let {
                            _view = it
                            onViewReady()
                        }
                    }
            }
            return _view!!
        }

    private fun setView(view: ViewType) {
        _view = view
        _view!!.tag = this@CSView
        onViewReady()
    }

    fun <ViewType : View> inflate(@LayoutRes layoutId: Int): ViewType =
        group?.inflate(layoutId) ?: context.inflate(layoutId)

    protected open fun createView(): ViewType? = null

    protected open fun onViewReady() = Unit

    open fun hideKeyboard() {
        inputService.hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    override fun onDestruct() {
//        if (isDestroyed) unexpected("$className $this Already destroyed")
        super.onDestruct()

//        val parentGroup = (_view?.parent as? ViewGroup)
//        if (parentGroup !is AdapterView<*>) parentGroup?.remove(view)

        _view?.tag = "tag instance of $className removed, onDestroy called"
        _view?.onDestroy()
        _view = null
    }

    override fun onAddedToParentView() = Unit

    override fun onRemovedFromParentView() {
        if (lifecycleStopOnRemoveFromParentView && !isDestructed) destruct()
    }

    open var isActivated
        get() = contentView.isActivated
        set(value) {
            contentView.isActivated = value
        }

    open var isEnabled
        get() = contentView.isEnabled
        set(value) {
            contentView.isEnabled = value
        }

    open var isSelected
        get() = contentView.isSelected
        set(value) {
            contentView.isSelected = value
        }

    open val contentView: View get() = view
}