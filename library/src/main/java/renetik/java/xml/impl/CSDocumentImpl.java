package renetik.java.xml.impl;

import renetik.java.xml.CSDocument;

public class CSDocumentImpl extends CSTagImpl implements CSDocument {

	public CSDocumentImpl(renetik.java.xml.w3c.CSDocument node) {
		super(node.getDocumentElement());
	}

}
