package cs.java.xml;

import cs.java.collections.CSList;
import cs.java.collections.CSMap;

public interface Tag extends Iterable<Tag> {
	String get(String attribute);

	CSMap<String, String> getAttributes();

	CSList<Tag> getChilds();

	String getName();

	boolean has(String attribute);
}
