package cs.java.lang;

import cs.java.collections.CSList;

public interface CSTextInterface extends CharSequence, Appendable, Iterable<Character> {

	CSTextInterface add(CharSequence... string);

	CSTextInterface add(CharSequence string);

	CSTextInterface add(Object... msg);

	CSTextInterface add(Object msg);

	CSTextInterface addLine();

	CSTextInterface addSpace();

	CSTextInterface caseDown();

	CSTextInterface caseUp(int index);

	CSTextInterface cut(int start, int end);

	CSTextInterface cutEnd(int length);

	CSTextInterface leaveStart(int length);

	CSTextInterface cutStart(int length);

	boolean isEmpty();

	CSTextInterface remove(String... strings);

	CSTextInterface replace(String regex, String replace);

	CSTextInterface replaceEnd(String string);

	CSList<CSTextInterface> split(String string);

	CSTextInterface trim();

}
