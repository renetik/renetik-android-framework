package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager.*
import androidx.annotation.LayoutRes
import renetik.android.content.inflate
import renetik.android.content.input
import renetik.android.framework.CSContext
import renetik.android.framework.event.*
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.view.inflate
import renetik.android.view.onClick
import renetik.kotlin.className
import renetik.kotlin.notNull
import renetik.kotlin.unexpected

open class CSView<ViewType : View> : CSContext,
    CSHasParent, CSViewInterface {

    private val layout: CSLayoutRes?
    private val viewId: Int?
    private var parent: CSViewInterface? = null
        set(value) {
            field = value
            register(field?.eventDestroy?.listenOnce { onDestroy() })
        }
    var lifecycleStopOnRemoveFromParent = true
    private var _group: ViewGroup? = null
    private val group: ViewGroup? by lazy {
        _group ?: parent?.view as? ViewGroup ?: (parent as? CSView<*>)?.group
    }

    private var _view: ViewType? = null

    protected constructor(parent: CSViewInterface, layout: CSLayoutRes) : super(parent) {
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
    override val view: ViewType
        get() {
            when {
                _view != null -> return _view!!
                isDestroyed -> unexpected("$className $this Already destroyed")
                layout != null -> setView(inflate(layout.id))
                viewId != null -> setView(parent!!.view(viewId) as ViewType)
                else -> (parent?.view as? ViewType)?.let {
                    _view = it
                    onViewReady()
                } ?: setView(obtainView()!!)
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

    protected open fun obtainView(): ViewType? = null

    protected open fun onViewReady() = Unit

    val hasParent get() = view.parent.notNull

    fun hideKeyboard() = hideKeyboardImpl()

    open fun hideKeyboardImpl() {
        input.hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) = input.showSoftInput(view, flag)

    fun showKeyboard() = input.toggleSoftInput(SHOW_IMPLICIT, 0);

    override fun onDestroy() {
        if (isDestroyed) unexpected("$className $this Already destroyed")
        // We need live view instance when calling onDestroy,
        // because it can be used by clients
        super.onDestroy()
        if (layout != null) {
            if (_view == null) unexpected("$className $this _view shouldn't be null")
            _view!!.tag = "tag instance of $className removed, onDestroy called"
        }
        _view = null
    }

    override fun onAddedToParent() = Unit

    override fun onRemovedFromParent() {
        if (lifecycleStopOnRemoveFromParent && !isDestroyed) onDestroy()
    }

    open var isActivated
        get() = view.isActivated
        set(value) {
            view.isActivated = value
        }

    open var isSelected
        get() = view.isSelected
        set(value) {
            view.isSelected = value
        }

    open fun onClick(function: (view: ViewType) -> Unit) = apply { view.onClick(function) }
}