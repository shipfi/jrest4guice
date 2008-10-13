package org.jrest4guice.sample.discuz;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jrest4guice.guice.GuiceContext;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.service.UserManageService;

public class DiscuzSecurityCenter extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5699303160574856701L;

	private String loginFailUrl;
	private String discuzPassportUrl;
	private String forward;
	private String securityKey;

	public void init(ServletConfig config) throws ServletException {
		this.loginFailUrl = config.getInitParameter("loginFailUrl");
		if (this.loginFailUrl == null || this.loginFailUrl.trim().equals(""))
			throw new ServletException(
					"没有为DiscuzSecurityCenter提供loginFailUrl参数！");

		this.discuzPassportUrl = config.getInitParameter("discuzPassportUrl");
		if (this.discuzPassportUrl == null
				|| this.discuzPassportUrl.trim().equals(""))
			throw new ServletException(
					"没有为DiscuzSecurityCenter提供discuzPassportUrl参数！");

		this.forward = config.getInitParameter("forward");
		if (this.forward == null)
			this.forward = "";
		this.forward = this.forward.trim();
		
		this.securityKey = config.getInitParameter("securityKey");
		if (this.securityKey == null
				|| this.securityKey.trim().equals(""))
			throw new ServletException(
					"没有为DiscuzSecurityCenter提供securityKey参数！");
	}

	public void service(HttpServletRequest servletReqest,
			HttpServletResponse servletResponse) throws ServletException,
			IOException {
		HttpServletRequest hRequest = (HttpServletRequest) servletReqest;
		HttpServletResponse hResponse = (HttpServletResponse) servletResponse;

		String action = hRequest.getParameter("action");
		if (action == null || action.trim().equals(""))
			action = "login";

		if (action.equalsIgnoreCase("login"))
			this.login(hRequest, hResponse);
		else
			this.logout(hRequest, hResponse);
	}

	private void logout(HttpServletRequest hRequest,
			HttpServletResponse hResponse) throws IOException,
			UnsupportedEncodingException {
		String _forward = this.getForward(hRequest);
		
		String verify = "logout" + _forward + this.securityKey;
		verify = DiscuzPassport.md5(verify);
		String location = this.discuzPassportUrl + "?action=logout&"
				+ "&forward=" + URLEncoder.encode(_forward, "UTF-8")
				+ "&verify=" + verify;

		hResponse.sendRedirect(location);
	}

	private void login(HttpServletRequest hRequest,
			HttpServletResponse hResponse) throws IOException,
			UnsupportedEncodingException {
		Principal userPrincipal = hRequest.getUserPrincipal();
		if (userPrincipal == null) {
			this.redirect2ErrorPage(hResponse);
			return;
		}

		String name = userPrincipal.getName();

		UserManageService ums = GuiceContext.getInstance().getBean(
				UserManageService.class);
		User user = ums.findUser(name);
		if (user == null) {
			this.redirect2ErrorPage(hResponse);
			return;
		}

		String username = user.getName();
		String password = user.getPassword();

		String _forward = this.getForward(hRequest);

		long time = System.currentTimeMillis() / 1000;
		Map<String, String> arrayenc = new HashMap<String, String>();
		arrayenc.put("time", time + "");
		arrayenc.put("username", username);
		arrayenc.put("password", password);
		arrayenc.put("email", "cnoss@snifast.com");

		String enc = DiscuzPassport.passportEncode(arrayenc);
		String auth = DiscuzPassport.encrypt(enc, this.securityKey);
		String verify = "login" + auth + _forward + this.securityKey;
		verify = DiscuzPassport.md5(verify);

		String location = this.discuzPassportUrl + "?action=login&auth="
				+ URLEncoder.encode(auth, "UTF-8") + "&forward="
				+ URLEncoder.encode(_forward, "UTF-8") + "&verify="
				+ URLEncoder.encode(verify, "UTF-8");

		hResponse.sendRedirect(location);
	}

	private String getForward(HttpServletRequest hRequest) {
		String _forward = hRequest.getParameter("forward");
		if(_forward == null)
			_forward = this.forward;
		
		if(_forward.startsWith("'") || _forward.startsWith("\""))
			_forward = _forward.substring(1);
		if(_forward.endsWith("'") || _forward.endsWith("\""))
			_forward = _forward.substring(0,_forward.length()-1);
		return _forward;
	}

	private void redirect2ErrorPage(HttpServletResponse hResponse)
			throws IOException {
		hResponse.sendRedirect(this.loginFailUrl);
	}
}
