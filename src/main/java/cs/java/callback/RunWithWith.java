package cs.java.callback;

public interface RunWithWith<FirstArgument, SecondArgument> {
    void run(FirstArgument first, SecondArgument second);
}
