package renetik.java.xml.w3c;

public interface CSNode {

	CSElement asElement();

	String getNodeName();

	String getNodeValue();

	boolean isElement();

}
