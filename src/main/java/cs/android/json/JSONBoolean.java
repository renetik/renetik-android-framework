package cs.android.json;

public class JSONBoolean extends JSONType {

    private static JSONBoolean TRUE = new JSONBoolean(true);
    private static JSONBoolean FALSE = new JSONBoolean(false);
    private final Boolean _value;

    private JSONBoolean(Boolean value) {
        super(value);
        _value = value;
    }

    public static JSONBoolean getInstance(boolean value) {
        return value ? TRUE : FALSE;
    }

    public JSONBoolean asJSONBoolean() {
        return this;
    }

    public Boolean get() {
        return _value;
    }
}
