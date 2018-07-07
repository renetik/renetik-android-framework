package cs.android.json.old;

public class CSJSONBoolean extends CSJSONType {

    private static CSJSONBoolean TRUE = new CSJSONBoolean(true);
    private static CSJSONBoolean FALSE = new CSJSONBoolean(false);
    private final Boolean _value;

    private CSJSONBoolean(Boolean value) {
        super(value);
        _value = value;
    }

    public static CSJSONBoolean getInstance(boolean value) {
        return value ? TRUE : FALSE;
    }

    public CSJSONBoolean asJSONBoolean() {
        return this;
    }

    public Boolean getValue() {
        return _value;
    }
}
