package cs.android.json.old;

import static cs.java.lang.CSLang.is;

public abstract class CSJSONType {

    final Object _jsonValue;

    public CSJSONType(Object value) {
        this._jsonValue = value;
    }

    public CSJSONArray asArray() {
        return null;
    }

    public Boolean asBoolean() {
        CSJSONBoolean json = asJSONBoolean();
        if (is(json)) return json.getValue();
        return null;
    }

    public Double asDouble() {
        CSJSONNumber json = asJSONNumber();
        if (is(json)) {
            Number number = json.getValue();
            if (is(number)) return number.doubleValue();
        }
        return null;
    }

    public CSJSONBoolean asJSONBoolean() {
        return null;
    }

    public CSJSONNumber asJSONNumber() {
        return null;
    }

    public CSJSONString asJSONString() {
        return null;
    }

    public CSJSONObject asObject() {
        return null;
    }

    public String asString() {
        CSJSONString jsonString = asJSONString();
        if (is(jsonString)) return jsonString.getValue();
        return null;
    }

    public Object getJSONValue() {
        return _jsonValue;
    }

    public abstract Object getValue();

    public String toJSONString() {
        return String.valueOf(getJSONValue());
    }

    public String toString() {
        return toJSONString();
    }

}
