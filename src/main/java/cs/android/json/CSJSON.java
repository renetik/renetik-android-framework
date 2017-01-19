package cs.android.json;

import org.json.JSONTokener;

import java.util.List;
import java.util.Map;

import static cs.java.lang.CSLang.unexpected;

public class CSJSON {

    public static CSJSONBoolean create(Boolean value) {
        return CSJSONBoolean.getInstance(value);
    }

    public static CSJSONNumber create(Number number) {
        return new CSJSONNumber(number);
    }

    public static CSJSONString create(String value) {
        return new CSJSONString(value);
    }

    public static CSJSONObject create(Map data) {
        return createObject().put(data);
    }

    public static CSJSONObject createObject() {
        return new CSJSONObject();
    }

    public static <T extends CSJSONData> CSJSONArray create(List<T> objects) {
        CSJSONArray array = createArray();
        for (T jsonData : objects) array.add(jsonData.data());
        return array;
    }

    public static CSJSONArray createArray(CSJSONType... types) {
        CSJSONArray jsonArray = new CSJSONArray();
        for (CSJSONType type : types) jsonArray.add(type);
        return jsonArray;
    }

    public static CSJSONType parse(String json) {
        try {
            return create(new JSONTokener(json).nextValue());
        } catch (Exception e) {
            return new CSJSONNoType();
        }
    }

    public static CSJSONType create(Object value) {
        if (value instanceof CSIJSON) return ((CSIJSON) value).asJSON();
        if (value instanceof List) return new CSJSONArray().add((List) value);
        if (value instanceof Map) return new CSJSONObject().put((Map) value);
        if (value instanceof org.json.JSONObject) return new CSJSONObject((org.json.JSONObject) value);
        if (value instanceof org.json.JSONArray) return new CSJSONArray((org.json.JSONArray) value);
        if (value instanceof Boolean) return CSJSONBoolean.getInstance((Boolean) value);
        if (value instanceof String) return new CSJSONString((String) value);
        if (value instanceof Integer) return new CSJSONNumber((Integer) value);
        if (value instanceof Long) return new CSJSONNumber((Long) value);
        if (value instanceof Double) return new CSJSONNumber((Double) value);
        if (value == org.json.JSONObject.NULL) return null;
        if (value == null) return new CSJSONNullType();
        throw unexpected("some unexpected type ", value);
    }

    public static String toJSONString(List list) {
        return createArray().add(list).toJSONString();
    }

    public static String toJSONString(Map map) {
        return createObject().put(map).toJSONString();
    }
}
