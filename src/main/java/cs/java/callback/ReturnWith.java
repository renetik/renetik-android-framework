package cs.java.callback;

public interface ReturnWith<Type, With> {
	Type invoke(With with);
}
