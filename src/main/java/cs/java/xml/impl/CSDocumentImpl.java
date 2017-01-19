package cs.java.xml.impl;

import cs.java.xml.CSDocument;

public class CSDocumentImpl extends CSTagImpl implements CSDocument {

	public CSDocumentImpl(cs.java.xml.w3c.CSDocument node) {
		super(node.getDocumentElement());
	}

}
