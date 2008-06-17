package org.jrest4guice.sample.contact.service;

import java.util.List;

import org.jrest4guice.client.Page;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.service.impl.UserManageServiceBean;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@ImplementedBy(UserManageServiceBean.class)
public interface UserManageService {
	public boolean authUser(String name, String password);

	public List<Role> getUserRoles(String name);

	public User findUser(String name);

	public Page<User> getAllUsers(int pageIndex, int pageSize);

	public Page<Role> getAllRoles(int pageIndex, int pageSize);

}
