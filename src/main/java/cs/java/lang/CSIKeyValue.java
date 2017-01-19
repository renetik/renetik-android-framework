package cs.java.lang;

public class CSIKeyValue<Key, Value> {
    public final Key key;
    public final Value value;

    public CSIKeyValue(Key key, Value value) {
        this.key = key;
        this.value = value;
    }
}