package org.origin.common.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java
 * 
 */
public class XmlFormatter {

	public static XmlFormatter INSTANCE = new XmlFormatter();

	public String format(String unformattedXml) {
		// try {
		// final Document document = parseXmlFile(unformattedXml);
		//
		// org.apache.xml.serialize.OutputFormat format = new org.apache.xml.serialize.OutputFormat(document);
		// format.setLineWidth(65);
		// format.setIndenting(true);
		// format.setIndent(2);
		// Writer out = new StringWriter();
		// org.apache.xml.serialize.XMLSerializer serializer = new org.apache.xml.serialize.XMLSerializer(out, format);
		// serializer.serialize(document);
		//
		// return out.toString();
		// } catch (IOException e) {
		// throw new RuntimeException(e);
		// }
		return unformattedXml;
	}

	private Document parseXmlFile(String in) {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(in));
			return db.parse(is);

		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String formatExpression(Node doc) {
		String xmlString = null;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(doc);
			transformer.transform(source, result);
			xmlString = result.getWriter().toString();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}

		return xmlString;
	}

	public static void main(String[] args) {
		String unformattedXml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><QueryMessage\n" //
				+ "        xmlns=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/message\"\n" //
				+ "        xmlns:query=\"http://www.SDMX.org/resources/SDMXML/schemas/v2_0/query\">\n" //
				+ "    <Query>\n" //
				+ "        <query:CategorySchemeWhere>\n" //
				+ "   \t\t\t\t\t         <query:AgencyID>ECB</query:AgencyID>\n" //
				+ "        </query:CategorySchemeWhere>\n" //
				+ "    </Query>\n\n\n\n\n" //
				+ "</QueryMessage>";

		System.out.println(XmlFormatter.INSTANCE.format(unformattedXml));
	}

}
