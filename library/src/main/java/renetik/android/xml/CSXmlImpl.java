package renetik.android.xml;

import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import renetik.android.xml.w3c.impl.CSW3CDocument;
import renetik.android.java.xml.CSDocument;
import renetik.android.java.xml.CSXML;
import renetik.android.java.xml.impl.CSDocumentImpl;

import static renetik.android.lang.CSLang.exception;

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
