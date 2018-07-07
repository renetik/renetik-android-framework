package cs.android.json.old;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

import cs.java.collections.CSList;
import cs.java.collections.CSMap;

import static cs.android.json.old.CSJSON.create;
import static cs.android.json.old.CSJSON.createJSONObject;
import static cs.java.lang.CSLang.NO;
import static cs.java.lang.CSLang.asDouble;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.json;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.no;

public class CSJSONDataOld implements Iterable<String>, CSJSONDataInterface {

    protected CSJSONObject _data;
    private int _index;
    private String _key;
    private String _childDataKey;

    public CSJSONDataOld() {
        _data = createJSONObject();
    }

    public CSJSONDataOld(CSJSONObject object) {
        load(object);
    }

    public void childDataKey(String key) {
        _childDataKey = key;
    }

    public CSJSONDataOld load(CSJSONObject data) {
        if (no(data)) return this;
        _data = data;
        onLoad(data);
        return this;
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
        if (is(_childDataKey)) {
            CSJSONObject object = _data.getObject(_childDataKey);
            if (no(object)) _data.put(_childDataKey, object = createJSONObject());
            return object;
        }
        return _data;
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

    public CSJSONDataOld getData(String key) {
        CSJSONObject object = data().getObject(key);
        return is(object) ? new CSJSONDataOld(object) : null;
    }

    public Double getDouble(String key) {
        try {
            return Double.parseDouble(getString(key));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getString(CSJSONDataOld data, String key) {
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

    public CSList getList(CSJSONObject data, String key) {
        if (no(data) || !data.contains(key)) return null;
        Object value = data.get(key).getValue();
        if (value instanceof CSList) return (CSList) value;
        return null;
    }

    public <T> CSList<T> getList(String key) {
        return getList(asJSONObject(), key);
    }

    public <T> void putList(String key, Iterable<T> list) {
        CSJSONArray jsonArray = new CSJSONArray();
        for (T object : list) jsonArray.add(create(object));
        put(key, jsonArray);
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

    public void load(CSJSONDataOld data) {
        load(data.asJSONObject());
    }

    public CSList<String> getStrings(String id) {
        if (!data().contains(id)) return null;
        CSList<String> list = list();
        loadStrings(list, data(), id);
        return list;
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

    public <T extends CSJSONDataOld> void put(String key, CSList<T> value) {
        data().put(key, create(value));
    }

    public <T extends CSJSONDataOld> void put(String key, Map<String, T> value) {
        data().put(key, create(value));
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

    public <T extends CSJSONDataOld> void putStringMap(String key, Map<String, String> value) {
        data().put(key, create(value));
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

    public CSJSONDataOld index(int index) {
        _index = index;
        return this;
    }

    public CSJSONDataOld key(String key) {
        _key = key;
        return this;
    }

    public CSJSONObject asJSONObject() {
        return data();
    }

    protected <T extends CSJSONDataOld> T load(T apiData, CSJSONDataOld jsonData, String id) {
        return load(apiData, jsonData.asJSONObject(), id);
    }

    public <T extends CSJSONDataOld> T load(T apiData, CSJSONObject jsonData, String id) {
        CSJSONObject object = jsonData.getObject(id);
        if (no(object)) return null;
        apiData.load(object);
        return apiData;
    }

    public <T extends CSJSONDataOld> T load(T apiData, String id) {
        return load(apiData, data(), id);
    }

    protected void save(CSJSONObject object, String id, Double string) {
        if (is(string)) object.put(id, string);
    }

    protected void save(CSJSONObject object, String id, CSJSONDataOld data) {
        if (is(data)) object.put(id, data.asJSONObject());
    }

    protected void save(CSJSONObject object, String id, String string) {
        if (is(string)) object.put(id, string);
    }

    public CSJSONObject cloneObject() {
        return json(toJSONString()).asObject();
    }

    public <T extends CSJSONDataOld> CSList<T> createList(final Class<T> type, String arrayKey) {
        return CSJSON.createList(type, getArray(arrayKey));
    }

    protected <T extends CSJSONDataOld> CSList<CSList<T>> createListOfList(Class<T> type, String arrayOfArrayKey) {
        return CSJSON.createListOfList(type, getArray(arrayOfArrayKey));
    }

    protected <T extends CSJSONDataOld> CSList<T> createListByObject(Class<T> type, String objectKey) {
        return CSJSON.createListByObject(type, getObject(objectKey));
    }

    protected <T extends CSJSONDataOld> CSList<T> sort(CSList<T> data, Comparator<T> comparator) {
        if (no(data)) return null;
        Collections.sort(data, comparator);
        return reIndex(data);
    }

    protected <T extends CSJSONDataOld> CSList<T> reIndex(CSList<T> array) {
        if (no(array)) return null;
        int index = 0;
        for (CSJSONDataOld data : array) data.index(index++);
        return array;
    }

}
