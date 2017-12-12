package cs.android.json;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import cs.java.callback.CSReturn;
import cs.java.collections.CSList;
import cs.java.collections.CSMap;

import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.asDouble;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.json;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.newInstance;
import static cs.java.lang.CSLang.no;

public class CSJSONData implements Iterable<String>, CSJSONDataInterface {

    protected CSJSONObject _data;
    private int _index;
    private String _key;
    private String _childDataKey;

    public CSJSONData() {
        _data = CSJSON.createObject();
    }

    public CSJSONData(CSJSONObject object) {
        load(object);
    }

    public void childDataKey(String key) {
        _childDataKey = key;
    }

    public void load(CSJSONObject data) {
        if (no(data)) return;
        _data = data;
        onLoad(data);
    }

    protected void onLoad(CSJSONObject data) {
    }

    public CSJSONObject getObject(CSJSONObject data, String key) {
        return no(data) ? null : data.getObject(key);
    }

    public CSJSONObject getObject(String key) {
        return getObject(data(), key);
    }

    private CSJSONObject data() {
        return is(_childDataKey) ? _data.getObject(_childDataKey) : _data;
    }

    public Boolean getBoolean(String key) {
        return data().getBoolean(key);
    }

    public boolean getBool(String key) {
        return getBool(data(), key);
    }

    public boolean getBool(CSJSONObject data, String key) {
        if (no(data)) return NO;
        Boolean success = data.getBoolean(key);
        return is(success) ? success : NO;
    }

