package cs.android.json;

import java.util.Map;

import static cs.java.lang.Lang.is;

public class JSONType {

    private final Object _value;

    public JSONType(Object value) {
        this._value = value;
    }

    public JSONArray asArray() {
        return null;
    }

    public Boolean asBoolean() {
        JSONBoolean json = asJSONBoolean();
        if (is(json)) return json.get();
        return null;
    }

    public Double asDouble() {
        JSONNumber json = asJSONNumber();
        if (is(json)) {
            Number number = json.get();
            if (is(number)) return number.doubleValue();
        }
        return null;
    }

    public JSONBoolean asJSONBoolean() {
        return null;
    }

    public JSONNumber asJSONNumber() {
        return null;
    }

    public JSONString asJSONString() {
        return null;
    }

    public JSONObject asObject() {
        return null;
    }

    public String asString() {
        JSONString jsonString = asJSONString();
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
