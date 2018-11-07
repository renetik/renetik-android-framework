package cs.java.xml;

import cs.java.collections.CSList;
import cs.java.collections.CSMap;

public interface CSTag extends Iterable<CSTag> {
	String get(String attribute);

	CSMap<String, String> getAttributes();

	CSList<CSTag> getChilds();

	String getName();

	boolean has(String attribute);
}
