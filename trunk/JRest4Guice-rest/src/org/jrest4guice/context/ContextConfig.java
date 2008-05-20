package org.jrest4guice.context;

import javax.servlet.FilterConfig;
import javax.servlet.ServletConfig;

public class ContextConfig {
	private ServletConfig servletConfig;
	private FilterConfig filterConfig;

	public ContextConfig(ServletConfig config){
		this.servletConfig = config;
	}
	public ContextConfig(FilterConfig config){
		this.filterConfig = config;
	}
	
	public String getInitParameter(String name){
		if(this.servletConfig != null)
			return this.servletConfig.getInitParameter(name);
		else if(this.filterConfig != null)
			return this.filterConfig.getInitParameter(name);
		else
			return null;
	}
}
