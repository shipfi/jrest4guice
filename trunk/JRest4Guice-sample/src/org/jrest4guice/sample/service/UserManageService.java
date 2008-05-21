package org.jrest4guice.sample.service;

import java.util.List;

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
	public boolean authUser(String name,String password);
	
	public List<User> getAllUsers();

	public List<Role> getUserRoles(String userId);
}
