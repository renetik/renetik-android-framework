package cs.java.callback;

public interface CSReturnWith<Type, With> {
	Type invoke(With with);
}
