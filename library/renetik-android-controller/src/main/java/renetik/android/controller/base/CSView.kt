package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import renetik.android.core.extensions.content.inflate
import renetik.android.core.extensions.content.inputService
import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes
import renetik.android.event.common.CSContext
import renetik.android.ui.extensions.view.inflate
import renetik.android.ui.extensions.view.onClick
import renetik.android.ui.extensions.view.onDestroy
import renetik.android.ui.protocol.CSHasParentView
import renetik.android.ui.protocol.CSViewInterface

open class CSView<ViewType : View> : CSContext,
    CSHasParentView, CSViewInterface {

    private val layout: CSLayoutRes?
    private val viewId: Int?
    private var parent: CSViewInterface? = null
    var lifecycleStopOnRemoveFromParentView = true
    private var _group: ViewGroup? = null
    private val group: ViewGroup? by lazy {
        _group ?: parent?.view as? ViewGroup ?: (parent as? CSView<*>)?.group
    }

    private var _view: ViewType? = null

    constructor(parent: CSViewInterface, layout: CSLayoutRes) : super(parent) {
        this.parent = parent
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, group: ViewGroup, layout: CSLayoutRes) : super(parent) {
        this.parent = parent
        this._group = group
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, viewId: Int) : super(parent) {
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
                isDestroyed -> unexpected("$className $this Already destroyed")
                layout != null -> setView(inflate(layout.id))
                viewId != null -> setView(parent!!.view(viewId) as ViewType)
                else -> createView()?.let { setView(it) } ?: run {
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

    override fun onDestroy() {
        if (isDestroyed) unexpected("$className $this Already destroyed")
        _view?.tag = "tag instance of $className removed, onDestroy called"
        super.onDestroy()
        _view?.onDestroy()
        _view = null
    }

    override fun onAddedToParentView() = Unit

    override fun onRemovedFromParentView() {
        if (lifecycleStopOnRemoveFromParentView && !isDestroyed) onDestroy()
    }

    open var isActivated
        get() = view.isActivated
        set(value) {
            view.isActivated = value
        }

    open var isEnabled
        get() = view.isEnabled
        set(value) {
            view.isEnabled = value
        }

    open var isSelected
        get() = view.isSelected
        set(value) {
            view.isSelected = value
        }

    open fun onClick(function: (view: ViewType) -> Unit) = apply { view.onClick(function) }
}