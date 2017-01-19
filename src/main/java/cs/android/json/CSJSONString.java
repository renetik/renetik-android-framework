package cs.android.json;

public class CSJSONString extends CSJSONType {

    private final String _value;

    public CSJSONString(String value) {
        super(value);
        _value = value;
    }

    public CSJSONString asJSONString() {
        return this;
    }

    public String get() {
        return _value;
    }

}
