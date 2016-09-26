package cs.android.json;

import java.util.Iterator;
import java.util.Map;

import cs.java.callback.Return;
import cs.java.collections.CSList;

import static cs.java.lang.Lang.*;

public class JSONData implements Iterable<String>, isJSON {

    private JSONObject _data;
    private int _index;

    public JSONData() {
        _data = JSON.createObject();
    }

    public JSONData(JSONObject object) {
        load(object);
    }

    public void load(JSONObject data) {
        if (no(data)) return;
        _data = data;
        onLoad(data);
    }

    protected void onLoad(JSONObject data) {
    }

    public JSONObject get(String key) {
        return _data.getObject(key);
    }

    public Boolean getBoolean(String key) {
        return _data.getBoolean(key);
    }

    public JSONData getData(String key) {
        JSONObject object = _data.getObject(key);
        return is(object) ? new JSONData(object) : null;
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getString(String key) {
        JSONType jsonType = _data.get(key);
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

    public void load(JSONData data) {
        load(data.object());
    }

    public JSONObject object() {
        return _data;
    }

    public CSList<String> getStrings(String id) {
        if (!_data.contains(id)) return null;
        CSList<String> stringlist = list();
        loadStrings(stringlist, _data, id);
        return stringlist;
    }

    public void loadStrings(CSList<String> list, JSONObject data, String id) {
        if (data.contains(id)) for (JSONType type : data.getArray(id))
            list.add(type.asString());
    }

    public void put(String key, Boolean value) {
        _data.put(key, value);
    }

    public void put(String key, JSONType object) {
        _data.put(key, object);
    }

    public <T extends JSONData> void put(String key, CSList<T> value) {
        _data.put(key, JSON.create(value));
    }

    public <T extends JSONData> void put(String key, Map<String, T> value) {
        _data.put(key, JSON.create(value));
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

    public <T extends JSONData> void putStringMap(String key, Map<String, String> value) {
        _data.put(key, JSON.create(value));
    }

    public String toJSONString() {
        return object().toJSONString();
    }

    public String toString() {
        return toJSONString();
    }

    protected <T extends JSONData> CSList<T> createList(String id, final Class<T> type) {
        return createList(id, new Return<T>() {
            public T invoke() {
                return newInstance(type);
            }
        });
    }

    protected <T extends JSONData> CSList<T> createList(String id, Return<T> factory) {
        CSList list = list();
        int index = -1;
        for (JSONType type : iterate(getArray(id))) {
            T item = load(factory.invoke(), type.asObject());
            item.setIndex(++index);
            list.add(item);
        }
        return list;
    }

    public JSONArray getArray(String key) {
        return _data.getArray(key);
    }

    protected <T extends JSONData> T load(T data, JSONObject object) {
        if (no(object)) return null;
        data.load(object);
        return data;
    }

    public void setIndex(int index) {
        _index = index;
    }

    public JSONObject asJSON() {
        return data();
    }

    public final JSONObject data() {
        return _data;
    }

    protected <T extends JSONData> T load(T apiData, JSONData jsonData, String id) {
        return load(apiData, jsonData._data, id);
    }

    public <T extends JSONData> T load(T apiData, JSONObject jsonData, String id) {
        JSONObject object = jsonData.getObject(id);
        if (no(object)) return null;
        apiData.load(object);
        return apiData;
    }

    public <T extends JSONData> T load(T apiData, String id) {
        return load(apiData, _data, id);
    }

    protected void save(JSONObject object, String id, Double string) {
        if (is(string)) object.put(id, string);
    }

    protected void save(JSONObject object, String id, JSONData data) {
        if (is(data)) object.put(id, data.data());
    }

    protected void save(JSONObject object, String id, String string) {
        if (is(string)) object.put(id, string);
    }

    public JSONObject cloneObject() {
        return json(toJSONString()).asObject();
    }
}
