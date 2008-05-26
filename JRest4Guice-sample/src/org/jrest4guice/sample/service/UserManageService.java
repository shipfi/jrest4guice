package org.jrest4guice.sample.service;

import java.util.List;

import org.jrest4guice.core.client.Page;
import org.jrest4guice.sample.entity.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.impl.UserManageServiceBean;

import com.google.inject.ImplementedBy;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
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
