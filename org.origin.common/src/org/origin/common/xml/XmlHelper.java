package org.origin.common.xml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.origin.common.xml.XmlReader.NodeInfo;
import org.origin.common.xml.XmlReader.PrefixInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class XmlHelper {

	public static XmlHelper INSTANCE = new XmlHelper();

	/**
	 * Update prefix in a DOM document.
	 * 
	 * @param document
	 * @param rootElement
	 * @param nsPrefixToNamespaceMap
	 * @param namespaceToNewPrefixMap
	 * @param nodeInfos
	 * @return
	 */
	public boolean updatePrefix(Document document, Element rootElement, Map<String, String> nsPrefixToNamespaceMap, Map<String, String> namespaceToNewPrefixMap, List<NodeInfo> nodeInfos) {
		if (document == null || rootElement == null) {
			return false;
		}

		boolean isPrefixUpdated = false;

		for (NodeInfo nodeInfo : nodeInfos) {
			Node node = nodeInfo.getNode();

			// 1. Update root attribute Node for namespace map
			// @see https://stackoverflow.com/questions/8367491/set-localname-property-of-an-attribute-node-in-java-dom
			// @see https://stackoverflow.com/questions/10584670/setting-namespaces-and-prefixes-in-a-java-dom-document
			if (nodeInfo.isNsAttributeNode()) {
				String namespace = nodeInfo.getNamespace();
				String nsPrefix = nodeInfo.getPrefix();

				if (namespace != null && namespaceToNewPrefixMap.containsKey(namespace)) {
					String newPrefix = namespaceToNewPrefixMap.get(namespace);
					if (newPrefix != null && !newPrefix.equals(nsPrefix)) {
						rootElement.removeAttribute("xmlns:" + nsPrefix);
						rootElement.setAttribute("xmlns:" + newPrefix, namespace);

						isPrefixUpdated = true;
					}
				}
			}

			// 2. Update Node prefix
			if (nodeInfo.matchNodePrefix()) {
				String oldPrefix = node.getPrefix();
				String namespace = nsPrefixToNamespaceMap.get(oldPrefix);
				if (namespace != null && namespaceToNewPrefixMap.containsKey(namespace)) {
					String newPrefix = namespaceToNewPrefixMap.get(namespace);
					if (newPrefix != null && !newPrefix.equals(oldPrefix)) {
						node.setPrefix(newPrefix);

						isPrefixUpdated = true;
					}
				}
			}

			// 3. Update Node value with prefixes
			if (nodeInfo.matchNodeValue()) {
				String oldNodeValue = node.getNodeValue();
				String newNodeValue = "";

				int index = 0;
				List<PrefixInfo> prefixInfos = nodeInfo.getNodeValuePrefixInfos();
				for (PrefixInfo prefixInfo : prefixInfos) {
					String nsPrefix = prefixInfo.getPrefix();
					int nsPrefixStartIndex = prefixInfo.getStartIndex(); // include
					int nsPrefixEndIndex = prefixInfo.getEndIndex(); // exclude

					String updatedPrefix = nsPrefix;

					String namespace = nsPrefixToNamespaceMap.get(nsPrefix);
					if (namespace != null && namespaceToNewPrefixMap.containsKey(namespace)) {
						String newPrefix = namespaceToNewPrefixMap.get(namespace);
						if (newPrefix != null && !newPrefix.equals(nsPrefix)) {
							updatedPrefix = newPrefix;
						}
					}

					String segmentBeforePrefix = "";
					if (nsPrefixStartIndex > index) {
						segmentBeforePrefix = oldNodeValue.substring(index, nsPrefixStartIndex);
					}

					newNodeValue += (segmentBeforePrefix + updatedPrefix);
					index = nsPrefixEndIndex;
				}
				if (index < oldNodeValue.length()) {
					newNodeValue += oldNodeValue.substring(index);
				}

				if (!newNodeValue.equals(oldNodeValue)) {
					node.setNodeValue(newNodeValue);
					isPrefixUpdated = true;
				}
			}
		}

		return isPrefixUpdated;
	}

	/**
	 * 
	 * @param document
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public String documentToString(Document document) {
		// Writer witer = new StringWriter();
		//
		// org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat(document);
		// // format.setLineWidth(65);
		// format.setIndenting(true);
		// format.setIndent(4);
		//
		// org.apache.xml.serialize.XMLSerializer serializer = new org.apache.xml.serialize.XMLSerializer(witer, format);
		// try {
		// serializer.serialize(document);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// String stringContent = witer.toString();
		// return stringContent;
		return null;
	}

	// @SuppressWarnings("deprecation")
	public void printDocument(String prefix, Document document) {
		// Writer witer = new StringWriter();
		//
		// org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat(document);
		// format.setLineWidth(65);
		// format.setIndenting(true);
		// format.setIndent(2);
		//
		// org.apache.xml.serialize.XMLSerializer serializer = new org.apache.xml.serialize.XMLSerializer(witer, format);
		// try {
		// serializer.serialize(document);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }
		//
		// String stringContent = witer.toString();
		// println(prefix, stringContent);
	}

	public void printDocument(Document document) {
		XmlHelper.INSTANCE.println("", "XML:");
		System.out.println("------------------------------------------------------------------------------------");
		printDocument("", document);
		System.out.println("------------------------------------------------------------------------------------");
		System.out.println();
	}

	public void printNamespaceMap(Map<String, String> namespaceToPrefixMap) {
		System.out.println();
		System.out.println("    XSLT namespace map:");
		for (Iterator<String> nsItor = namespaceToPrefixMap.keySet().iterator(); nsItor.hasNext();) {
			String namespace = nsItor.next();
			String prefix = namespaceToPrefixMap.get(namespace);
			System.out.println("\tnamespace=" + namespace + ", prefix=" + prefix);
		}
	}

	public void printNode(String prefix, Node node) {
		if (node == null) {
			System.out.println(prefix + "node is null");
			return;
		}

		boolean hasAttrs = false;

		String message = "";
		message += "[" + node.getClass().getName() + "]";
		if (node.getBaseURI() != null) {
			message += "baseURI = " + node.getBaseURI();
			hasAttrs = true;
		}
		if (hasAttrs) {
			message += ",";
		}
		message += " prefix = " + node.getPrefix();
		message += ", localName = " + node.getLocalName();
		message += ", nodeName = " + node.getNodeName();
		message += ", nodeType = " + node.getNodeType() + " (" + XmlHelper.INSTANCE.getNodeTypeLabel(node.getNodeType()) + ")";
		message += ", nodeValue = " + node.getNodeValue();
		println(prefix, message);
	}

	public void printNodeAttributes(String prefix, Node node) {
		println(prefix, "Attributes[");
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				Node attributeNode = attributes.item(i);
				// String attrNodeName = attributeNode.getNodeName();
				// String attrNodeValue = attributeNode.getNodeValue();
				printNode(prefix + "    ", attributeNode);
			}
		}
		println(prefix, "]");
		println(prefix, "");
	}

	public void println(String prefix, String message) {
		System.out.println(prefix + message);
	}

	/**
	 * 
	 * @param nodeType
	 * @return
	 */
	public String getNodeTypeLabel(short nodeType) {
		switch (nodeType) {
		case Node.ELEMENT_NODE: // 1
			return "ELEMENT_NODE";

		case Node.ATTRIBUTE_NODE: // 2
			return "ATTRIBUTE_NODE";

		case Node.TEXT_NODE: // 3
			return "TEXT_NODE";

		case Node.CDATA_SECTION_NODE: // 4
			return "CDATA_SECTION_NODE";

		case Node.ENTITY_REFERENCE_NODE: // 5
			return "ENTITY_REFERENCE_NODE";

		case Node.ENTITY_NODE: // 6
			return "ENTITY_NODE";

		case Node.PROCESSING_INSTRUCTION_NODE: // 7
			return "PROCESSING_INSTRUCTION_NODE";

		case Node.COMMENT_NODE: // 8
			return "COMMENT_NODE";

		case Node.DOCUMENT_NODE: // 9
			return "DOCUMENT_NODE";

		case Node.DOCUMENT_TYPE_NODE: // 10
			return "DOCUMENT_TYPE_NODE";

		case Node.DOCUMENT_FRAGMENT_NODE: // 11
			return "DOCUMENT_FRAGMENT_NODE";

		case Node.NOTATION_NODE: // 12
			return "NOTATION_NODE";
		}
		return "unknown";
	}

	public boolean isElementNode(Node node) {
		return (Node.ELEMENT_NODE == node.getNodeType()) ? true : false; // 1
	}

	public boolean isAttributeNode(Node node) {
		return (Node.ATTRIBUTE_NODE == node.getNodeType()) ? true : false; // 2
	}

	public boolean isTextNode(Node node) {
		return (Node.TEXT_NODE == node.getNodeType()) ? true : false; // 3
	}

	public boolean isCDataSectionNode(Node node) {
		return (Node.CDATA_SECTION_NODE == node.getNodeType()) ? true : false; // 4
	}

	public boolean isEntityReferenceNode(Node node) {
		return (Node.ENTITY_REFERENCE_NODE == node.getNodeType()) ? true : false; // 5
	}

	public boolean isEntityNode(Node node) {
		return (Node.ENTITY_NODE == node.getNodeType()) ? true : false; // 6
	}

	public boolean isProcessingInstructionNode(Node node) {
		return (Node.PROCESSING_INSTRUCTION_NODE == node.getNodeType()) ? true : false; // 7
	}

	public boolean isCommentNode(Node node) {
		return (Node.COMMENT_NODE == node.getNodeType()) ? true : false; // 8
	}

	public boolean isDocumentNode(Node node) {
		return (Node.DOCUMENT_NODE == node.getNodeType()) ? true : false; // 9
	}

	public boolean isDocumentTypeNode(Node node) {
		return (Node.DOCUMENT_TYPE_NODE == node.getNodeType()) ? true : false; // 10
	}

	public boolean isDocumentFragmentNode(Node node) {
		return (Node.DOCUMENT_FRAGMENT_NODE == node.getNodeType()) ? true : false; // 11
	}

	public boolean isNotationNode(Node node) {
		return (Node.NOTATION_NODE == node.getNodeType()) ? true : false; // 12
	}

}

// need to update prefix in the Node
// (or maybe remove the node with old prefix and add new node with new prefix)
// no API to set localName

// Warning:
// No such API from Node interface
// node.setNodeName()
// node.setLocalName();
// if (node instanceof org.apache.xerces.dom.DeferredAttrNSImpl) {
// org.apache.xerces.dom.DeferredAttrNSImpl attrNode = (DeferredAttrNSImpl) node;
// }
// if (node instanceof Element) {
// ((Element) node).setAttributeNS(XSDConstants.XMLNS_URI_2000, "xmlns:" + prefixInRegistry,
// namespace);
// }
