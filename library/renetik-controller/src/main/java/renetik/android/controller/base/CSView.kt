package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager.*
import androidx.annotation.LayoutRes
import renetik.android.content.inflate
import renetik.android.content.input
import renetik.android.framework.CSContext
import renetik.android.framework.event.*
import renetik.android.framework.event.CSEvent.CSEventRegistration
import renetik.android.framework.lang.CSLayoutRes
import renetik.android.java.extensions.later
import renetik.android.java.extensions.notNull
import renetik.android.java.extensions.unexpected
import renetik.android.logging.CSLog.logError
import renetik.android.view.extensions.inflate

open class CSView<ViewType : View> : CSContext,
    CSHasParent, CSViewInterface {

    private val layout: CSLayoutRes?
    private val viewId: Int?
    private var parent: CSViewInterface? = null
        set(value) {
            field = value
            register(field?.onDestroy?.listenOnce { onDestroy() })
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
                isDestroyed -> let { logError(this); throw unexpected }
                _view != null -> return _view!!
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

    fun hideKeyboard() {
        hideKeyboardImpl()
        later { hideKeyboardImpl() }
    }

    open fun hideKeyboardImpl() {
        input.hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) = input.showSoftInput(view, flag)

    fun showKeyboard() = input.toggleSoftInput(SHOW_IMPLICIT, 0);

    override fun onDestroy() {
        if (isDestroyed) let { logError(this); throw unexpected }
        if (layout != null) {
            if (_view == null) let { logError(this); throw unexpected }
            // TODO: Should I for some reason destroy children manually ?
            // (_view as? ViewGroup)?.children?.forEach { (it.tag as? CSView<*>)?.onDestroy() }
            _view!!.tag = null
        }
        _view = null
        super.onDestroy()
    }

    override fun onAddedToParent() = Unit

    override fun onRemovedFromParent() {
        if (lifecycleStopOnRemoveFromParent) onDestroy()
    }
}

