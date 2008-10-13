package org.jrest4guice.rest.exception;

public class Need2RedirectException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3353226125771080400L;

	
	private String redirectUrl;
	
	public Need2RedirectException(String redirectUrl){
		super();
		this.redirectUrl = redirectUrl;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
}
