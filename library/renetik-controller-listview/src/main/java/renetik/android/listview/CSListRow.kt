package renetik.android.listview

import renetik.android.framework.lang.CSListContainer
import renetik.android.java.extensions.collections.deleteLast
import renetik.android.java.extensions.collections.list
import renetik.android.java.extensions.collections.put
import renetik.android.listview.CSListRow.RowTypes.*

fun <DataType : Any> createRow(type: CSListRow.RowTypes, index: Int): CSListRow<DataType> =
    CSListRow(type, index)

fun <DataType : Any> createTableHeader(): CSListRow<DataType> = CSListRow(TableHeader, 0)

fun <DataType : Any> createTableFooter(): CSListRow<DataType> = CSListRow(TableFooter, 0)

fun <DataType : Any> createRow(data: DataType, viewType: CSListRow.RowTypes, index: Int)
        : CSListRow<DataType> = CSListRow(data, viewType, index)

fun <T : Any> rowsFromList(list: List<T>): List<CSListRow<T>> {
    val listRows = list<CSListRow<T>>(createTableHeader())
    var rowIndex = 0
    for (data in list) listRows.add(createRow(data, Row, rowIndex++))
    listRows.add(createTableFooter())
    return listRows
}

fun <T : Any, Data> List<Data>.createListRowsFromListContainers()
        : List<CSListRow<Data>> where Data : CSListContainer<T> {
    val listRows = list<CSListRow<Data>>(createTableHeader())
    var sectionIndex = 0
    for (listContainer in this) {
        listRows.add(createRow(listContainer, SectionHeader, sectionIndex++))
        var rowIndex = 0
        while (rowIndex < listContainer.list.size)
            listRows.add(createRow(listContainer, Row, rowIndex++))
        listRows.add(createRow(listContainer, SectionSpace, sectionIndex))
    }
    listRows.add(createTableFooter())
    return listRows
}

fun <T : Any> List<T>.createListRows(
    isHeader: (index: Int, item: T, sectionIndex: Int, rowIndex: Int) -> Boolean,
    isSpace: (index: Int, item: T, sectionIndex: Int, rowIndex: Int) -> Boolean)
        : List<CSListRow<T>> {
    val listRows = list<CSListRow<T>>(createTableHeader())
    var sectionIndex = 0
    var rowIndex = 0
    for ((index, item) in withIndex()) {
        if (isHeader(index, item, sectionIndex, rowIndex)) {
            listRows.put(CSListRow(item, SectionHeader, sectionIndex++))
            rowIndex = 0
        } else {
            if (isSpace(index, item, sectionIndex, rowIndex))
                listRows.add(createRow(SectionSpace, sectionIndex))
            listRows.add(CSListRow(item, Row, rowIndex++))
        }
    }
    listRows.add(createTableFooter())
    return listRows
}

fun <T : Any> rowsFromLists(list: List<List<T>>): List<CSListRow<T>> {
    val listRows = list<CSListRow<T>>(createTableHeader())
    var sectionIndex = 0
    for (childList in list) {
        listRows.add(createRow(SectionHeader, sectionIndex++))
        var rowIndex = 0
        for (data in childList) listRows.add(createRow(data, Row, rowIndex++))
        listRows.add(createRow(SectionSpace, sectionIndex))
    }
    listRows.deleteLast()
    listRows.add(createTableFooter())
    return listRows
}

class CSListRow<DataType : Any> {

    val type: RowTypes
    val index: Int
    var data: DataType? = null

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
