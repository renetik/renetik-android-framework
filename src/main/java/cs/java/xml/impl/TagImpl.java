package cs.java.xml.impl;

import static cs.java.lang.Lang.iterate;
import static cs.java.lang.Lang.list;
import static cs.java.lang.Lang.map;
import static cs.java.lang.Lang.no;

import java.util.Iterator;

import cs.java.collections.CSList;
import cs.java.collections.CSMap;
import cs.java.collections.CSMapItem;
import cs.java.xml.Tag;
import cs.java.xml.w3c.Element;
import cs.java.xml.w3c.NamedNodeMap;
import cs.java.xml.w3c.Node;
import cs.java.xml.w3c.NodeList;

public class TagImpl implements Tag {
	private final Element element;
	private CSList<Tag> childs;
	private CSMap<String, String> attributes;

	public TagImpl(Element node) {
		element = node;
	}

	@Override
	public String get(String attribute) {
		return getAttributes().value(attribute);
	}

	@Override
	public CSMap<String, String> getAttributes() {
		if (no(attributes)) {
			attributes = map();
			NamedNodeMap nodeAttributes = element.getAttributes();
			for (int i = 0; i < nodeAttributes.getLength(); i++) {
				Node node = nodeAttributes.item(i);
				attributes.put(node.getNodeName(), node.getNodeValue());
			}
		}
		return attributes;
	}

	private String getAttributesString() {
		String code = " ";
		for (CSMapItem<String, String> attr : iterate(getAttributes()))
			code += attr.key() + "='" + attr.value() + "' ";
		return code;
	}

	@Override
	public CSList<Tag> getChilds() {
		if (no(childs)) {
			childs = list();
			NodeList nodes = element.getChildNodes();
			for (int index = 0; index < nodes.getLength(); index++)
				if (nodes.item(index).isElement()) childs.add(new TagImpl(nodes.item(index).asElement()));
		}
		return childs;
	}

	private String getChildsString() {
		String code = "";
		for (Tag child : getChilds())
			code += child + "";
		return code;
	}

	private String getContentString() {
		if (getChilds().size() > 0) return getChildsString() + "\n";
		String content = element.getTextContent();
		if (content == null) return "";
		return content;
	}

	@Override
	public String getName() {
		return element.getNodeName();
	}

	@Override
	public boolean has(String attribute) {
		return getAttributes().hasKey(attribute);
	}

	@Override
	public Iterator<Tag> iterator() {
		return getChilds().iterator();
	}

	@Override public String toString() {
		return "\n<" + getName() + getAttributesString() + ">" + getContentString() + "</" + getName()
				+ ">";
	}

}
