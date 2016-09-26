package cs.android.xml.w3c.impl;

import cs.java.xml.w3c.Element;
import cs.java.xml.w3c.Node;

public class W3CNode implements Node {

	private final org.w3c.dom.Node item;

	public W3CNode(org.w3c.dom.Node item) {
		this.item = item;
	}

	@Override
	public Element asElement() {
		return new W3CElement((org.w3c.dom.Element) item);
	}

	@Override
	public String getNodeName() {
		return item.getNodeName();
	}

	@Override
	public String getNodeValue() {
		return item.getNodeValue();
	}

	@Override
	public boolean isElement() {
		return item.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE;
	}

}
