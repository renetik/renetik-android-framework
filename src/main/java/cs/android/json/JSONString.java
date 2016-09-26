package cs.android.json;

public class JSONString extends JSONType {

    private final String _value;

    public JSONString(String value) {
        super(value);
        _value = value;
    }

    public JSONString asJSONString() {
        return this;
    }

    public String get() {
        return _value;
    }

}
