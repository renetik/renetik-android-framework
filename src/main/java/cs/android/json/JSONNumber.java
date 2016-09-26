package cs.android.json;

public class JSONNumber extends JSONType {

    private final Number _value;

    public JSONNumber(Number value) {
        super(value);
        _value = value;
    }

    public JSONNumber asJSONNumber() {
        return this;
    }

    public Number get() {
        return _value;
    }
}
