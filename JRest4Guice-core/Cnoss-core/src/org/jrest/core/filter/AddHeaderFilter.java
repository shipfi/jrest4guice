package org.jrest.core.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddHeaderFilter implements Filter {
	private final Map headers = new HashMap();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @modelMap request
	 * @modelMap response
	 * @modelMap chain
	 * @throws IOException
	 * @throws ServletException
	 */
	public void doFilter(final HttpServletRequest request,
			final HttpServletResponse response, final FilterChain chain)
			throws IOException, ServletException {
		for (final Iterator it = this.headers.entrySet().iterator(); it
				.hasNext();) {
			final Map.Entry entry = (Map.Entry) it.next();
			response.addHeader((String) entry.getKey(), (String) entry
					.getValue());
		}
		chain.doFilter(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(final ServletRequest req, final ServletResponse res,
			final FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			this.doFilter((HttpServletRequest) req, (HttpServletResponse) res,
					chain);
		} else {
			chain.doFilter(req, res);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(final FilterConfig config) throws ServletException {
		final String headersStr = config.getInitParameter("headers");
		final String[] headers = headersStr.split(",");
		String[] temp;
		for (final String element : headers) {
			temp = element.split("=");
			this.headers.put(temp[0].trim(), temp[1].trim());
		}
	}
}
