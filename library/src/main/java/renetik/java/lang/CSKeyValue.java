package renetik.java.lang;

public class CSKeyValue<Key, Value> {
    public final Key key;
    public final Value value;

    public CSKeyValue(Key key, Value value) {
        this.key = key;
        this.value = value;
    }
}