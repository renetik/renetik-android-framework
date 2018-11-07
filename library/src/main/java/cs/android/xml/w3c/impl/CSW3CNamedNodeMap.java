package cs.android.xml.w3c.impl;

import cs.java.xml.w3c.CSNamedNodeMap;
import cs.java.xml.w3c.CSNode;

public class CSW3CNamedNodeMap implements CSNamedNodeMap {

	private final org.w3c.dom.NamedNodeMap attributes;

	public CSW3CNamedNodeMap(org.w3c.dom.NamedNodeMap attributes) {
		this.attributes = attributes;
	}

	@Override
	public int getLength() {
		return attributes.getLength();
	}

	@Override
	public CSNode item(int index) {
		return new CSW3CNode(attributes.item(index));
	}

}
