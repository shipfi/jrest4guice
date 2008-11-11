package org.jrest4guice.sample.contact.service;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.commons.lang.MD5Util;
import org.jrest4guice.persistence.BaseEntityManager;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ:86895156)</a>
 * 
 */
@Transactional
public class UserManageService{
	@Inject
	private BaseEntityManager<String, User> userEntityManager;
	@Inject
	private BaseEntityManager<String, Role> roleEntityManager;

	@Transactional(type = TransactionalType.READOLNY)
	public boolean authUser(String name, String password) {
		User user = this.userEntityManager.load(
				"named:byNameAndPassword", name, MD5Util.toMD5(password));
		return user != null;
	}

	@Transactional(type = TransactionalType.READOLNY)
	public List<Role> getUserRoles(String name) {
		return this.roleEntityManager.list("named:listByUserName", name);
	}

	@Transactional(type = TransactionalType.READOLNY)
	public User findUser(String name) {
		return this.userEntityManager.load("named:byName", name);
	}

	@Transactional(type = TransactionalType.READOLNY)
	@RolesAllowed("admin")
	public Page<Role> getAllRoles(int pageIndex, int pageSize) {
		return this.roleEntityManager.page("named:list", new Pagination(
				pageIndex, pageSize));
	}

	@Transactional(type = TransactionalType.READOLNY)
	@RolesAllowed("admin")
	public Page<User> getAllUsers(int pageIndex, int pageSize) {
		return this.userEntityManager.page("named:list", new Pagination(
				pageIndex, pageSize));
	}

}
