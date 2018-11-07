package cs.android.xml.w3c.impl;

import org.w3c.dom.Document;

import cs.java.xml.w3c.CSDocument;
import cs.java.xml.w3c.CSElement;

public class CSW3CDocument implements CSDocument {

	private final Document document;

	public CSW3CDocument(Document document) {
		this.document = document;
	}

	@Override
	public CSElement getDocumentElement() {
		return new CSW3CElement(document.getDocumentElement());
	}

}
