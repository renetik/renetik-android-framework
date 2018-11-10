package renetik.android.xml.w3c.impl;

import renetik.android.java.xml.w3c.CSElement;
import renetik.android.java.xml.w3c.CSNode;

public class CSW3CNode implements CSNode {

	private final org.w3c.dom.Node item;

	public CSW3CNode(org.w3c.dom.Node item) {
		this.item = item;
	}

	@Override
	public CSElement asElement() {
		return new CSW3CElement((org.w3c.dom.Element) item);
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
