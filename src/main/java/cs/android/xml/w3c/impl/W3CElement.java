package cs.android.xml.w3c.impl;

import cs.java.xml.w3c.Element;
import cs.java.xml.w3c.NamedNodeMap;
import cs.java.xml.w3c.NodeList;

public class W3CElement implements Element {

	private final org.w3c.dom.Element element;

	public W3CElement(org.w3c.dom.Element element) {
		this.element = element;
	}

	@Override
	public NamedNodeMap getAttributes() {
		return new W3CNamedNodeMap(element.getAttributes());
	}

	@Override
	public NodeList getChildNodes() {
		return new W3CNodeList(element.getChildNodes());
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
