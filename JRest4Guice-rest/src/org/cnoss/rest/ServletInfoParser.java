package org.cnoss.rest;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

class ServletInfoParser extends DefaultHandler {

	private boolean startParseServletMapping;

	private String servletName;
	private String urlPattern;

	private StringBuffer content;

	private Map<String, String> servletInfos;

	public ServletInfoParser() {
		this.content = new StringBuffer();
		this.servletInfos = new HashMap<String, String>(0);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.clearContent();

		if (qName.equalsIgnoreCase("servlet-mapping")) {
			startParseServletMapping = true;
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("servlet-name")) {
			servletName = null;
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("url-pattern")) {
			urlPattern = null;
		}
	}

	private void clearContent() {
		content.delete(0, content.length());
	}

	public void characters(char ch[], int start, int length)
			throws SAXException {
		content.append(ch, start, length);
	}

	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if (qName.equalsIgnoreCase("servlet-mapping")) {
			startParseServletMapping = false;
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("servlet-name")) {
			servletName = content.toString();
			this.clearContent();
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("url-pattern")) {
			urlPattern = content.toString();
			this.clearContent();
		}

		if (this.servletName != null && this.urlPattern != null) {
			this.servletInfos.put(this.servletName, this.urlPattern);
		}
	}

	public Map<String, String> parse(ServletContext servletContext)
			throws Exception {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setNamespaceAware(false);
		factory.setValidating(false);
		SAXParser parser = factory.newSAXParser();
		servletContext.getContextPath();
		InputStream resourceAsStream = new FileInputStream(servletContext
				.getRealPath("WEB-INF/web.xml"));
		parser.parse(resourceAsStream, this);
		resourceAsStream.close();
		return this.servletInfos;
	}
}
