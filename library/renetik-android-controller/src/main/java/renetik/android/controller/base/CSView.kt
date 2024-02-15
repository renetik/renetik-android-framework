package renetik.android.controller.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.view.ContextThemeWrapper
import renetik.android.core.extensions.content.inputService
import renetik.android.core.kotlin.className
import renetik.android.core.kotlin.unexpected
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar
import renetik.android.core.logging.CSLog.logErrorTrace
import renetik.android.event.common.CSContext
import renetik.android.event.common.destruct
import renetik.android.ui.extensions.view
import renetik.android.ui.extensions.view.onDestroy
import renetik.android.ui.protocol.CSHasParentView
import renetik.android.ui.protocol.CSViewInterface

open class CSView<ViewType : View> : CSContext,
    CSHasParentView, CSViewInterface {

    var lifecycleStopOnRemoveFromParentView = true

    private val layout: CSLayoutRes?

    @IdRes
    private val viewId: Int?

    var parentView: CSViewInterface? = null
        private set

    private var group: ViewGroup? by lazyNullableVar {
        parentView?.view as? ViewGroup ?: (parentView as? CSView<*>)?.group
    }

    private var _view: ViewType? = null

    constructor(parent: CSViewInterface, view: ViewType) : super(parent) {
        this.parentView = parent
        this.layout = null
        this.viewId = null
        setView(view)
    }

    constructor(parent: CSViewInterface, layout: CSLayoutRes) : super(parent) {
        this.parentView = parent
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, group: ViewGroup, layout: CSLayoutRes) : super(parent) {
        this.parentView = parent
        this.group = group
        this.layout = layout
        this.viewId = null
    }

    constructor(parent: CSViewInterface, @IdRes viewId: Int) : super(parent) {
        this.parentView = parent
        this.layout = null
        this.viewId = viewId
    }

    constructor(parent: CSViewInterface) : super(parent) {
        this.parentView = parent
        this.layout = null
        this.viewId = null
    }

    @Suppress("UNCHECKED_CAST")
    final override val view: ViewType
        get() {
            when {
                _view != null -> return _view!!
                isDestructed -> logErrorTrace { "$className $this Already destroyed" }
                layout != null -> setView(inflate(layout.id))
                viewId != null -> setView(parentView!!.view<View>(viewId) as ViewType)
                else -> createView()?.let { setView(it) }
                    ?: run {
                        (parentView?.view as? ViewType)?.let {
                            _view = it
                            onViewReady()
                        }
                    }
            }
            return _view!!
        }

    private fun setView(view: ViewType) {
        _view = view
        if (view.tag !is CSView<*>) view.tag = this@CSView
        onViewReady()
    }

    val isViewReady: Boolean get() = _view != null

    fun <ViewType : View> inflate(@LayoutRes layoutId: Int): ViewType {
        val context = CSViewInterface.themeOverride?.let {
            ContextThemeWrapper(this, it)
        } ?: this
        val inflater = LayoutInflater.from(context)
        @Suppress("UNCHECKED_CAST")
        return (group?.let { inflater.inflate(layoutId, it, false) }
            ?: inflater.inflate(layoutId, null)) as ViewType
    }

    protected open fun createView(): ViewType? = null

    protected open fun onViewReady() = Unit

    open fun hideKeyboard() {
        inputService.hideSoftInputFromWindow(view.rootView.windowToken, 0)
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

    override fun onDestruct() {
        super.onDestruct()
        _view?.let {
            if (it.tag == this) {
                it.tag = "tag instance of $className removed, onDestroy called"
                it.onDestroy()
            }
            _view = null
        } ?: unexpected()
    }
}