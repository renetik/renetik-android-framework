package renetik.java.callback;

public interface CSRunWithWith<FirstArgument, SecondArgument> {
    void run(FirstArgument first, SecondArgument second);
}
