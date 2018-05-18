package org.origin.common.launch.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.origin.common.launch.impl.LaunchConfigAttributes;
import org.w3c.dom.Document;

public class LaunchConfigPersistence {

	public static LaunchConfigPersistence INSTANCE = new LaunchConfigPersistence();

	/**
	 * 
	 * @param launchManager
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public LaunchConfigAttributes load(File file) throws IOException {
		LaunchConfigReader reader = new LaunchConfigReader();
		return reader.read(file);
	}

	/**
	 * 
	 * @param data
	 * @param file
	 */
	public void save(LaunchConfigAttributes data, File file) {
		LaunchConfigWriter writer = new LaunchConfigWriter();
		writer.write(data, file);
	}

	/**
	 * 
	 * @return
	 * @throws ParserConfigurationException
	 */
	public Document getDocument() throws ParserConfigurationException {
		DocumentBuilderFactory dfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dfactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		return doc;
	}

	/**
	 * Serializes a XML document into a string - encoded in UTF8 format, with platform line separators.
	 *
	 * @param domDoc
	 *            document to serialize
	 * @return the document as a string
	 * @throws TransformerException
	 *             if an unrecoverable error occurs during the serialization
	 * @throws IOException
	 *             if the encoding attempted to be used is not supported
	 */
	public String serializeDocument(Document domDoc) throws TransformerException, IOException {
		ByteArrayOutputStream output = null;
		try {
			DOMSource source = new DOMSource(domDoc);

			output = new ByteArrayOutputStream();
			StreamResult outputTarget = new StreamResult(output);

			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.METHOD, "xml"); //$NON-NLS-1$
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			transformer.transform(source, outputTarget);

			return output.toString("UTF8"); //$NON-NLS-1$

		} finally {
			if (output != null) {
				output.close();
			}
		}
	}

}
