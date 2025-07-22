@file:Suppress("LeakingThis")

package renetik.android.controller.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import renetik.android.core.extensions.content.inputService
import renetik.android.core.kotlin.className
import renetik.android.core.lang.CSLayoutRes
import renetik.android.core.lang.lazy.CSLazyNullableVar.Companion.lazyNullableVar
import renetik.android.core.lang.value.isTrue
import renetik.android.core.lang.variable.CSVariable.Companion.variable
import renetik.android.core.logging.CSLog.logErrorTrace
import renetik.android.event.CSEvent.Companion.event
import renetik.android.event.common.CSContext
import renetik.android.event.common.destruct
import renetik.android.event.invoke
import renetik.android.event.property.CSProperty.Companion.property
import renetik.android.event.registration.CSHasChangeValue
import renetik.android.event.registration.onChange
import renetik.android.event.registration.plus
import renetik.android.ui.extensions.inflate
import renetik.android.ui.extensions.view
import renetik.android.ui.extensions.view.isVisible
import renetik.android.ui.extensions.view.onDestroy
import renetik.android.ui.protocol.CSHasParentView
import renetik.android.ui.protocol.CSViewInterface
import renetik.android.ui.protocol.CSViewInterface.Companion.context

open class CSView<ViewType : View> : CSContext,
    CSHasParentView, CSViewInterface {

    var lifecycleStopOnRemoveFromParentView = true

    private val layout: Int?

    @IdRes
    private val viewId: Int?

    var parentView: CSViewInterface by variable()

    private var group: ViewGroup? by lazyNullableVar {
        parentView.view as? ViewGroup ?: (parentView as? CSView<*>)?.group
    }

    private var _view: ViewType? = null

    constructor(parent: CSViewInterface, view: ViewType) :
            super(parent, view.context) {
        this.parentView = parent
        this + parentView.isVisible.onChange(::updateVisibility)
        this.layout = null
        this.viewId = null
        setView(view)
    }

    constructor(parent: CSViewInterface, layout: CSLayoutRes) :
            super(parent, context(parent)) {
        this.parentView = parent
        this + parentView.isVisible.onChange(::updateVisibility)
        this.layout = layout.id
        this.viewId = null
    }

    constructor(parent: CSViewInterface, group: ViewGroup, layout: CSLayoutRes) :
            super(parent, context(parent)) {
        this.parentView = parent
        this + parentView.isVisible.onChange(::updateVisibility)
        this.group = group
        this.layout = layout.id
        this.viewId = null
    }

    constructor(parent: CSViewInterface, @IdRes viewId: Int) : super(parent) {
        this.parentView = parent
        this + parentView.isVisible.onChange(::updateVisibility)
        this.layout = null
        this.viewId = viewId
    }

    constructor(
        parent: CSViewInterface,
        @IdRes viewId: Int? = null,
        @LayoutRes layout: Int? = null,
    ) : super(parent) {
        this.parentView = parent
        this + parentView.isVisible.onChange(::updateVisibility)
        this.viewId = viewId
        this.layout = layout
    }

    @Suppress("UNCHECKED_CAST")
    final override val view: ViewType
        get() {
            when {
                _view != null -> return _view!!
                isDestructed -> logErrorTrace { "$className $this Already destroyed" }
                layout != null -> setView(inflate(layout))
                viewId != null -> setView(parentView.view<View>(viewId) as ViewType)
                else -> createView()?.let { setView(it) }
                    ?: run {
                        (parentView.view as? ViewType)?.let {
                            _view = it
                            onViewReady()
                        }
                    }
            }
            return _view!!
        }

    protected fun setView(view: ViewType) {
        _view = view
        if (view.tag !is CSView<*>) view.tag = this@CSView
        onViewReady()
    }

    val isViewReady: Boolean get() = _view != null

    fun <ViewType : View> inflate(@LayoutRes layoutId: Int): ViewType =
        context.inflate(layoutId, group)

    protected open fun createView(): ViewType? = null

    protected open fun onViewReady() = Unit

    open fun hideKeyboard() {
        inputService.hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    override fun onAddedToParentView() {
        updateVisibility()
    }

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

    //Visibility
    private val _isVisible = property(false)
    override val isVisible: CSHasChangeValue<Boolean> get() = _isVisible
    private var onViewShowingCalled = false

    override fun updateVisibility() {
        if (checkIfIsShowing()) {
            if (!isVisible.isTrue) onViewVisibilityChanged(true)
        } else if (isVisible.isTrue) onViewVisibilityChanged(false)
    }

    protected open fun checkIfIsShowing(): Boolean =
        // Maybe parentView.view.isVisible in some cases will cause problems...
        view.isVisible && parentView.isVisible.isTrue

    private fun onViewVisibilityChanged(showing: Boolean) {
        if (isVisible.value == showing) return
        _isVisible.value(showing)
        if (isVisible.isTrue) onViewShowing() else onViewHiding()
    }

    protected open fun onViewShowing() {
        if (!onViewShowingCalled) {
            onViewShowingFirstTime()
            onViewShowingCalled = true
        } else onViewShowingAgain()
    }

    val onViewShowingFirstTime = event()
    protected open fun onViewShowingFirstTime() = onViewShowingFirstTime.invoke()

    protected open fun onViewShowingAgain() {}

    protected open fun onViewHiding() {
        if (!onViewShowingCalled) {
            onViewHidingFirstTime()
            onViewShowingCalled = true
        } else onViewHidingAgain()
    }

    protected open fun onViewHidingFirstTime() {}
    protected open fun onViewHidingAgain() {}

    override fun onDestruct() {
//        _isVisible.setFalse()
        super.onDestruct()
        // View doesn't have to be created in some cases
        _view?.let {
            if (it.tag == this) {
                it.tag = "tag instance of $className removed, onDestroy called"
                it.onDestroy()
            }
            _view = null
        }
    }
}