package renetik.android.xml.w3c.impl;

import renetik.android.java.xml.w3c.CSNode;
import renetik.android.java.xml.w3c.CSNodeList;

public class CSW3CNodeList implements CSNodeList {

	private final org.w3c.dom.NodeList nodeList;

	public CSW3CNodeList(org.w3c.dom.NodeList nodeList) {
		this.nodeList = nodeList;
	}

	@Override
	public int getLength() {
		return nodeList.getLength();
	}

	@Override
	public CSNode item(int index) {
		return new CSW3CNode(nodeList.item(index));
	}

}
