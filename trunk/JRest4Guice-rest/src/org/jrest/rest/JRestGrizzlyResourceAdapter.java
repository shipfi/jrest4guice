package org.jrest.rest;

import java.util.logging.Level;

import com.sun.grizzly.http.servlet.HttpServletRequestImpl;
import com.sun.grizzly.http.servlet.HttpServletResponseImpl;
import com.sun.grizzly.http.servlet.ServletConfigImpl;
import com.sun.grizzly.tcp.Request;
import com.sun.grizzly.tcp.Response;
import com.sun.grizzly.tcp.http11.GrizzlyAdapter;
import com.sun.grizzly.tcp.http11.GrizzlyRequest;
import com.sun.grizzly.tcp.http11.GrizzlyResponse;

public class JRestGrizzlyResourceAdapter extends GrizzlyAdapter {
    public static final int REQUEST_RESPONSE_NOTES = 12;
    public static final int SERVLETCONFIG_NOTES = 13;    

    private RequestProcessor requestProcessor;
    
    public JRestGrizzlyResourceAdapter() {
        super();
        this.init();
    }

    
    public JRestGrizzlyResourceAdapter(String publicDirectory) {
        super(publicDirectory);
        this.init();
    }
    
    private void init(){
    	this.setHandleStaticResources(true);
    	this.requestProcessor = new RequestProcessor();
    }
    
   
    @Override
    public void service(Request request, Response response) throws Exception {
    	String uri = request.getRequestProcessor().getCurrentUri();
//    	System.out.println("service : "+uri);
    	super.service(request, response);
    }

    @Override
    public void service(GrizzlyRequest request, GrizzlyResponse response) {
//    	System.out.println("dynimic ... "+ request.getRequestURI());
        try {
            Request req = request.getRequest();
            Response res = response.getResponse();

            HttpServletRequestImpl httpRequest = (HttpServletRequestImpl) req.getNote(REQUEST_RESPONSE_NOTES);
            HttpServletResponseImpl httpResponse = (HttpServletResponseImpl) res.getNote(REQUEST_RESPONSE_NOTES);
            ServletConfigImpl servletConfig = (ServletConfigImpl) req.getNote(SERVLETCONFIG_NOTES);

            if (httpRequest == null) {
                httpRequest = new HttpServletRequestImpl(request);
                httpResponse = new HttpServletResponseImpl(response);
                req.setNote(REQUEST_RESPONSE_NOTES, httpRequest);
                req.setNote(SERVLETCONFIG_NOTES, servletConfig);
                res.setNote(REQUEST_RESPONSE_NOTES, httpResponse);
            }

    		// REST资源的参数，这些参数都包含在URL中
    		String uri = request.getRequestURI();
    		String contextPath = httpRequest.getContextPath();
    		if(!contextPath.trim().equals("/"))
    			uri = uri.replace(contextPath, "");

    		if (uri == null || "".equals(uri) || "/".equals(uri)) {
    			return;
    		}

    		this.requestProcessor.process(httpRequest, httpResponse);
        
        
        } catch (Throwable ex) {
            getLogger().log(Level.SEVERE, "service exception:", ex);
        }
    }

   
    /**
     * {@inheritDoc}
     */     
    @Override
    public void afterService(GrizzlyRequest request, GrizzlyResponse response) throws Exception {
        Request req = request.getRequest();
        HttpServletRequestImpl httpRequest = (HttpServletRequestImpl) req.getNote(REQUEST_RESPONSE_NOTES);
        HttpServletResponseImpl httpResponse = (HttpServletResponseImpl) req.getNote(REQUEST_RESPONSE_NOTES);

        httpRequest.recycle();
        httpResponse.recycle();
    }
}
