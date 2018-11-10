package renetik.android.java.xml;

import renetik.android.java.collections.CSList;
import renetik.android.java.collections.CSMap;

public interface CSTag extends Iterable<CSTag> {
	String get(String attribute);

	CSMap<String, String> getAttributes();

	CSList<CSTag> getChilds();

	String getName();

	boolean has(String attribute);
}
