package org.origin.common.launch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.origin.common.launch.impl.LaunchConfigAttributes;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class LaunchConfigReader {

	public LaunchConfigReader() {
	}

	protected void handleException(Exception e, File file) throws IOException {
		throw new IOException(e.getMessage() + " exception occurs, while reading launch configuration: " + file.getName(), e);
	}

	public LaunchConfigAttributes read(File file) throws IOException {
		LaunchConfigAttributes configData = null;
		InputStream input = null;
		try {
			input = new FileInputStream(file);
			configData = createInfoFromXML(input);

		} catch (FileNotFoundException e) {
			handleException(e, file);
		} catch (ParserConfigurationException e) {
			handleException(e, file);
		} catch (IOException e) {
			handleException(e, file);
		} catch (SAXException e) {
			handleException(e, file);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					handleException(e, file);
				}
			}
		}
		return configData;
	}

	/**
	 * 
	 * @param input
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public LaunchConfigAttributes createInfoFromXML(InputStream input) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		parser.setErrorHandler(new DefaultHandler());
		Element root = parser.parse(new InputSource(input)).getDocumentElement();

		LaunchConfigAttributes configData = new LaunchConfigAttributes();

		initializeFromXML(configData, root);

		return configData;
	}

	/**
	 * 
	 * @param data
	 * @param root
	 * @throws IOException
	 * @see LaunchConfigurationInfo.initializeFromXML(Element root)
	 */
	protected void initializeFromXML(LaunchConfigAttributes data, Element root) throws IOException {
		// read root
		if (!root.getNodeName().equalsIgnoreCase(LaunchConfigAttributes.LAUNCH_CONFIGURATION)) {
			throw new IOException("Invalid Format. " + LaunchConfigAttributes.LAUNCH_CONFIGURATION + " is not available.");
		}

		// read type
		String typeId = root.getAttribute(LaunchConfigAttributes.TYPE);
		if (typeId == null) {
			throw new IOException("Invalid Format. " + LaunchConfigAttributes.TYPE + " is not available.");
		}

		data.setTypeId(typeId);

		NodeList list = root.getChildNodes();
		Node node = null;
		Element element = null;
		String nodeName = null;
		for (int i = 0; i < list.getLength(); ++i) {
			node = list.item(i);
			short nodeType = node.getNodeType();
			if (nodeType == Node.ELEMENT_NODE) {
				element = (Element) node;
				nodeName = element.getNodeName();
				if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.STRING_ATTRIBUTE)) {
					data.setStringAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.INT_ATTRIBUTE)) {
					data.setIntegerAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.BOOLEAN_ATTRIBUTE)) {
					data.setBooleanAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.LIST_ATTRIBUTE)) {
					data.setListAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.MAP_ATTRIBUTE)) {
					data.setMapAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigAttributes.SET_ATTRIBUTE)) {
					data.setSetAttribute(element);
				}
			}
		}
	}

}
