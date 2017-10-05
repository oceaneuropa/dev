package org.origin.common.xml;

import java.io.StringReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @see http://www.java2s.com/Tutorials/Java/XML/SAX/Output_line_number_for_SAX_parser_event_handler_in_Java.htm
 *
 */
public class SampleOfXmlLocator extends DefaultHandler {
	private Locator locator;

	public void setDocumentLocator(Locator locator) {
		this.locator = locator;
	}

	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if (qName.equals("order")) {
			System.out.println("here process element start");

		} else {
			String location = "";
			if (locator != null) {
				location = locator.getSystemId(); // XML-document name;
				location += " line " + locator.getLineNumber();
				location += ", column " + locator.getColumnNumber();
				location += ": ";
			}
			throw new SAXException(location + "Illegal element");
		}
	}

	public static void main(String[] args) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		SAXParser parser = factory.newSAXParser();
		parser.parse("sample.xml", new SampleOfXmlLocator());
	}

	public static void main2(String[] argv) throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		SAXParser parser = factory.newSAXParser();
		SampleOfXmlLocator handler = new SampleOfXmlLocator();
		// parser.parse("xmlFileName.xml", handler);

		StringReader sr = new StringReader("<folks></folks>");
		InputSource is = new InputSource(sr);
		parser.parse(is, handler);
	}

}
