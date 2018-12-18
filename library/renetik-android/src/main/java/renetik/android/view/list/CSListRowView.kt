package renetik.android.view.list

import renetik.android.json.CSJsonData
import renetik.android.view.base.CSLayoutId

class CSListRowView<DataType : CSJsonData>(
        parent: CSListController<CSListRow<DataType>, *>, layout: CSLayoutId,
        private val onLoadData: (CSListRowView<DataType>).(CSListRow<DataType>) -> Unit)
    : CSRowView<CSListRow<DataType>>(parent, layout) {

    override fun onLoad(row: CSListRow<DataType>) = onLoadData.invoke(this, row)
}
