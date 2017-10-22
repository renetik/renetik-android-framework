package cs.android.json;

import java.util.Iterator;
import java.util.Map;

import cs.java.callback.CSReturn;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.*;

public class CSJSONData implements Iterable<String>, CSJSONDataInterface {

    private CSJSONObject _data;
    private int _index;

    public CSJSONData() {
        _data = CSJSON.createObject();
    }

    public CSJSONData(CSJSONObject object) {
        load(object);
    }

    public void load(CSJSONObject data) {
        if (no(data)) return;
        _data = data;
        onLoad(data);
    }

    protected void onLoad(CSJSONObject data) {
    }

    public CSJSONObject get(String key) {
        return _data.getObject(key);
    }

    public Boolean getBoolean(String key) {
        return _data.getBoolean(key);
    }

    public CSJSONData getData(String key) {
        CSJSONObject object = _data.getObject(key);
        return is(object) ? new CSJSONData(object) : null;
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getString(String key) {
        CSJSONType jsonType = _data.get(key);
        return is(jsonType) ? jsonType.getValue() + "" : "";
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

    public int index() {
        return _index;
    }

    public Iterator<String> iterator() {
        return _data.iterator();
    }

    public void load(CSJSONData data) {
        load(data.object());
    }

    public CSJSONObject object() {
        return _data;
    }

    public CSList<String> getStrings(String id) {
        if (!_data.contains(id)) return null;
        CSList<String> stringlist = list();
        loadStrings(stringlist, _data, id);
        return stringlist;
    }

    public void loadStrings(CSList<String> list, CSJSONObject data, String id) {
        if (data.contains(id)) for (CSJSONType type : data.getArray(id))
            list.add(type.asString());
    }

    public void put(String key, Boolean value) {
        _data.put(key, value);
    }

    public void put(String key, CSJSONType object) {
        _data.put(key, object);
    }

    public <T extends CSJSONData> void put(String key, CSList<T> value) {
        _data.put(key, CSJSON.create(value));
    }

    public <T extends CSJSONData> void put(String key, Map<String, T> value) {
        _data.put(key, CSJSON.create(value));
    }

    public void clear(String key) {
        _data.remove(key);
    }

    public void put(String key, String value) {
        _data.put(key, value);
    }

    public void putNumberString(String string, String value) {
        put(string, asDouble(value));
    }

    public void put(String key, Number value) {
        _data.put(key, value);
    }

    public <T extends CSJSONData> void putStringMap(String key, Map<String, String> value) {
        _data.put(key, CSJSON.create(value));
    }

    public String toJSONString() {
        return object().toJSONString();
    }

    public String toString() {
        return toJSONString();
    }

    protected <T extends CSJSONData> CSList<T> createList(String id, final Class<T> type) {
        return createList(id, new CSReturn<T>() {
            public T invoke() {
                return newInstance(type);
            }
        });
    }

    protected <T extends CSJSONData> CSList<T> createList(String id, CSReturn<T> factory) {
        CSList list = list();
        int index = -1;
        for (CSJSONType type : iterate(getArray(id))) {
            T item = load(factory.invoke(), type.asObject());
            item.setIndex(++index);
            list.add(item);
        }
        return list;
    }

    public CSJSONArray getArray(String key) {
        return _data.getArray(key);
    }

    protected <T extends CSJSONData> T load(T data, CSJSONObject object) {
        if (no(object)) return null;
        data.load(object);
        return data;
    }

    public void setIndex(int index) {
        _index = index;
    }

    public CSJSONObject asJSON() {
        return data();
    }

    public final CSJSONObject data() {
        return _data;
    }

    protected <T extends CSJSONData> T load(T apiData, CSJSONData jsonData, String id) {
        return load(apiData, jsonData._data, id);
    }

    public <T extends CSJSONData> T load(T apiData, CSJSONObject jsonData, String id) {
        CSJSONObject object = jsonData.getObject(id);
        if (no(object)) return null;
        apiData.load(object);
        return apiData;
    }

    public <T extends CSJSONData> T load(T apiData, String id) {
        return load(apiData, _data, id);
    }

    protected void save(CSJSONObject object, String id, Double string) {
        if (is(string)) object.put(id, string);
    }

    protected void save(CSJSONObject object, String id, CSJSONData data) {
        if (is(data)) object.put(id, data.data());
    }

    protected void save(CSJSONObject object, String id, String string) {
        if (is(string)) object.put(id, string);
    }

    public CSJSONObject cloneObject() {
        return json(toJSONString()).asObject();
    }
}
