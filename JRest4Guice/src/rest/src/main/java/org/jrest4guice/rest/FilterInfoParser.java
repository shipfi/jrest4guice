package org.jrest4guice.rest;

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

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 *
 */
class FilterInfoParser extends DefaultHandler {

	private boolean startParseServletMapping;

	private String filterName;
	private String urlPattern;

	private StringBuffer content;

	private Map<String, String> servletInfos;

	public FilterInfoParser() {
		this.content = new StringBuffer();
		this.servletInfos = new HashMap<String, String>(0);
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		this.clearContent();

		if (qName.equalsIgnoreCase("filter-mapping")) {
			startParseServletMapping = true;
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("filter-name")) {
			filterName = null;
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
		if (qName.equalsIgnoreCase("filter-mapping")) {
			startParseServletMapping = false;
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("filter-name")) {
			filterName = content.toString();
			this.clearContent();
		}

		if (startParseServletMapping && qName.equalsIgnoreCase("url-pattern")) {
			urlPattern = content.toString();
			this.clearContent();
		}

		if (this.filterName != null && this.urlPattern != null) {
			this.servletInfos.put(this.filterName, this.urlPattern);
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
