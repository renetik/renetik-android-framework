package renetik.android.java.event;

public interface CSEvent<T> {

    CSEventRegistration add(CSListener<T> listener);

    void fire(T arg);

    void clear();

    interface CSEventRegistration {
        void cancel();

        CSEvent<?> event();

        CSEventRegistration setActive(boolean active);

        boolean isActive();
    }
}