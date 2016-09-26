package cs.android.xml.w3c.impl;

import cs.java.xml.w3c.NamedNodeMap;
import cs.java.xml.w3c.Node;

public class W3CNamedNodeMap implements NamedNodeMap {

	private final org.w3c.dom.NamedNodeMap attributes;

	public W3CNamedNodeMap(org.w3c.dom.NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	@Override
	public int getLength() {
		return attributes.getLength();
	}

	@Override
	public Node item(int index) {
		return new W3CNode(attributes.item(index));
	}

}
