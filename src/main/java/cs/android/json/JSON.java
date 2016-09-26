package cs.android.json;

import org.json.JSONTokener;

import java.util.List;
import java.util.Map;

import static cs.java.lang.Lang.unexpected;

public class JSON {

    public static JSONBoolean create(Boolean value) {
        return JSONBoolean.getInstance(value);
    }

    public static JSONNumber create(Number number) {
        return new JSONNumber(number);
    }

    public static JSONString create(String value) {
        return new JSONString(value);
    }

    public static JSONObject create(Map data) {
        return createObject().put(data);
    }

    public static JSONObject createObject() {
        return new JSONObject();
    }

    public static <T extends JSONData> JSONArray create(List<T> objects) {
        JSONArray array = createArray();
        for (T jsonData : objects) array.add(jsonData.data());
        return array;
    }

    public static JSONArray createArray(JSONType... types) {
        JSONArray jsonArray = new JSONArray();
        for (JSONType type : types) jsonArray.add(type);
        return jsonArray;
    }

    public static JSONType parse(String json) {
        try {
            return create(new JSONTokener(json).nextValue());
        } catch (Exception e) {
            return new JSONNoType();
        }
    }

    public static JSONType create(Object value) {
        if (value instanceof isJSON) return ((isJSON) value).asJSON();
        if (value instanceof List) return new JSONArray().add((List) value);
        if (value instanceof Map) return new JSONObject().put((Map) value);
        if (value instanceof org.json.JSONObject) return new JSONObject((org.json.JSONObject) value);
        if (value instanceof org.json.JSONArray) return new JSONArray((org.json.JSONArray) value);
        if (value instanceof Boolean) return JSONBoolean.getInstance((Boolean) value);
        if (value instanceof String) return new JSONString((String) value);
        if (value instanceof Integer) return new JSONNumber((Integer) value);
        if (value instanceof Long) return new JSONNumber((Long) value);
        if (value instanceof Double) return new JSONNumber((Double) value);
        if (value == org.json.JSONObject.NULL) return null;
        if (value == null) return new JSONNullType();
        throw unexpected("some unexpected type ", value);
    }

    public static String toJSONString(List list) {
        return createArray().add(list).toJSONString();
    }

    public static String toJSONString(Map map) {
        return createObject().put(map).toJSONString();
    }
}
