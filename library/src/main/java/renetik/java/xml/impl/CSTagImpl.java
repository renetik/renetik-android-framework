package renetik.java.xml.impl;

import static renetik.android.lang.CSLang.iterate;
import static renetik.android.lang.CSLang.list;
import static renetik.android.lang.CSLang.map;
import static renetik.android.lang.CSLang.no;

import java.util.Iterator;

import renetik.java.collections.CSList;
import renetik.java.collections.CSMap;
import renetik.java.collections.CSMapItem;
import renetik.java.xml.CSTag;
import renetik.java.xml.w3c.CSElement;
import renetik.java.xml.w3c.CSNamedNodeMap;
import renetik.java.xml.w3c.CSNode;
import renetik.java.xml.w3c.CSNodeList;

public class CSTagImpl implements CSTag {
	private final CSElement element;
	private CSList<CSTag> childs;
	private CSMap<String, String> attributes;

	public CSTagImpl(CSElement node) {
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
			CSNamedNodeMap nodeAttributes = element.getAttributes();
			for (int i = 0; i < nodeAttributes.getLength(); i++) {
				CSNode node = nodeAttributes.item(i);
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
	public CSList<CSTag> getChilds() {
		if (no(childs)) {
			childs = list();
			CSNodeList nodes = element.getChildNodes();
			for (int index = 0; index < nodes.getLength(); index++)
				if (nodes.item(index).isElement()) childs.add(new CSTagImpl(nodes.item(index).asElement()));
		}
		return childs;
	}

	private String getChildsString() {
		String code = "";
		for (CSTag child : getChilds())
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
	public Iterator<CSTag> iterator() {
		return getChilds().iterator();
	}

	@Override public String toString() {
		return "\n<" + getName() + getAttributesString() + ">" + getContentString() + "</" + getName()
				+ ">";
	}

}
