package cs.java.lang;

import static cs.java.lang.CSLang.is;

public class CSValue<T> implements CSValueInterface<T> {

    private T value;

    public CSValue(T value) {
        this.value = value;
    }

    public static <T> T value(CSValueInterface<T> hasValue) {
        return is(hasValue) ? hasValue.getValue() : null;
    }

    public T getValue() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
