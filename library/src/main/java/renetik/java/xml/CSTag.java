package renetik.java.xml;

import renetik.java.collections.CSList;
import renetik.java.collections.CSMap;

public interface CSTag extends Iterable<CSTag> {
	String get(String attribute);

	CSMap<String, String> getAttributes();

	CSList<CSTag> getChilds();

	String getName();

	boolean has(String attribute);
}
