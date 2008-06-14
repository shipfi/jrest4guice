package org.jrest4guice.sample.contact.service.impl;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManager;

import org.jrest4guice.client.Page;
import org.jrest4guice.client.Pagination;
import org.jrest4guice.commons.lang.MD5Util;
import org.jrest4guice.jpa.BaseEntityManager;
import org.jrest4guice.sample.contact.entity.Role;
import org.jrest4guice.sample.contact.entity.User;
import org.jrest4guice.sample.contact.service.UserManageService;
import org.jrest4guice.transaction.annotations.Transactional;
import org.jrest4guice.transaction.annotations.TransactionalType;

import com.google.inject.Inject;

/**
 * 
 * @author <a href="mailto:zhangyouqun@gmail.com">cnoss (QQ：86895156)</a>
 * 
 */
@SuppressWarnings( { "unchecked", "unused" })
public class UserManageServiceBean implements UserManageService {
	private BaseEntityManager<String, User> userEntityManager;
	private BaseEntityManager<String, Role> roleEntityManager;

	@Inject
	private void init(EntityManager em) {
		this.userEntityManager = new BaseEntityManager<String, User>(
				User.class, em);
		this.roleEntityManager = new BaseEntityManager<String, Role>(
				Role.class, em);
	}

	@Override
	@Transactional(type = TransactionalType.READOLNY)
	public boolean authUser(String name, String password) {
		User user = this.userEntityManager.loadByNamedQuery(
				"byNameAndPassword", name, MD5Util.toMD5(password));
		return user != null;
	}

	@Override
	@Transactional(type = TransactionalType.READOLNY)
	public List<Role> getUserRoles(String name) {
		return this.roleEntityManager.listByNamedQuery("listByUserName", name);
	}

	@Override
	@Transactional(type = TransactionalType.READOLNY)
	@RolesAllowed("admin")
	public User findUser(String name) {
		return this.userEntityManager.loadByNamedQuery("byName", name);
	}

	@Override
	@Transactional(type = TransactionalType.READOLNY)
	@RolesAllowed("admin")
	public Page<Role> getAllRoles(int pageIndex, int pageSize) {
		return this.roleEntityManager.pageByNamedQuery("list", new Pagination(
				pageIndex, pageSize));
	}

	@Override
	@Transactional(type = TransactionalType.READOLNY)
	@RolesAllowed("admin")
	public Page<User> getAllUsers(int pageIndex, int pageSize) {
		return this.userEntityManager.pageByNamedQuery("list", new Pagination(
				pageIndex, pageSize));
	}

}
