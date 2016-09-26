package cs.java.xml.w3c;

public interface Element {

	NamedNodeMap getAttributes();

	NodeList getChildNodes();

	String getNodeName();

	String getTextContent();

}
