package cs.android.json;

import java.util.Map;

import static cs.java.lang.CSLang.is;

public class CSJSONType {

    private final Object _value;

    public CSJSONType(Object value) {
        this._value = value;
    }

    public CSJSONArray asArray() {
        return null;
    }

    public Boolean asBoolean() {
        CSJSONBoolean json = asJSONBoolean();
        if (is(json)) return json.get();
        return null;
    }

    public Double asDouble() {
        CSJSONNumber json = asJSONNumber();
        if (is(json)) {
            Number number = json.get();
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
        if (is(jsonString)) return jsonString.get();
        return null;
    }

    public Object getValue() {
        return _value;
    }

    public String toJSONString() {
        return String.valueOf(getValue());
    }

    public String toString() {
        return toJSONString();
    }

    public Map<String, String> asMap() {
        return asObject().asMap(String.class);
    }
}
