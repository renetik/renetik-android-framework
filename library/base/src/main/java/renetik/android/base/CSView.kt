package renetik.android.base

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import renetik.android.extensions.inflate
import renetik.android.view.extensions.fade
import renetik.android.view.extensions.hide
import renetik.android.view.extensions.inflate
import renetik.android.view.extensions.show
import renetik.android.java.extensions.notNull

open class CSView<ViewType : View>(context: Context) : CSContextController(context) {

    private var parentGroup: ViewGroup? = null
    private var layoutId: CSLayoutId? = null
    private var _view: ViewType? = null
    fun setView(view: ViewType) {
        _view = view
    }

    val view: ViewType by lazy {
        val view = _view ?: layoutId?.let { inflate(it.id) } ?: let { createView()!! }
        view.tag = this
        view
    }

    fun inflate(layoutId: Int): ViewType = parentGroup?.inflate(layoutId)
            ?: context.inflate(layoutId)

    protected open fun createView(): ViewType? = null

    constructor(parent: Context, layoutId: CSLayoutId? = null) : this(parent) {
        this.layoutId = layoutId
    }

    constructor(parent: ViewGroup, layoutId: CSLayoutId) : this(parent.context, layoutId) {
        this.parentGroup = parent
    }

    constructor(parent: CSView<ViewGroup>, layoutId: CSLayoutId) : this(parent.view, layoutId)

    fun fade(fadeIn: Boolean) = view.fade(fadeIn)
    open fun show(): CSView<ViewType> = apply { view.show() }
    open fun hide(): CSView<ViewType> = apply { view.hide() }
    val hasParent get() = view.parent.notNull

    open fun hideKeyboard() {
        service<InputMethodManager>(Context.INPUT_METHOD_SERVICE)
                .hideSoftInputFromWindow(view.rootView.windowToken, 0)
    }

    fun showKeyboard(view: View, flag: Int) =
            service<InputMethodManager>(Context.INPUT_METHOD_SERVICE).showSoftInput(view, flag)

    override fun onDestroy() {
        super.onDestroy()
        view.tag = null
    }
}
