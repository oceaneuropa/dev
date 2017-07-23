package org.origin.common.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

public class XmlReader {

	public static class Context {
		protected int level = 0;

		public int getLevel() {
			return level;
		}

		public void setLevel(int level) {
			this.level = level;
		}

		public Context addLevel() {
			return addLevel(1);
		}

		public Context addLevel(int levelToAdd) {
			this.level += levelToAdd;
			return this;
		}

		public String getLevelString() {
			String str = "";
			for (int i = 0; i < level; i++) {
				str += "    ";
			}
			return str;
		}

		public Context clone() {
			Context clone = new Context();
			clone.setLevel(this.level);
			return clone;
		}
	}

	public static class NodeInfo {
		protected Node node;

		// type 1
		// - Node is for NS attribute (which defines prefix and namespace)
		protected boolean isNsAttributeNode;
		protected String namespace; /* used when isNamespaceAttributeNode is true */
		protected String prefix; /* used when isNamespaceAttributeNode is true */

		// type 2
		// - Node has prefix
		protected boolean matchNodePrefix;

		// type 3
		// - Node with value which contains prefix
		protected boolean matchNodeValue;
		protected List<PrefixInfo> nodeValuePrefixInfos; /* used when matchNodeValue is true */

		public NodeInfo(Node node) {
			this.node = node;
		}

		public Node getNode() {
			return this.node;
		}

		public boolean isNsAttributeNode() {
			return this.isNsAttributeNode;
		}

		public void setNsAttributeNode(boolean isNsAttributeNode) {
			this.isNsAttributeNode = isNsAttributeNode;
		}

		public String getNamespace() {
			return namespace;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public boolean matchNodePrefix() {
			return matchNodePrefix;
		}

		public void setMatchNodePrefix(boolean matchNodePrefix) {
			this.matchNodePrefix = matchNodePrefix;
		}

		public boolean matchNodeValue() {
			return matchNodeValue;
		}

		public void setMatchNodeValue(boolean matchNodeValue) {
			this.matchNodeValue = matchNodeValue;
		}

		public List<PrefixInfo> getNodeValuePrefixInfos() {
			return this.nodeValuePrefixInfos;
		}

		public void setNodeValuePrefixInfos(List<PrefixInfo> nodeValuePrefixInfos) {
			this.nodeValuePrefixInfos = nodeValuePrefixInfos;
		}
	}

	public static class PrefixInfo {
		protected String prefix;
		protected int startIndex; // include
		protected int endIndex; // exclude

		public PrefixInfo() {
		}

		public PrefixInfo(String prefix, int startIndex, int endIndex) {
			this.prefix = prefix;
			this.startIndex = startIndex;
			this.endIndex = endIndex;
		}

		public String getPrefix() {
			return prefix;
		}

		public void setPrefix(String prefix) {
			this.prefix = prefix;
		}

		public int getStartIndex() {
			return startIndex;
		}

		public void setStartIndex(int startIndex) {
			this.startIndex = startIndex;
		}

		public int getEndIndex() {
			return endIndex;
		}

		public void setEndIndex(int endIndex) {
			this.endIndex = endIndex;
		}
	}

	// protected DOMParser domParser;
	protected Document document;
	protected Element rootElement;
	protected List<String> globalNsPrefixes = new ArrayList<String>();
	protected Map<String, String> globalNsPrefixToNamespaceMap = new LinkedHashMap<String, String>();
	protected List<NodeInfo> nodesAssociatedWithNsPrefix = new ArrayList<NodeInfo>();
	protected boolean debug = false;

	public XmlReader() {
		// this(new DOMParser());
	}

	// /**
	// *
	// * @param parser
	// */
	// public XmlReader(DOMParser parser) {
	// this.domParser = parser;
	// }

	public Document getDocument() {
		return this.document;
	}

	public Element getRootElement() {
		return this.rootElement;
	}

	public List<String> getNsPrefixes() {
		return this.globalNsPrefixes;
	}

	public Map<String, String> getNsPrefixToNamespaceMap() {
		return this.globalNsPrefixToNamespaceMap;
	}

	public List<NodeInfo> getNodesAssociatedWithNsPrefix() {
		return this.nodesAssociatedWithNsPrefix;
	}

