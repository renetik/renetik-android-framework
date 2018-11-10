package renetik.android.java.xml.impl;

import renetik.android.java.xml.CSDocument;

public class CSDocumentImpl extends CSTagImpl implements CSDocument {

	public CSDocumentImpl(renetik.android.java.xml.w3c.CSDocument node) {
		super(node.getDocumentElement());
	}

}
