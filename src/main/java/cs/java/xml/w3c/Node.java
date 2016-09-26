package cs.java.xml.w3c;

public interface Node {

	Element asElement();

	String getNodeName();

	String getNodeValue();

	boolean isElement();

}
