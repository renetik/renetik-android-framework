package renetik.java.callback;

public interface CSReturnWith<ReturnType, ArgumentType> {
	ReturnType invoke(ArgumentType with);
}
