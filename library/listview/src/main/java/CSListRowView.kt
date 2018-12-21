package renetik.android.listview

import renetik.android.base.CSLayoutId
import renetik.android.json.data.CSJsonData

class CSListRowView<DataType : CSJsonData>(
        parent: CSListController<CSListRow<DataType>, *>, layout: CSLayoutId,
        private val onLoadData: (CSListRowView<DataType>).(CSListRow<DataType>) -> Unit)
    : CSRowView<CSListRow<DataType>>(parent, layout) {

    override fun onLoad(row: CSListRow<DataType>) = onLoadData.invoke(this, row)
}
