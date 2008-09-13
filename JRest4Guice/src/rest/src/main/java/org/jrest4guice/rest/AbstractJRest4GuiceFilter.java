package org.jrest4guice.rest;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public abstract class AbstractJRest4GuiceFilter implements Filter {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4393521946859930914L;

	/**
	 * rest服务资源的url前缀
	 */
	protected String urlPrefix;

	/**
	 * 忽略处理的资源扩展名
	 */
	protected static Set<String> extNameExcludes;
	static {
		extNameExcludes = new HashSet<String>(11);
		extNameExcludes.add("js");
		extNameExcludes.add("jsp");
		extNameExcludes.add("jspa");
		extNameExcludes.add("do");
		extNameExcludes.add("action");
		extNameExcludes.add("html");
		extNameExcludes.add("htm");
		extNameExcludes.add("jpg");
		extNameExcludes.add("gif");
		extNameExcludes.add("png");
		extNameExcludes.add("bmp");
		extNameExcludes.add("swf");
		extNameExcludes.add("css");
		extNameExcludes.add("htc");
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		// 初始化需要被过滤器忽略的资源扩展名
		String _extNameExcludes = config.getInitParameter("extNameExcludes");
		if (_extNameExcludes != null && !_extNameExcludes.trim().equals("")) {
			String[] exts = _extNameExcludes.split(",");
			for (String ext : exts)
				extNameExcludes.add(ext);
		}

		// 初始化rest服务的url前缀
		this.initUrlPrefix(config);
		
		this.executeInit(config);
	}

	/**
	 * 过滤器的主入口
	 */
	public void doFilter(ServletRequest servletReqest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest hRequest = (HttpServletRequest) servletReqest;
		HttpServletResponse hResponse = (HttpServletResponse) servletResponse;

		String uri = hRequest.getRequestURI();
		uri = uri.replace(hRequest.getContextPath(), "");

		// 忽略以下的文件不处理
		String _uri = uri.trim().toLowerCase();
		int index = _uri.lastIndexOf(".");
		if (index != -1) {
			String ext_name = _uri.substring(index + 1);
			if (extNameExcludes.contains(ext_name)) {
				filterChain.doFilter(hRequest, hResponse);
				return;
			}
		}

		if (uri == null || "".equals(uri) || "/".equals(uri)) {
			filterChain.doFilter(hRequest, hResponse);
			return;
		}
		
		this.executeFilter(hRequest, hResponse, filterChain, uri);
	}

	protected abstract void executeInit(FilterConfig config) throws ServletException;
	protected abstract void executeFilter(HttpServletRequest servletReqest,
			HttpServletResponse servletResponse, FilterChain filterChain, String uri)
			throws IOException, ServletException;

	
	/**
	 * 实始化rest服务的url前缀
	 * @param config
	 */
	private void initUrlPrefix(FilterConfig config) {
		try {
			FilterInfoParser servletInfoParser = new FilterInfoParser();
			Map<String, String> filterInfos = servletInfoParser.parse(config
					.getServletContext());
			String urlPattern = filterInfos.get(config.getFilterName());
			this.urlPrefix = urlPattern;

			if (urlPattern.endsWith("*"))
				this.urlPrefix = urlPattern.substring(0,
						urlPattern.length() - 1);

			if (this.urlPrefix.equals("/"))
				this.urlPrefix = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}
	
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
}
