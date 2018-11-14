package renetik.android.view.list

import renetik.android.java.collections.CSList
import renetik.android.java.collections.list
import renetik.android.java.lang.CSValues
import renetik.android.json.CSJsonData
import renetik.android.view.list.CSListRow.RowTypes
import renetik.android.view.list.CSListRow.RowTypes.*

fun <DataType : CSJsonData> createRow(type: RowTypes, index: Int): CSListRow<DataType> {
    return CSListRow(type, index)
}

fun <DataType : CSJsonData> createTableHeader(): CSListRow<DataType> {
    return CSListRow(RowTypes.TableHeader, 0)
}

fun <DataType : CSJsonData> createTableFooter(): CSListRow<DataType> {
    return CSListRow(RowTypes.TableFooter, 0)
}

fun <DataType : CSJsonData> createRow(data: DataType, viewType: RowTypes, index: Int)
        : CSListRow<DataType> {
    return CSListRow(data, viewType, index)
}

fun <T : CSJsonData> rowsFromList(data: CSList<T>): CSList<CSListRow<T>> {
    val listData = list<CSListRow<T>>(createTableHeader())
    var rowIndex = 0
    for (row in data) listData.add(createRow(row, Row, rowIndex++))
    listData.add(createTableFooter())
    return listData
}

fun <T : CSJsonData, Data> rowsFromListDatas(dataList: CSList<Data>)
        : CSList<CSListRow<Data>> where  Data : CSJsonData, Data : CSValues<T> {
    val listData = list<CSListRow<Data>>(createTableHeader())
    var sectionIndex = 0
    for (data in dataList) {
        listData.add(createRow(data, SectionHeader, sectionIndex))
        var rowIndex = 0
        while (rowIndex < data.values().count())
            listData.add(createRow(data, Row, rowIndex++))
        listData.add(createRow(data, SectionSpace, sectionIndex))
        sectionIndex++
    }
    listData.add(createTableFooter())
    return listData
}

fun <T : CSJsonData> rowsFromLists(data: CSList<CSList<T>>): CSList<CSListRow<T>> {
    val listData = list<CSListRow<T>>(createTableHeader())
    var sectionIndex = 0
    for (sectionData in data) {
        listData.add(createRow(SectionHeader, sectionIndex))
        var rowIndex = 0
        for (row in sectionData) listData.add(createRow(row, Row, rowIndex++))
        listData.add(createRow(SectionSpace, sectionIndex))
        sectionIndex++
    }
    listData.removeLast()
    listData.add(createTableFooter())
    return listData
}

class CSListRow<DataType : CSJsonData> {

    val type: RowTypes
    val index: Int
    lateinit var data: DataType

    constructor(type: RowTypes, index: Int) {
        this.type = type
        this.index = index
    }

    constructor(data: DataType, type: RowTypes, index: Int) : this(type, index) {
        this.data = data
    }

    enum class RowTypes {
        TableHeader, Row, TableFooter, SectionHeader, SectionSpace
    }
}
