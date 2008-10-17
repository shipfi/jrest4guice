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
import javax.servlet.http.HttpSession;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jrest4guice.rest.commons.sna.SNAIdRequestServlet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
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
	protected static final Set<String> extNameExcludes = new HashSet<String>();
	static {
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

	/**
	 * 会话服务器的url
	 */
	private String sessionServerUrl;
	
	protected long sessionTimeOut = 15;
	

	/*
	 * (non-Javadoc)
	 * 
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

		this.sessionServerUrl = config.getInitParameter("sessionServerUrl");

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

		if (this.sessionServerUrl != null) {
			// 检测当前应用是否充当了会话服务器的职能，如果是，则直接进入下一个过滤器
			boolean isSessionServerUrl = this.sessionServerUrl.replace(
					hRequest.getRequestURL().toString(), "").trim().equals("");
			if (isSessionServerUrl) {
				filterChain.doFilter(hRequest, hResponse);
				return;
			}

			HttpSession session = hRequest.getSession();
			// 从当前会话中获取snaId
			String snaId = (String) session
					.getAttribute(SNAIdRequestServlet.SNA_ID);
			if (snaId == null) {// 如果没有标识，则尝试从参数中获取
				snaId = hRequest.getParameter(SNAIdRequestServlet.SNA_ID);
			}

			String queryString = hRequest.getQueryString();
			// 如果snaId为null，则重定向到sna会话服务器，获取新的snaId
			if (snaId == null || snaId.trim().equals("")) {
				if (queryString != null && !queryString.equals(""))
					queryString = "&" + queryString;
				else
					queryString = "";

				// 从SNA会话服务器上获取snaId
				String redirectUrl = this.sessionServerUrl + "?sourceUrl="
						+ hRequest.getRequestURL() + ";jsessionid="
						+ session.getId() + queryString;
				hResponse.sendRedirect(redirectUrl);
				return;
			}

			// 保存snaId到当前会话
			if (session.getAttribute(SNAIdRequestServlet.SNA_ID) == null) {
				session.setAttribute(SNAIdRequestServlet.SNA_ID, snaId);

				// 构造查询参数
				queryString = queryString.replaceAll(SNAIdRequestServlet.SNA_ID
						+ "=" + snaId, "");
				if (queryString.startsWith("&"))
					queryString = queryString.replaceFirst("&", "?");
				// 重定向回原始页面
				hResponse.sendRedirect(hRequest.getRequestURL() + queryString);
				return;
			}
		}

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

	protected abstract void executeInit(FilterConfig config)
			throws ServletException;

	protected abstract void executeFilter(HttpServletRequest servletReqest,
			HttpServletResponse servletResponse, FilterChain filterChain,
			String uri) throws IOException, ServletException;

	/**
	 * 实始化rest服务的url前缀
	 * 
	 * @param config
	 */
	private void initUrlPrefix(FilterConfig config) {
		try {
			FilterInfoParser servletInfoParser = new FilterInfoParser();
			servletInfoParser.parse(config.getServletContext());
			
			Map<String, String> filterInfos = servletInfoParser.getFilterInfos();
			
			this.sessionTimeOut = Integer.parseInt(servletInfoParser.getSessionTimeout());
			
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	class FilterInfoParser extends DefaultHandler {

		private boolean startParse;

		private String name;
		private String urlPattern;
		private StringBuffer content;

		private String sessionTimeout = "15";
		private Map<String, String> filterInfos;
		private Map<String, String> servletInfos;

		public FilterInfoParser() {
			this.content = new StringBuffer();
			this.filterInfos = new HashMap<String, String>(0);
			this.servletInfos = new HashMap<String, String>(0);
		}

		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			this.clearContent();

			if (qName.equalsIgnoreCase("filter-mapping")
					|| qName.equalsIgnoreCase("servlet-mapping")) {
				startParse = true;
			}

			if (startParse
					&& (qName.equalsIgnoreCase("filter-name") || qName
							.equalsIgnoreCase("servlet-name"))) {
				name = null;
			}

			if (startParse && qName.equalsIgnoreCase("url-pattern")) {
				urlPattern = null;
			}
			
			if (qName.equalsIgnoreCase("session-timeout")) {
				this.clearContent();
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
				startParse = false;
				this.filterInfos.put(this.name, this.urlPattern);
			}

			if (qName.equalsIgnoreCase("servlet-mapping")) {
				startParse = false;
				this.servletInfos.put(this.name, this.urlPattern);
			}

			if (startParse
					&& (qName.equalsIgnoreCase("filter-name") || qName
							.equalsIgnoreCase("servlet-name"))) {
				name = content.toString();
				this.clearContent();
			}

			if (startParse && qName.equalsIgnoreCase("url-pattern")) {
				urlPattern = content.toString();
				this.clearContent();
			}

			if (qName.equalsIgnoreCase("session-timeout")) {
				this.sessionTimeout = content.toString();
				this.clearContent();
			}
		}
		
		public final Map<String, String> getFilterInfos() {
			return filterInfos;
		}

		public final Map<String, String> getServletInfos() {
			return servletInfos;
		}

		public String getSessionTimeout() {
			return sessionTimeout;
		}

		public void parse(ServletContext servletContext)
				throws Exception {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			factory.setNamespaceAware(false);
			factory.setValidating(false);
			SAXParser parser = factory.newSAXParser();
			servletContext.getContextPath();
			InputStream resourceAsStream = null;
			try {
				resourceAsStream = new FileInputStream(servletContext
						.getRealPath("WEB-INF/web.xml"));
				parser.parse(resourceAsStream, this);
			}finally{
				if(resourceAsStream != null)
					resourceAsStream.close();
			}
			
		}
	}
}
