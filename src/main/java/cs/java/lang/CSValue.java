package cs.java.lang;

public class CSValue<T> implements CSValueInterface<T> {

    private T value;

    public CSValue(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
