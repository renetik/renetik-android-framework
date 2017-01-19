package cs.java.lang;

import cs.java.collections.CSList;

public interface CSIText extends CharSequence, Appendable, Iterable<Character> {

	CSIText add(CharSequence... string);

	CSIText add(CharSequence string);

	CSIText add(Object... msg);

	CSIText add(Object msg);

	CSIText addLine();

	CSIText addSpace();

	CSIText caseDown();

	CSIText caseUp(int index);

	CSIText cut(int start, int end);

	CSIText cutEnd(int length);

	CSIText leaveStart(int length);

	CSIText cutStart(int length);

	boolean isEmpty();

	CSIText remove(String... strings);

	CSIText replace(String regex, String replace);

	CSIText replaceEnd(String string);

	CSList<CSIText> split(String string);

	CSIText trim();

}
