package cs.android.xml;

import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cs.android.xml.w3c.impl.CSW3CDocument;
import cs.java.xml.CSDocument;
import cs.java.xml.CSXML;
import cs.java.xml.impl.CSDocumentImpl;

import static cs.java.lang.CSLang.exception;

public class CSXmlImpl implements CSXML {

    private DocumentBuilder builder;

    @Override
    public CSDocument load(String text) {
        try {
            return new CSDocumentImpl(new CSW3CDocument(getBuilder().parse(new InputSource(new StringReader(text)))));
        } catch (java.lang.Exception ex) {
            throw exception(ex);
        }
    }

    private DocumentBuilder getBuilder() throws ParserConfigurationException {
        return builder == null ? (builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()) : builder;
    }
}
