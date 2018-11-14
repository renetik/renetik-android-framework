package renetik.android.view.list

import renetik.android.json.CSJsonData
import renetik.android.viewbase.CSLayoutId

class CSListRowView<DataType : CSJsonData>(
        parent: CSListController<CSListRow<DataType>, *>, layout: CSLayoutId,
        private val onLoad: (CSListRowView<DataType>) -> Unit)
    : CSRowView<CSListRow<DataType>>(parent, layout) {

    override fun onLoad(row: CSListRow<DataType>) = onLoad.invoke(this)
}
