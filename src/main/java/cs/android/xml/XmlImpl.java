package cs.android.xml;

import org.xml.sax.InputSource;

import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import cs.android.xml.w3c.impl.W3CDocument;
import cs.java.xml.XML;
import cs.java.xml.impl.DocumentImpl;

import static cs.java.lang.Lang.exception;

public class XmlImpl implements XML {

    private DocumentBuilder builder;

    @Override
    public cs.java.xml.Document load(String text) {
        try {
            return new DocumentImpl(new W3CDocument(getBuilder().parse(new InputSource(new StringReader(text)))));
        } catch (java.lang.Exception ex) {
            throw exception(ex);
        }
    }

    private DocumentBuilder getBuilder() throws ParserConfigurationException {
        return builder == null ? (builder = DocumentBuilderFactory.newInstance().newDocumentBuilder()) : builder;
    }
}
