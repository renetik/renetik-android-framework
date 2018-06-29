package cs.android.json;

import org.json.JSONTokener;

import java.util.List;
import java.util.Map;

import cs.java.callback.CSReturn;
import cs.java.collections.CSList;

import static cs.java.lang.CSLang.empty;
import static cs.java.lang.CSLang.exception;
import static cs.java.lang.CSLang.iterate;
import static cs.java.lang.CSLang.list;
import static cs.java.lang.CSLang.newInstance;
import static cs.java.lang.CSLang.no;

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
        return createJSONObject().put(data);
    }

    public static CSJSONObject createJSONObject() {
        return new CSJSONObject();
    }

    public static <T extends CSJSONData> CSJSONArray create(List<T> objects) {
        CSJSONArray array = createArray();
        for (T jsonData : objects) array.add(jsonData.asJSONObject());
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

//    public static Object parse2(String json) {
//        try {
//            return convertJSONTypes(new JSONTokener(json).nextValue());
//        } catch (Exception e) {
//            return new CSJSONNoType();
//        }
//    }
//
//    private static Object convertJSONTypes(Object value) {
//        if(value instanceof org.json.JSONObject) {
//            CSMap<String, Object> map = map();
//            for (String key : (org.json.JSONObject) value)
//                value.put(key, get(key).getValue());
//            return value;
//        }
//    }

    public static CSJSONType create(Object value) {
        if (value instanceof CSJSONDataInterface)
            return ((CSJSONDataInterface) value).asJSONObject();
        if (value instanceof List) return new CSJSONArray().add((List) value);
        if (value instanceof Map) return new CSJSONObject().put((Map) value);
        if (value instanceof org.json.JSONObject)
            return new CSJSONObject((org.json.JSONObject) value);
        if (value instanceof org.json.JSONArray) return new CSJSONArray((org.json.JSONArray) value);
        if (value instanceof Boolean) return CSJSONBoolean.getInstance((Boolean) value);
        if (value instanceof String) return new CSJSONString((String) value);
        if (value instanceof Integer) return new CSJSONNumber((Integer) value);
        if (value instanceof Long) return new CSJSONNumber((Long) value);
        if (value instanceof Double) return new CSJSONNumber((Double) value);
        if (value == org.json.JSONObject.NULL) return null;
        if (value == null) return new CSJSONNullType();
        throw exception("some unexpected type ", value);
    }

    public static String toJSONString(List list) {
        return createArray().add(list).toJSONString();
    }

    public static String toJSONString(Map map) {
        return createJSONObject().put(map).toJSONString();
    }

    public static <T extends CSJSONData> CSList<T> createList(final Class<T> type, CSJSONArray array) {
        return createList(() -> newInstance(type), array);
    }

    public static <T extends CSJSONData> CSList<T> createList(CSReturn<T> factory, CSJSONArray array) {
        CSList<T> list = list();
        int index = 0;
        for (CSJSONType dataType : iterate(array)) {
            CSJSONObject data = dataType.asObject();
            if (empty(data)) continue;
            T item = load(factory.invoke(), data);
            item.index(index++);
            list.add(item);
        }
        return list;
    }

    public static <T extends CSJSONData> CSList<CSList<T>> createListOfList(Class<T> type, CSJSONArray arrayOfArray) {
        if (no(arrayOfArray)) return null;
        CSList<CSList<T>> list = list();
        for (CSJSONType arrayInArray : arrayOfArray) {
            int index = 0;
            CSList<T> listInList = list.put((CSList) list());
            for (CSJSONType value : iterate(arrayInArray.asArray())) {
                CSJSONObject data = value.asObject();
                if (empty(data)) continue;
                listInList.put(load(newInstance(type), value.asObject())).index(index++);
            }
        }
        return list;
    }

    public static <T extends CSJSONData> CSList<T> createListByObject(Class<T> type, CSJSONObject objectOfObjects) {
        if (no(objectOfObjects)) return null;
        CSList<T> list = list();
        int index = 0;
        for (String key : objectOfObjects) {
            CSJSONObject data = objectOfObjects.getObject(key);
            if (empty(data)) continue;
            list.put(load(newInstance(type), data)).index(index++).key(key);
        }
        return list;
    }

    public static <T extends CSJSONData> T load(T data, CSJSONObject object) {
        if (no(object)) return null;
        data.load(object);
        return data;
    }

}
