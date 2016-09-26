package cs.java.event;

public interface CSEvent<T> {

    EventRegistration add(CSListener<T> listener);

    void fire(T arg);

    void clear();

    interface EventRegistration {
        void cancel();

        CSEvent<?> event();
    }
}