	/**
	 * 
	 * @param xslt
	 */
	public void parse(String xslt) {
		InputSource inputSource = new InputSource(new StringReader(xslt));
		inputSource.setPublicId("String");
		try {
			// this.domParser.parse(inputSource);
			// this.document = domParser.getDocument();

			parse(this.document);

			// } catch (SAXException e) {
			// e.printStackTrace();
			// } catch (IOException e) {
			// e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param document
	 */
	public void parse(Document document) {
		if (debug) {
			XmlHelper.INSTANCE.printDocument(this.document);
		}

		this.rootElement = (document != null) ? document.getDocumentElement() : null;
		if (this.rootElement == null) {
			return;
		}

		this.nodesAssociatedWithNsPrefix.clear();

		parseNamespaceMap(rootElement);

		Context context = new Context();
		traverse(context, rootElement);
	}

	/**
	 * 
	 * @param rootNode
	 */
	protected void parseNamespaceMap(Node rootNode) {
		this.globalNsPrefixes.clear();
		this.globalNsPrefixToNamespaceMap.clear();

		NamedNodeMap attributes = rootNode.getAttributes();
		if (attributes != null) {
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				Node attrNode = attributes.item(i);
				String attrNodeName = attrNode.getNodeName();
				String attrNodeValue = attrNode.getNodeValue();

				if (attrNodeName != null && attrNodeValue != null) {
					boolean isNamespaceAttribute = false;
					if (attrNodeName.startsWith("xmlns:") && attrNodeName.length() > ("xmlns:".length())) {
						isNamespaceAttribute = true;
					}

					if (isNamespaceAttribute) {
						String namespace = attrNodeValue;
						String prefix = attrNodeName.substring("xmlns:".length());

						this.globalNsPrefixes.add(prefix);
						this.globalNsPrefixToNamespaceMap.put(prefix, namespace);

						NodeInfo nodeInfo = new NodeInfo(attrNode);
						nodeInfo.setNsAttributeNode(true);
						nodeInfo.setNamespace(namespace);
						nodeInfo.setPrefix(prefix);
						this.nodesAssociatedWithNsPrefix.add(nodeInfo);
					}
				}
			}
		}
	}

	/**
	 * 
	 * @param context
	 * @param node
	 */
	protected void traverse(Context context, Node node) {
		if (node == null) {
			return;
		}
		if (debug) {
			XmlHelper.INSTANCE.printNode(context.getLevelString(), node);
			XmlHelper.INSTANCE.printNodeAttributes(context.clone().addLevel().getLevelString(), node);
		}

		NodeInfo nodeInfo = new NodeInfo(node);

		// check Node prefix
		String nodePrefix = node.getPrefix();
		if (nodePrefix != null && this.globalNsPrefixes.contains(nodePrefix)) {
			nodeInfo.setMatchNodePrefix(true);
		}

		// check Node value
		String nodeValue = node.getNodeValue();
		if (nodeValue != null && !nodeValue.isEmpty()) {
			List<PrefixInfo> prefixInfos = parsePrefix(this.globalNsPrefixes, nodeValue);
			if (prefixInfos != null && !prefixInfos.isEmpty()) {
				nodeInfo.setNodeValuePrefixInfos(prefixInfos);
				nodeInfo.setMatchNodeValue(true);
			}
		}

		if (nodeInfo.matchNodePrefix() || nodeInfo.matchNodeValue()) {
			this.nodesAssociatedWithNsPrefix.add(nodeInfo);
		}

		// traverse attribute Nodes
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null) {
			int length = attributes.getLength();
			for (int i = 0; i < length; i++) {
				Node attributeNode = attributes.item(i);
				traverse(context, attributeNode);
			}
		}

		// traverse children Nodes
		NodeList children = node.getChildNodes();
		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				Node childNode = children.item(i);
				if (childNode instanceof Text && childNode.getLocalName() == null) {
					continue;
				}
				traverse(context.clone().addLevel(), childNode);
			}
		}
	}

	/**
	 * 
	 * @param globalNsPrefixes
	 * @param string
	 * @return
	 */
	protected List<PrefixInfo> parsePrefix(List<String> globalNsPrefixes, String string) {
		List<PrefixInfo> prefixInfos = new ArrayList<PrefixInfo>();
		parsePrefix(globalNsPrefixes, string, prefixInfos, 0);
		return prefixInfos;
	}

	/**
	 * 
	 * @param globalNsPrefixes
	 * @param string
	 * @param prefixInfos
	 * @param previousLength
	 */
	protected void parsePrefix(List<String> globalNsPrefixes, String string, List<PrefixInfo> prefixInfos, int previousLength) {
		if (string == null || string.isEmpty() || !string.contains(":")) {
			return;
		}

		// This method is based on the idea that prefix is ALWAYS followed by ":".
		// - Split the string into: <part1>:<part2>
		// - Parse prefix from <part1> by checking whether <part1> is "nsPrefix" or ".../nsPrefix"
		// - Recursively parse <part2>

		// find index of first ":" in the string
		int index = string.indexOf(":");

		if (index == 0) {
			// ":" is at start position
			// - There is no prefix before ":"
			// - Continue to parse string content after ":" (if available)
			String part2 = (index < (string.length() - 1)) ? string.substring(index + 1) : null;
			parsePrefix(globalNsPrefixes, part2, prefixInfos, (previousLength + 1));
		}

		if (index == (string.length() - 1)) {
			// ":" is at end position
			// - String content before ":" won't be prefix, since a prefix, if valid, should be followed with a local name.
			// - There is no content to parse after ":"
			return;
		}

		// ":" appears somewhere in the string (not at start position or end position)
		String part1 = string.substring(0, index);
		for (String nsPrefix : globalNsPrefixes) {
			String prefix = null;
			int startIndex = -1;
			int endIndex = -1;

			if (part1.equals(nsPrefix)) {
				// part1 is the prefix
				prefix = nsPrefix;
				startIndex = previousLength + 0;
				endIndex = startIndex + prefix.length();

			} else if (part1.endsWith(nsPrefix)) {
				int index2 = part1.lastIndexOf(nsPrefix);
				// check the one character in part1 before the content of nsPrefix
				String s = part1.substring(index2 - 1, index2);
				if ("/".equals(s)) {
					// part1 content is ".../<nsPrefix>"
					// part1 contains the prefix
					prefix = nsPrefix;
					startIndex = previousLength + index2;
					endIndex = startIndex + prefix.length();
				}
			}

			if (prefix != null) {
				// prefix is found
				PrefixInfo prefixInfo = new PrefixInfo(prefix, startIndex, endIndex);
				prefixInfos.add(prefixInfo);
				break;
			}
		}

		String part2 = string.substring(index + 1);
		parsePrefix(globalNsPrefixes, part2, prefixInfos, (previousLength + part1.length() + 1));
	}

}
