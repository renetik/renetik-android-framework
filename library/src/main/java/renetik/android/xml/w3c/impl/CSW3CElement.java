package renetik.android.xml.w3c.impl;

import renetik.java.xml.w3c.CSElement;
import renetik.java.xml.w3c.CSNamedNodeMap;
import renetik.java.xml.w3c.CSNodeList;

public class CSW3CElement implements CSElement {

	private final org.w3c.dom.Element element;

	public CSW3CElement(org.w3c.dom.Element element) {
		this.element = element;
	}

	@Override
	public CSNamedNodeMap getAttributes() {
		return new CSW3CNamedNodeMap(element.getAttributes());
	}

	@Override
	public CSNodeList getChildNodes() {
		return new CSW3CNodeList(element.getChildNodes());
	}

	@Override
	public String getNodeName() {
		return element.getNodeName();
	}

	@Override
	public String getTextContent() {
		return element.getNodeValue();
	}

}
