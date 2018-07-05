package org.origin.common.launch.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.origin.common.launch.impl.LaunchConfigData;
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

	public LaunchConfigData read(File file) throws IOException {
		LaunchConfigData configData = null;
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
	public LaunchConfigData createInfoFromXML(InputStream input) throws ParserConfigurationException, IOException, SAXException {
		DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		parser.setErrorHandler(new DefaultHandler());
		Element root = parser.parse(new InputSource(input)).getDocumentElement();

		LaunchConfigData configData = new LaunchConfigData();

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
	protected void initializeFromXML(LaunchConfigData data, Element root) throws IOException {
		// read root
		if (!root.getNodeName().equalsIgnoreCase(LaunchConfigData.LAUNCH_CONFIGURATION)) {
			throw new IOException("Invalid Format. " + LaunchConfigData.LAUNCH_CONFIGURATION + " is not available.");
		}

		// read type
		String typeId = root.getAttribute(LaunchConfigData.TYPE);
		if (typeId == null) {
			throw new IOException("Invalid Format. " + LaunchConfigData.TYPE + " is not available.");
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
				if (nodeName.equalsIgnoreCase(LaunchConfigData.STRING_ATTRIBUTE)) {
					data.setStringAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigData.INT_ATTRIBUTE)) {
					data.setIntegerAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigData.BOOLEAN_ATTRIBUTE)) {
					data.setBooleanAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigData.LIST_ATTRIBUTE)) {
					data.setListAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigData.MAP_ATTRIBUTE)) {
					data.setMapAttribute(element);
				} else if (nodeName.equalsIgnoreCase(LaunchConfigData.SET_ATTRIBUTE)) {
					data.setSetAttribute(element);
				}
			}
		}
	}

}
