package cs.android.json;

public class CSJSONNumber extends CSJSONType {

    private final Number _value;

    public CSJSONNumber(Number value) {
        super(value);
        _value = value;
    }

    public CSJSONNumber asJSONNumber() {
        return this;
    }

    public Number get() {
        return _value;
    }
}