    public CSJSONData getData(String key) {
        CSJSONObject object = data().getObject(key);
        return is(object) ? new CSJSONData(object) : null;
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getString(CSJSONData data, String key) {
        return is(data) ? data.getString(key) : "";
    }

    public String getString(CSJSONObject data, String key) {
        if (no(data)) return "";
        String success = data.getString(key);
        return is(success) ? success : "";
    }

    public String getString(String key) {
        CSJSONType jsonType = data().get(key);
        return is(jsonType) ? jsonType.getJSONValue() + "" : "";
    }

    public Long getLong(String key) {
        try {
            return Long.parseLong(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Integer getInteger(String key) {
        try {
            return Integer.parseInt(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public Object getValue(String key) {
        return asJSONObject().get(key).getValue();
    }

    public CSMap getMap(String key) {
        Object value = getValue(key);
        if (value instanceof CSMap) return (CSMap) value;
        return null;
    }

    public CSList getList(String key) {
        Object value = getValue(key);
        if (value instanceof CSList) return (CSList) value;
        return null;
    }

    public int index() {
        return _index;
    }

    public String key() {
        return _key;
    }

    public Iterator<String> iterator() {
        return data().iterator();
    }

    public void load(CSJSONData data) {
        load(data.asJSONObject());
    }

    public CSList<String> getStrings(String id) {
        if (!data().contains(id)) return null;
        CSList<String> stringlist = list();
        loadStrings(stringlist, data(), id);
        return stringlist;
    }

    public void loadStrings(CSList<String> list, CSJSONObject data, String id) {
        if (data.contains(id)) for (CSJSONType type : data.getArray(id))
            list.add(type.asString());
    }

    public void put(String key, Boolean value) {
        data().put(key, value);
    }

    public void put(String key, CSJSONType object) {
        data().put(key, object);
    }

    public <T extends CSJSONData> void put(String key, CSList<T> value) {
        data().put(key, CSJSON.create(value));
    }

    public <T extends CSJSONData> void put(String key, Map<String, T> value) {
        data().put(key, CSJSON.create(value));
    }

    public void clear(String key) {
        data().remove(key);
    }

    public void put(String key, String value) {
        data().put(key, value);
    }

    public void putNumberString(String string, String value) {
        put(string, asDouble(value));
    }

    public void put(String key, Number value) {
        data().put(key, value);
    }

    public <T extends CSJSONData> void putStringMap(String key, Map<String, String> value) {
        data().put(key, CSJSON.create(value));
    }

    public String toJSONString() {
        return asJSONObject().toJSONString();
    }

    public String toString() {
        return toJSONString();
    }

    public CSJSONArray getArray(CSJSONObject data, String key) {
        return no(data) ? null : data.getArray(key);
    }

    public CSJSONArray getArray(String key) {
        return data().getArray(key);
    }

    protected <T extends CSJSONData> T load(T data, CSJSONObject object) {
        if (no(object)) return null;
        data.load(object);
        return data;
    }

    public CSJSONData setIndex(int index) {
        _index = index;
        return this;
    }

    public CSJSONData setKey(String key) {
        _key = key;
        return this;
    }

    public CSJSONObject asJSONObject() {
        return data();
    }

    protected <T extends CSJSONData> T load(T apiData, CSJSONData jsonData, String id) {
        return load(apiData, jsonData.asJSONObject(), id);
    }

    public <T extends CSJSONData> T load(T apiData, CSJSONObject jsonData, String id) {
        CSJSONObject object = jsonData.getObject(id);
        if (no(object)) return null;
        apiData.load(object);
        return apiData;
    }

    public <T extends CSJSONData> T load(T apiData, String id) {
        return load(apiData, data(), id);
    }

    protected void save(CSJSONObject object, String id, Double string) {
        if (is(string)) object.put(id, string);
    }

    protected void save(CSJSONObject object, String id, CSJSONData data) {
        if (is(data)) object.put(id, data.asJSONObject());
    }

    protected void save(CSJSONObject object, String id, String string) {
        if (is(string)) object.put(id, string);
    }

    public CSJSONObject cloneObject() {
        return json(toJSONString()).asObject();
    }

    protected <T extends CSJSONData> CSList<T> createList(final Class<T> type, String arrayKey) {
        return createList(type, getArray(arrayKey));
    }

    protected <T extends CSJSONData> CSList<T> createList(final Class<T> type, CSJSONArray array) {
        return createList(new CSReturn<T>() {
            public T invoke() {
                return newInstance(type);
            }
        }, array);
    }

    protected <T extends CSJSONData> CSList<T> createList(CSReturn<T> factory, CSJSONArray array) {
        CSList<T> list = list();
        int index = 0;
        for (CSJSONType type : iterate(array)) {
            T item = load(factory.invoke(), type.asObject());
            item.setIndex(index++);
            list.add(item);
        }
        return list;
    }

    protected <T extends CSJSONData> CSList<CSList<T>> createListOfList(Class<T> type, String arrayOfArrayKey) {
        return createListOfList(type, getArray(arrayOfArrayKey));
    }

    protected <T extends CSJSONData> CSList<CSList<T>> createListOfList(Class<T> type, CSJSONArray arrayOfArray) {
        if (no(arrayOfArray)) return null;
        CSList<CSList<T>> list = list();
        for (CSJSONType arrayInArray : arrayOfArray) {
            int index = 0;
            CSList<T> listInList = list.put((CSList) list());
            for (CSJSONType value : iterate(arrayInArray.asArray()))
                listInList.put(load(newInstance(type), value.asObject())).setIndex(index++);
        }
        return list;
    }

    protected <T extends CSJSONData> CSList<T> createListByObject(Class<T> type, String objectKey) {
        return createListByObject(type, getObject(objectKey));
    }

    protected <T extends CSJSONData> CSList<T> createListByObject(Class<T> type, CSJSONObject objectOfObjects) {
        if (no(objectOfObjects)) return null;
        CSList<T> list = list();
        int count = 0;
        for (String key : objectOfObjects) {
            CSJSONData data = list.put(load(newInstance(type), objectOfObjects.getObject(key)));
            data.setIndex(count++);
            data.setKey(key);
        }
        return list;
    }

    protected <T extends CSJSONData> CSList<T> sort(CSList<T> data, Comparator<T> comparator) {
        Collections.sort(data, comparator);
        return reIndex(data);
    }

    protected <T extends CSJSONData> CSList<T> reIndex(CSList<T> array) {
        int index = 0;
        for (CSJSONData data : array) data.setIndex(index++);
        return array;
    }


}
