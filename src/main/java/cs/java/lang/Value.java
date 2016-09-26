package cs.java.lang;

public class Value<T> implements HasValue<T> {

    private T value;

    public Value(T value) {
        this.value = value;
    }

    @Override
		public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
