package renetik.android.controller.view.grid
//
//import android.view.ViewGroup
//import renetik.android.controller.base.CSView
//import renetik.android.core.lang.CSLayoutRes
//import renetik.android.core.lang.variable.CSVariable.Companion.variable
//import renetik.android.ui.extensions.view.activated
//import renetik.android.ui.extensions.view.firstChild
//import renetik.android.ui.extensions.view.selected
//
//// Requires item to be wrapped in empty frame layout for now...
//open class CSGridItemView2<RowType : Any?>(
//    parent: CSView<*>,
//    group: ViewGroup,
//    layout: CSLayoutRes,
//    var onLoad: ((CSGridItemView2<RowType>).(RowType) -> Unit)? = null)
//    : CSView<ViewGroup>(parent, layout) {
//
//    constructor(
//        parent: CSView<out ViewGroup>, layout: CSLayoutRes,
//        onLoad: ((CSGridItemView2<RowType>).(RowType) -> Unit)? = null)
//            : this(parent, parent.view, layout, onLoad)
//
//    var value by variable<RowType>()
//    var index = -1
//    var itemDisabled = false
//
//    fun load(value: RowType, index: Int = 0) {
//        this.index = index
//        this.value = value
//        onLoad(value)
//    }
//
//    open fun onLoad(value: RowType) {
//        onLoad?.invoke(this, value)
//    }
//
//    override var isActivated: Boolean
//        get() = view.firstChild!!.isActivated
//        set(value) {
//            view.firstChild!!.activated(value)
//        }
//
//    override var isSelected: Boolean
//        get() = view.firstChild!!.isSelected
//        set(value) {
//            view.firstChild!!.selected(value)
//        }
//}
//
