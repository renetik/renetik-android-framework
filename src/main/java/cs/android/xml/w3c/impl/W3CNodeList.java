package cs.android.xml.w3c.impl;

import cs.java.xml.w3c.Node;
import cs.java.xml.w3c.NodeList;

public class W3CNodeList implements NodeList {

	private final org.w3c.dom.NodeList nodeList;

	public W3CNodeList(org.w3c.dom.NodeList nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public int getLength() {
		return nodeList.getLength();
	}

	@Override
	public Node item(int index) {
		return new W3CNode(nodeList.item(index));
	}

}
