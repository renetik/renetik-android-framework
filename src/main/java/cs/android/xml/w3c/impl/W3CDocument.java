package cs.android.xml.w3c.impl;

import org.w3c.dom.Document;

import cs.java.xml.w3c.Element;

public class W3CDocument implements cs.java.xml.w3c.Document {

	private final Document document;

	public W3CDocument(Document document) {
		this.document = document;
	}

	@Override
	public Element getDocumentElement() {
		return new W3CElement(document.getDocumentElement());
	}

}
