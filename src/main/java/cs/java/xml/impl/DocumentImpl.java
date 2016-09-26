package cs.java.xml.impl;

import cs.java.xml.Document;

public class DocumentImpl extends TagImpl implements Document {

	public DocumentImpl(cs.java.xml.w3c.Document node) {
		super(node.getDocumentElement());
	}

}
