package org.jrest4guice.sample.contact.security;

import java.util.List;
import java.util.Map;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.jrest4guice.client.JRestClient;
import org.jrest4guice.client.ModelMap;
import org.jrest4guice.security.Role;
import org.jrest4guice.security.User;

@SuppressWarnings("unchecked")
public class SecurityLoginModule implements LoginModule {
	private Subject subject;

	private CallbackHandler callbackHandler;

	// 鉴别状况
	private boolean succeeded = false;

	private boolean commitSucceeded = false;

	// 用户名和用户密码.
	private String username;

	private char[] password;

	private User user;

	private List<Role> roles;

	JRestClient client;
	
	private String authUrl;
	private String roleListUrl;
	

	public void initialize(Subject subject, CallbackHandler callbackHandler,
			Map sharedState, Map options) {
		this.subject = subject;
		this.callbackHandler = callbackHandler;
		
		this.authUrl = (String)options.get("authUrl");
		this.roleListUrl = (String)options.get("roleListUrl");
		
		if((this.authUrl == null || this.authUrl.trim().equals("")) || (this.roleListUrl == null || this.roleListUrl.trim().equals(""))){
			throw new RuntimeException("没有为登录模板指定 authUrl 和 roleListUrl 属性");
		}
		
		if(!this.roleListUrl.endsWith("/"))
			this.roleListUrl += "/";
	}

	public boolean login() throws LoginException {
		this.client = new JRestClient();
		// 提示输入用户名和密码;
		if (callbackHandler == null) {
			throw new LoginException("没有指明 CallBackHandler!");
		}
		Callback[] callbacks = new Callback[2];
		callbacks[0] = new NameCallback("用户名");
		callbacks[1] = new PasswordCallback("密码", false);
		try {
			callbackHandler.handle(callbacks);
			username = ((NameCallback) callbacks[0]).getName();
			password = ((PasswordCallback) callbacks[1]).getPassword();
		} catch (Exception e) {
			// e.printStackTrace();
		}
		// ==================================================================
		// 处理登录
		// ==================================================================
		ModelMap<String, Object> urlParam = new ModelMap<String, Object>();
		try {
			urlParam.put("userName", username);
			urlParam.put("userPassword", new String(password));
			Object result = client.callRemote(
					this.authUrl, "get", urlParam);
			if (result != null) {
				Boolean value = Boolean.valueOf(result.toString());
				succeeded = value.booleanValue();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// ==================================================================

		return succeeded;
	}

	public boolean commit() throws LoginException {
		if (!succeeded) {
			return false;
		} else {
			user = new User();
			user.setName(this.username);

			if (!subject.getPrincipals().contains(user)) {
				// 注册用户
				subject.getPrincipals().add(user);
			}

			// ==================================================================
			// 查询当前用户下的所有角色
			// ==================================================================
			try {
				List<Role> roles = (List<Role>) client.callRemote(
						this.roleListUrl + this.username,"get", null);
				if (roles != null) {
					this.roles = roles;
					for (Role role : this.roles) {
						// 注册角色
						if (!subject.getPrincipals().contains(role)) {
							subject.getPrincipals().add(role);
						}
					}
				}
			} catch (Exception e) {
				 e.printStackTrace();
			}
			// ==================================================================

			Role guest = new Role();
			guest.setName("guest");
			subject.getPrincipals().add(guest);

			this.clearUserNameAndPassword();
			commitSucceeded = true;
			return true;
		}
	}

	public boolean abort() throws LoginException {
		if (succeeded == false) {
			return false;
		} else if (succeeded == true && commitSucceeded == false) {
			succeeded = false;
			clear();
		} else {
			logout();
		}
		return true;
	}

	private void clear() {
		this.clearUserNameAndPassword();
		user = null;
		roles = null;
	}

	private void clearUserNameAndPassword() {
		username = null;
		if (password != null) {
			for (int i = 0; i < password.length; i++) {
				password[i] = ' ';
			}
			password = null;
		}
	}

	public boolean logout() throws LoginException {
		System.out.println("logout()");
		subject.getPrincipals().remove(user);
		subject.getPrincipals().remove(roles);
		succeeded = commitSucceeded;
		clear();
		return true;
	}
}
