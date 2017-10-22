package cs.java.lang;

public class CSValue<T> implements CSValueInterface<T> {

    private T value;

    public CSValue(T value) {
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
