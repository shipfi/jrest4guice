package org.jrest4guice.sample.service.impl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hsqldb.lib.MD5;
import org.jrest4guice.core.jpa.BaseEntityManager;
import org.jrest4guice.core.transaction.annotations.Transactional;
import org.jrest4guice.core.transaction.annotations.TransactionalType;
import org.jrest4guice.core.util.MD5Util;
import org.jrest4guice.sample.entity.Role;
import org.jrest4guice.sample.entity.User;
import org.jrest4guice.sample.service.UserManageService;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss</a>
 *
 */
@SuppressWarnings( { "unchecked", "unused" })
public class UserManageServiceBean implements UserManageService {
	private BaseEntityManager<String, User> userEntityManager;
	private BaseEntityManager<String, Role> roleEntityManager;

	@Inject
	private void init(EntityManager em) {
		this.userEntityManager = new BaseEntityManager<String, User>(User.class, em);
		this.roleEntityManager = new BaseEntityManager<String, Role>(Role.class, em);
	}

	@Transactional(type=TransactionalType.READOLNY)
	public boolean authUser(String name, String password) {
		User user = this.userEntityManager.loadByNamedQuery("byNameAndPassword", name,MD5Util.toMD5(password));
		return user!=null;
	}

	@Transactional(type=TransactionalType.READOLNY)
	public List<Role> getUserRoles(String name) {
		return this.roleEntityManager.listByNamedQuery("listByUserName", name);
	}

	@Override
	@Transactional(type=TransactionalType.READOLNY)
	public List<User> getAllUsers() {
		return this.userEntityManager.listByNamedQuery("list");
	}

}
