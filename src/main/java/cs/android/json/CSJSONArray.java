package cs.android.json;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Iterator;
import java.util.List;

import cs.java.collections.CSIterator;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.is;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.warn;

public class CSJSONArray extends CSJSONType implements Iterable<CSJSONType> {


    public CSJSONArray() {
        this(new org.json.JSONArray());
    }

    public CSJSONArray(org.json.JSONArray value) {
        super(value);
    }

    public void add(boolean value) {
        add(CSJSON.create(value));
    }

    public void add(double value) {
        add(CSJSON.create(value));
    }

    public void add(String value) {
        add(CSJSON.create(value));
    }

    public CSJSONArray getArray(int index) {
        CSJSONType value = get(index);
        if (is(value)) {
            CSJSONArray typeValue = value.asArray();
            if (is(typeValue)) return typeValue;
            throw exception("Expected JSONArray, found ", value.getJSONValue());
        }
        return null;
    }

    public Boolean getBoolean(int index) {
        CSJSONType value = get(index);
        if (is(value)) {
            CSJSONBoolean typeValue = value.asJSONBoolean();
            if (is(typeValue)) return typeValue.getValue();
            throw exception("Expected Boolean, found ", value.getJSONValue());
        }
        return null;
    }

    public Number getNumber(int index) {
        CSJSONType value = get(index);
        if (is(value)) {
            CSJSONNumber typeValue = value.asJSONNumber();
            if (is(typeValue)) return typeValue.getValue();
            throw exception("Expected Number, found ", value.getJSONValue());
        }
        return null;
    }

    public CSJSONObject getObject(int index) {
        CSJSONType value = get(index);
        if (is(value)) {
            CSJSONObject typeValue = value.asObject();
            if (is(typeValue)) return typeValue;
            throw exception("Expected JSONObject, found ", value.getJSONValue());
        }
        return null;
    }

    public String getString(int index) {
        CSJSONType value = get(index);
        if (is(value)) {
            CSJSONString typeValue = value.asJSONString();
            if (is(typeValue)) return typeValue.getValue();
            throw exception("Expected String, found ", value.getJSONValue());
        }
        return null;
    }

    public CSJSONArray add(List objects) {
        for (Object object : objects) add(CSJSON.create(object));
        return this;
    }

    public void add(CSJSONType value) {
        set(getSize(), value);
    }

    public CSJSONArray asArray() {
        return this;
    }

    public CSJSONType get(int index) {
        try {
            return CSJSON.create(getJSONValue().get(index));
        } catch (JSONException e) {
            warn(e);
        }
        return null;
    }

    public JSONArray getJSONValue() {
        return (JSONArray) super.getJSONValue();
    }

    public int getSize() {
        return getJSONValue().length();
    }

    public Iterator<CSJSONType> iterator() {
        return new CSIterator<CSJSONType>(getSize()) {
            public CSJSONType getValue() {
                return get(index());
            }
        };
    }

    public void set(int index, CSJSONType type) {
        try {
            getJSONValue().put(index, type.getJSONValue());
        } catch (JSONException e) {
            warn(e);
        }
    }

    public CSList<Object> getValue() {
        CSList<Object> value = list();
        for (CSJSONType type : this) value.add(type.getValue());
        return value;
    }

}