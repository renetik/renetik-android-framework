package cs.android.view.list;

import cs.android.json.CSJsonData;
import cs.java.collections.CSList;
import cs.java.lang.CSValues;

import static cs.android.view.list.CSListRow.RowTypes.Row;
import static cs.android.view.list.CSListRow.RowTypes.SectionHeader;
import static cs.android.view.list.CSListRow.RowTypes.SectionSpace;
import static cs.java.lang.CSLang.list;

/**
 * Created by renetik on 08/12/17.
 */

public class CSListRow<DataType extends CSJsonData> {

    private DataType _data;
    private RowTypes _type;
    private int _index;

    public CSListRow(RowTypes type, int index) {
        _type = type;
        _index = index;
    }

    public CSListRow(DataType data, RowTypes type, int index) {
        this(type, index);
        _data = data;
    }

    public static CSListRow createRow(RowTypes type, int index) {
        return new CSListRow(type, index);
    }

    public static CSListRow createTableHeader() {
        return new CSListRow(RowTypes.TableHeader, 0);
    }

    public static CSListRow createTableFooter() {
        return new CSListRow(RowTypes.TableFooter, 0);
    }

    public static CSListRow createRow(CSJsonData data, RowTypes viewType, int index) {
        return new CSListRow(data, viewType, index);
    }

    public static <T extends CSJsonData> CSList<CSListRow<T>> rowsFromList(CSList<T> data) {
        CSList<CSListRow<T>> listData = list(createTableHeader());
        int rowIndex = 0;
        for (T row : data) listData.add(createRow(row, Row, rowIndex++));
        listData.add(createTableFooter());
        return listData;
    }

    public static <T extends CSJsonData, Data extends CSJsonData & CSValues<T>> CSList<CSListRow<Data>> rowsFromListDatas(CSList<Data> dataList) {
        CSList<CSListRow<Data>> listData = list(createTableHeader());
        int sectionIndex = 0;
        for (Data data : dataList) {
            listData.add(createRow(data, SectionHeader, sectionIndex));
            int rowIndex = 0;
            while (rowIndex < data.values().count())
                listData.add(createRow(data, Row, rowIndex++));
            listData.add(createRow(data, SectionSpace, sectionIndex));
            sectionIndex++;
        }
        listData.add(createTableFooter());
        return listData;
    }

    public static <T extends CSJsonData> CSList<CSListRow<T>> rowsFromLists(CSList<CSList<T>> data) {
        CSList<CSListRow<T>> listData = list(createTableHeader());
        int sectionIndex = 0;
        for (CSList<T> sectionData : data) {
            listData.add(createRow(SectionHeader, sectionIndex));
            int rowIndex = 0;
            for (T row : sectionData) listData.add(createRow(row, Row, rowIndex++));
            listData.add(createRow(SectionSpace, sectionIndex));
            sectionIndex++;
        }
        listData.removeLast();
        listData.add(createTableFooter());
        return listData;
    }

    public DataType data() {
        return _data;
    }

    public int index() {
        return _index;
    }

    public RowTypes rowType() {
        return _type;
    }

    public enum RowTypes {
        TableHeader, Row, TableFooter, SectionHeader, SectionSpace
    }

}
